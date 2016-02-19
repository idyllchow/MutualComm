package com.idyll.mutualcomm.talk;

import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import java.util.Arrays;

/**
 * @author shibo
 * @packageName com.idyll.mutualcomm.talk
 * @description 群聊助手类
 * @date 16/2/18
 */
public class GroupTalkHelper {

//    private static final

    public static void sendMessageToJerryFromTom(final String sendMsg) {
        // Tom 用自己的名字作为clientId，获取AVIMClient对象实例
        AVIMClient tom = AVIMClient.getInstance("Tom");
        // 与服务器连接
        tom.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e == null) {
                    // 创建与 Jerry，Bob,Harry,William 之间的对话
                    client.createConversation(Arrays.asList("Jerry", "Bob", "Harry", "William"), "Tom & Jerry & friedns", null,
                            new AVIMConversationCreatedCallback() {

                                @Override
                                public void done(AVIMConversation conversation, AVIMException e) {
                                    if (e == null) {
                                        AVIMTextMessage msg = new AVIMTextMessage();
                                        msg.setText(sendMsg);
                                        // 发送消息
                                        conversation.sendMessage(msg, new AVIMConversationCallback() {

                                            @Override
                                            public void done(AVIMException e) {
                                                if (e == null) {
                                                    Log.d("Tom & Jerry", "发送成功！");
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                }
            }
        });
    }


}
