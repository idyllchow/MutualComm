package com.idyll.mutualcomm.net;

import java.util.UUID;

/**
 * com.sponia.soccerstats.net
 * 网络请求消息类型
 * 15/9/6
 * shibo
 */
public class NetMessage {

    protected String msgId ;
    
    protected boolean isCancelMsg = false ;

    public NetMessage(String msgId, boolean isCancelMsg) {
        this.msgId = msgId;
        this.isCancelMsg = isCancelMsg;
    }
    
    public NetMessage(String msgId) {
        this.msgId = msgId;
    }
    
    public NetMessage() {
        msgId = UUID.randomUUID().toString().replaceAll("-", "");
        this.isCancelMsg = false;
    }
    
    /**
     * 返回是否被取消
     * @return
     */
    public boolean isCancelMsg() {
        return isCancelMsg;
    }
    
    /**
     * 获取消息id
     * @return
     */
    public String getMessageId() {
        return msgId;
    }
    
}
