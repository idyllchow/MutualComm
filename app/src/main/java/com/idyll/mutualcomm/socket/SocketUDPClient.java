//package com.idyll.mutualcomm.socket;
//
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetSocketAddress;
//import java.net.SocketAddress;
//import java.sql.Date;
//
///**
// * @author shibo
// * @packageName com.idyll.mutualcomm.socket
// * @description
// * @date 16/2/1
// */
//public class SocketUDPClient implements Utilities.SocketInterface {
//    private byte[] buffer = new byte[1024];
//    private boolean isReceive = false;
//    private Context context;
//    private DatagramSocket ds;
//    private DatagramPacket rec = new DatagramPacket(buffer, buffer.length);
//    private SocketAddress pointAddress, serverAddress;
//    private Thread receiver, tester;
//
//    public SocketUDPClient(Context context) {
//        this.context = context;
//        serverAddress = new InetSocketAddress(SERVER_IP, SERVER_PORT);
//    }
//
//    public void start() throws Exception {
//        println("start");
//        ds = new DatagramSocket();
//        isReceive = true;
//
//        receiver = new Thread() {
//            public void run() {
//                try {
//                    doSend(serverAddress, "register".getBytes());
//                    ds.receive(rec);
//                    String[] msg = new String(rec.getData(), rec.getOffset(),
//                            rec.getLength()).split(":");
//                    pointAddress = new InetSocketAddress(msg[0],
//                            Integer.parseInt(msg[1]));
//                    broadcast("[System]: Register success.");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                receive();
//            }
//        };
//        receiver.start();
//
//        tester = new Thread() {
//            public void run() {
//                while (isReceive) {
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    if (pointAddress != null) {
//                        doSend("test");
//                    }
//                }
//            }
//        };
//        tester.start();
//    }
//
//    public void stop() {
//        if (ds != null) {
//            ds.close();
//        }
//        isReceive = false;
//        pointAddress = null;
//    }
//
//    private void receive() {
//        while (isReceive) {
//            try {
//                ds.receive(rec);
//                String msg = new String(rec.getData(), rec.getOffset(),
//                        rec.getLength());
//                String line = rec.getSocketAddress() + ":" + msg;
//                println(line);
//                if (!msg.equals("test")) {
//                    onReceive(rec);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void onReceive(DatagramPacket rec) {
//        if (rec.getSocketAddress().equals(pointAddress)) {
//            broadcast("["
//                    + rec.getSocketAddress()
//                    + "]: "
//                    + new String(rec.getData(), rec.getOffset(),
//                    rec.getLength()));
//        }
//    }
//
//    public void broadcast(String message) {
//        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
//        intent.putExtra(EXTRA_MESSAGE, message);
//        context.sendBroadcast(intent);
//    }
//
//    public DatagramSocket getDatagramSocket() {
//        return ds;
//    }
//
//    public SocketAddress getPointAddress() {
//        return pointAddress;
//    }
//
//    public void doSend(String message) {
//        try {
//            println(message);
//            doSend(pointAddress, message.getBytes());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void doSend(SocketAddress addr, byte[] data) throws Exception {
//        DatagramPacket pack = new DatagramPacket(data, data.length, addr);
//        ds.send(pack);
//    }
//
//    private void println(String s) {
//        Log.i(TAG, new Date(System.currentTimeMillis()).toGMTString() + ": "
//                + s);
//    }
//}