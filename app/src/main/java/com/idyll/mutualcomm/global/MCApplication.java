package com.idyll.mutualcomm.global;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;

import com.idyll.mutualcomm.event.DaoMaster;
import com.idyll.mutualcomm.event.DaoSession;
import com.sponia.foundationmoudle.common.Common;
import com.sponia.foundationmoudle.common.SponiaBaseApplication;

import java.util.LinkedList;
import java.util.List;



/**
 * @author shibo
 * @packageName com.sponia.soccerstats.app
 * @description 全局application
 * @date 15/10/8
 */
public class MCApplication extends SponiaBaseApplication {

//    private static Context mContext;
    private static MCApplication mInstance;
    private DaoSession daoSession;
    //对于新增和删除操作add和remove，LinedList比较占优势，因为ArrayList实现了基于动态数组的数据结构，要移动数据。LinkedList基于链表的数据结构,便于增加删除
    private List<Activity> activityList = new LinkedList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Common.application = this;
        setupDatabase();
        // 初始化参数依次为 this, AppId, AppKey
//        AVOSCloud.initialize(this, "LaOIJplvEIVWOvdYkiH85plx-gzGzoHsz", "5lT4DM9YGBH3cidEXveiSwGQ");
//        // 启用北美节点
////        AVOSCloud.useAVCloudUS();
//        //这里指定只处理AVIMTextMessage类型的消息
//        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new CustomMessageHandler());
    }

    public static MCApplication getInstance() {
        return mInstance;
    }

    //添加Activity到容器中
    public void addActivity(Activity activity)  {
        activityList.add(activity);
    }
    //遍历所有Activity并finish
    public void exit(){
        for(Activity activity:activityList) {
            activity.finish();
        }
        System.exit(0);
    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "stats.db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
