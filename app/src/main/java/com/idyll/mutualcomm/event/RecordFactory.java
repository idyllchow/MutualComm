package com.idyll.mutualcomm.event;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import com.idyll.mutualcomm.global.MCApplication;
import com.sponia.foundationmoudle.utils.LogUtil;
import com.sponia.foundationmoudle.utils.TimeUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

public class RecordFactory {

    private static final String TAG = "RecordFactory";

    public static final String INTENT_EXPORT_DATA = RecordFactory.class.getName() + ".INTENT_EXPORT_DATA";

    public static final String RESULT_CODE = "result";

    public static final int SUCCESS = 0;

    public static final int FAILED = 1;

    public static final int EMPTY = 2;

    public static void insertRecord(MCRecordData record) {
        getMCRecordDataDao().insertInTx(record);
    }

    public static void insertRecords(List<MCRecordData> records) {
        getMCRecordDataDao().insertInTx(records);
    }

    public static void clearRecords() {
        getMCRecordDataDao().deleteAll();
    }

    public static List<MCRecordData> loadAll() {
        return getMCRecordDataDao().loadAll();
    }

    public static List<MCRecordData> queryRecords(String match_id, String team_id) {
        LogUtil.defaultLog("matchId == " + match_id + "; teamId == " + team_id);
        QueryBuilder<MCRecordData> qb = getMCRecordDataDao().queryBuilder();
        qb.where(MCRecordDataDao.Properties.Match_id.eq(match_id), MCRecordDataDao.Properties.Team_id.eq(team_id));
        return qb.list();
    }

    public static void updateRecords(List<MCRecordData> MCRecordDatas) {
        if (null != MCRecordDatas && 0 != MCRecordDatas.size()) {
            getMCRecordDataDao().updateInTx(MCRecordDatas);
        }
    }

    public static void updateRecord(MCRecordData MCRecordData) {
        if (null != MCRecordData) {
            getMCRecordDataDao().update(MCRecordData);
        }
    }

    public static void deleteRecord(MCRecordData MCRecordData) {
        getMCRecordDataDao().delete(MCRecordData);
    }

    public static void deleteRecordByEventKey(String event_key) {
        getMCRecordDataDao().deleteByKey(event_key);
    }

    public static void deleteRecords(ArrayList<String> event_keys) {
        getMCRecordDataDao().deleteByKeyInTx(event_keys);
    }

    private static MCRecordDataDao getMCRecordDataDao() {
        return MCApplication.getInstance().getDaoSession().getMCRecordDataDao();
    }

    public static void exportToCSV(final Context context, final String filePath, final String match_id, final String team_id) {
        AsyncTask asyncTask = new AsyncTask<Object, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Object... params) {
                String time = TimeUtil.getLocalTime(System.currentTimeMillis(), TimeUtil.DATE_FORMAT_ALL);
                //Android文件命名不能包含"："号
                time = time.replace(":", "_");
//                Cursor c = StatsApplication.getInstance().getDaoSession().getDatabase().
//                        rawQuery("SELECT * FROM " + MCRecordDataDao.TABLENAME
//                                + " WHERE " + MCRecordDataDao.Properties.Match_id.columnName + "=? AND "
//                                + MCRecordDataDao.Properties.Team_id.columnName + "=?;", new String[]{match_id, team_id});
                Cursor c = getMCRecordDataDao().getDatabase().
                        rawQuery("SELECT * FROM " + MCRecordDataDao.TABLENAME
                                + " WHERE " + MCRecordDataDao.Properties.Match_id.columnName + "=? AND "
                                + MCRecordDataDao.Properties.Team_id.columnName + "=?;", new String[]{match_id, team_id});
                LogUtil.d(TAG, "正在导出球员动作事件数据....");
                int result1 = exportToFile(c, filePath + File.separator + time + " action.csv");
                if (result1 == SUCCESS) {
                    return SUCCESS;
                } else if (result1 == FAILED) {
                    return FAILED;
                }
                return EMPTY;
            }

            @Override
            protected void onPostExecute(Integer result) {
                if (null != context) {
                    Intent intent = new Intent();
                    intent.setAction(INTENT_EXPORT_DATA);
                    intent.putExtra(RESULT_CODE, result);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }
        };
        asyncTask.execute();
    }

    private static int exportToFile(Cursor c, String fileName) {
        int result = SUCCESS;
        int rowCount = 0;
        int colCount = 0;
        FileWriter fw;
        BufferedWriter bfw;
        File saveFile = new File(fileName);
        try {
            rowCount = c.getCount();
            colCount = c.getColumnCount();
            fw = new FileWriter(saveFile);
            bfw = new BufferedWriter(fw);
            if (rowCount > 0) {
                c.moveToFirst();
                // 写入表头
                for (int i = 0; i < colCount; i++) {
                    if (i != colCount - 1)
                        bfw.write(c.getColumnName(i) + ',');
                    else
                        bfw.write(c.getColumnName(i));
                }
                // 写好表头后换行
                bfw.newLine();
                // 写入数据
                for (int i = 0; i < rowCount; i++) {
                    c.moveToPosition(i);
                    for (int j = 0; j < colCount; j++) {
                        if (j != colCount - 1) {
                            bfw.write(c.getString(j) + ',');
                        } else {
                            bfw.write(c.getString(j));
                        }
                    }
                    // 写好每条记录后换行
                    bfw.newLine();
                }
            } else {
                result = EMPTY;
            }
            // 将缓存数据写入文件
            bfw.flush();
            // 释放缓存
            bfw.close();
            // Toast.makeText(mContext, "导出完毕！", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            result = FAILED;
        } finally {
            c.close();
        }
        return result;
    }
}
