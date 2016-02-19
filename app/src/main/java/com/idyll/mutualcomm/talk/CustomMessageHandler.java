package com.idyll.mutualcomm.talk;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.sponia.foundationmoudle.utils.SponiaToastUtil;

/**
 * @author shibo
 * @packageName com.idyll.mutualcomm.talk
 * @description
 * @date 16/2/18
 */
public class CustomMessageHandler extends AVIMMessageHandler {
    //接收到消息后的处理逻辑
    @Override
    public void onMessage(AVIMMessage message,AVIMConversation conversation,AVIMClient client){
        SponiaToastUtil.showShortToast("receive msg " + ((AVIMTextMessage) message).getText());
        AVIMTextMessage reply = new AVIMTextMessage();
//        reply.setText("Tom，我在 Jerry 家，你跟 Harry 什么时候过来？还有 William 和你在一起么？");
        conversation.sendMessage(reply, new AVIMConversationCallback() {
            public void done(AVIMException e) {
                if (e == null) {
                    //回复成功!
                }
            }
        });
    }

    public void onMessageReceipt(AVIMMessage message,AVIMConversation conversation,AVIMClient client){

    }
}
