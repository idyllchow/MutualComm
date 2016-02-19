package com.idyll.mutualcomm.socket;

import com.idyll.mutualcomm.global.SpCode;
import com.sponia.foundationmoudle.utils.LogUtil;
import com.sponia.foundationmoudle.utils.SponiaSpUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author shibo
 * @packageName com.sponia
 * @description
 * @date 16/1/25
 */
public class MCSocketServer {
    private boolean isStartServer;
    private ServerSocket mServer;
    /**
     * 消息队列，用于保存SocketServer接收来自于客户机（手机端）的消息
     */
    private ArrayList<SocketMessage> mMsgList = new ArrayList<SocketMessage>();
    /**
     * 线程队列，用于接收消息。每个客户机拥有一个线程，每个线程只接收发送给自己的消息
     */
    private ArrayList<SocketThread> mThreadList = new ArrayList<>();

    /**
     * 开启SocketServer
     */
    public void startSocket(int port) {
        try {
            if (mServer != null) {
                mServer.close();
                mServer = null;
            }
            mServer = new ServerSocket();
            mServer.setReuseAddress(true);
            mServer.bind(new InetSocketAddress(port));
            LogUtil.defaultLog("启动server,端口：" + port);
            Socket socket;
            //Android（SocketClient）客户机的唯一标志，每个socketID表示一个Android客户机
            int socketID = 0;
            //开启发送消息线程
            startSendMessageThread();
            isStartServer = true;
            //用一个循环来检测是否有新的客户机加入
            while (isStartServer) {
                //accept()方法是一个阻塞的方法，调用该方法后，
                //该线程会一直阻塞，直到有新的客户机加入，代码才会继续往下走
                socket = mServer.accept();
                //有新的客户机加入后，则创建一个新的SocketThread线程对象
//                SocketThread thread = new SocketThread(socket, socketID++);
                SocketThread thread = new SocketThread(socket, socketID++);
                thread.start();
                //将该线程添加到线程队列
                mThreadList.add(thread);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启推送消息线程，如果mMsgList中有SocketMessage，则把该消息推送到Android客户机
     */
    public void startSendMessageThread() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    /*如果isStartServer=true，则说明SocketServer已启动，
                    用一个循环来检测消息队列中是否有消息，如果有，则推送消息到相应的客户机*/
                    while (isStartServer) {
                        //判断消息队列中的长度是否大于0，大于0则说明消息队列不为空
                        if (mMsgList.size() > 0) {
                            //读取消息队列中的第一个消息
                            LogUtil.defaultLog("startSendMessageThread--->");
                            SocketMessage from = mMsgList.get(0);
                            for (SocketThread to : mThreadList) {
                                if (to.socketID == from.to) {
                                    BufferedWriter writer = to.writer;
                                    JSONObject json = new JSONObject();
                                    json.put("from", from.from);
                                    json.put("msg", from.msg);
                                    json.put("playerNum", from.playerNum);
                                    json.put("eventCode", from.eventCode);
                                    json.put("matchTime", from.matchTime);
                                    json.put("clientStartAt", from.clientStartAt);
                                    json.put("id", from.id);
                                    json.put("time", from.time);
                                    //writer写进json中的字符串数据，末尾记得加换行符："\n"，否则在客户机端无法识别
                                    //因为BufferedReader.readLine()方法是根据换行符来读取一行的
                                    writer.write(json.toString() + "\n");
                                    //调用flush()方法，刷新流缓冲，把消息推送到手机端
                                    writer.flush();
                                    LogUtil.defaultLog("推送消息成功：" + from.msg + ">> to socketID:" + from.to);
                                    break;
                                }
                            }
                            //每推送一条消息之后，就要在消息队列中移除该消息
                            mMsgList.remove(0);
                        }
                        Thread.sleep(200);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 定义一个SocketThread类，用于接收消息
     */
    public class SocketThread extends Thread {

        public int socketID;
        //Socket用于获取输入流、输出流
        public Socket socket;
        //BufferedWriter 用于推送消息
        public BufferedWriter writer;
        //BufferedReader 用于接收消息
        public BufferedReader reader;
        private ArrayList<Integer> socketIDList = new ArrayList<>();

        Set socketIDSet = new HashSet();

        public SocketThread(Socket socket, int count) {
            socketID = count;
            this.socket = socket;
            socketIDList.add(socketID);
            LogUtil.defaultLog("新增一台客户机, ID: " + socketID);
            Set alreadySocket = (Set) SponiaSpUtil.getDefaultSpValue(SpCode.SOCKET_ID);
            Iterator it = alreadySocket.iterator();
            while (it.hasNext()) {
                socketIDSet.add(it.next());
            }
            socketIDSet.add(socketID);
            SponiaSpUtil.setDefaultSpValue(SpCode.SOCKET_ID, socketIDSet);
        }

        @Override
        public void run() {
            super.run();
            try {
                //初始化BufferedReader
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
                //初始化BufferedWriter
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
                //如果isStartServer=true，则说明SocketServer已经启动，
                //现在需要用一个循环来不断接收来自客户机的消息，并作其他处理
                while (isStartServer) {
                    //先判断reader是否已经准备好
                    if (reader.ready()) {
                        /*读取一行字符串，读取的内容来自于客户机
                        reader.readLine()方法是一个阻塞方法，
                        从调用这个方法开始，该线程会一直处于阻塞状态，
                        直到接收到新的消息，代码才会往下走*/
                        String data = reader.readLine();
                        //data作为json对象的内容，创建一个json对象
                        JSONObject json = new JSONObject(data);
                        //创建一个SocketMessage对象，用于接收json中的数据
                        SocketMessage msg = new SocketMessage();
                        msg.to = json.getInt("to");
                        msg.msg = json.getString("msg");
                        msg.playerNum = json.getString("playerNum");
                        msg.eventCode = json.getString("eventCode");
                        msg.matchTime = json.getString("matchTime");
                        msg.clientStartAt = json.getString("clientStartAt");
                        msg.id = json.getString("id");
                        msg.from = socketID;
                        //接收到一条消息后，将该消息添加到消息队列mMsgList
                        mMsgList.add(msg);
                        LogUtil.defaultLog("收到一条消息：" + json.getString("msg") + " >>>> to socketID:" + json.getInt("to") + "---id---" + msg.id);
                    }
                    //睡眠100ms，每100ms检测一次是否有接收到消息
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
