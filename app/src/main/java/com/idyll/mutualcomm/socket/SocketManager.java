package com.idyll.mutualcomm.socket;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.sponia.foundationmoudle.utils.LogUtil;
import com.sponia.foundationmoudle.utils.TimeUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * @author shibo
 * @packageName com.idyll.mutualcomm.socket
 * @description
 * @date 16/1/25
 */
public class SocketManager {

    private static StringBuffer mConsoleStr = new StringBuffer();
    private static Socket mSocket;
    private static boolean isStartRecieveMsg;

    private static SocketHandler mHandler;
    protected static BufferedReader mReader;//BufferedWriter 用于推送消息
    protected static BufferedWriter mWriter;//BufferedReader 用于接收消息

    /**
     * 初始化socket
     */
    private static void initSocket(final String ip, final int port) {
        //新建一个线程，用于初始化socket和检测是否有接收到新的消息
        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    isStartRecieveMsg = true;
                    mSocket = new Socket(ip, port);
                    mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), "utf-8"));
                    mWriter = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream(), "utf-8"));
                    LogUtil.defaultLog("mWriter=====>" + mWriter);
                    while(isStartRecieveMsg) {
                        if(mReader.ready()) {
                            /*读取一行字符串，读取的内容来自于客户机
                            reader.readLine()方法是一个阻塞方法，
                            从调用这个方法开始，该线程会一直处于阻塞状态，
                            直到接收到新的消息，代码才会往下走*/
                            String data = mReader.readLine();
                            //handler发送消息，在handleMessage()方法中接收
                            mHandler.obtainMessage(0, data).sendToTarget();
                        }
                        Thread.sleep(200);
                    }
                    mWriter.close();
                    mReader.close();
                    mSocket.close();
                } catch (ConnectException e) {
//                    SponiaToastUtil.showShortToast("连接失败,请重试!");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 发送
     */
    public static void send(String ip, int port) {
        initSocket(ip, port);
        new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... params) {
                sendMsg("0", "test");
                return null;
            }
        }.execute();
    }

    /**
     * 发送消息
     */
    private static void sendMsg(String socketID, String msg) {
        try {
            JSONObject json = new JSONObject();
            json.put("to", socketID);
            json.put("msg", msg);
            LogUtil.defaultLog("mWriter----->" + mWriter);
            mWriter.write(json.toString() + "\n");
            mWriter.flush();
            mConsoleStr.append("我:" + msg + "   " + TimeUtil.getTimeFormatMicrosecond(System.currentTimeMillis()) + "\n");
//            mConsoleTxt.setText(mConsoleStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class SocketHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        //将handler中发送过来的消息创建json对象
                        JSONObject json = new JSONObject((String)msg.obj);
                        mConsoleStr.append(json.getString("from")+":" +json.getString("msg")+"   "+getTime(System.currentTimeMillis())+"\n");
                        //将json数据显示在TextView中
//                        mConsoleTxt.setText(mConsoleStr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                default:
                    break;
            }
        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        isStartRecieveMsg = false;
//    }

    private static String getTime(long millTime) {
        Date d = new Date(millTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }
}
