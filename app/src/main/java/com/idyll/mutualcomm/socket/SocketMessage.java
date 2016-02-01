package com.idyll.mutualcomm.socket;

/**
 * @author shibo
 * @packageName com.idyll.mutualcomm.socket
 * @description
 * @date 16/1/25
 */
public class SocketMessage {

    public int to;//socketID，指发送给谁
    public int from;//socketID，指谁发送过来的
    public String msg;//消息内容
    public String playerNum;
    public String eventCode;
    public String matchTime;
    public String clientStartAt;
    public String time;//接收时间
    public MCSocketServer.SocketThread thread;//

}
