//package com.idyll.mutualcomm.socket;
//
//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.idyll.mutualcomm.R;
//
///**
// * @author shibo
// * @packageName com.idyll.mutualcomm.socket
// * @description
// * @date 16/2/1
// */
//public class TestActivity extends Activity
//        implements Utilities.ActivityInterface, View.OnClickListener {
//    private boolean isMessaging = false, isSending = false;
//    private TextView mDisplay;
//    private EditText text;
//    private Button sendBtn, menuBtn;
//    private SocketUDPClient socket;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        text = (EditText) findViewById(R.id.message_text);
//        sendBtn = (Button) findViewById(R.id.send_btn);
//        menuBtn = (Button) findViewById(R.id.menu_btn);
//        mDisplay = (TextView) findViewById(R.id.display);
//        socket = new SocketUDPClient(this);
//
//        sendBtn.setOnClickListener(this);
//        menuBtn.setOnClickListener(this);
//
//        registerReceiver(mHandleMessageReceiver, new IntentFilter(
//                DISPLAY_MESSAGE_ACTION));
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v == sendBtn) {
//            if (isMessaging && !text.getText().toString().equals("")) {
//                isSending = true;
//            }
//        } else if (v == menuBtn) {
//            openOptionsMenu();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.socket_connect:
//                if (!isMessaging) {
//                    try {
//                        socket.start();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    text.setEnabled(true);
//                    text.setFocusableInTouchMode(true);
//                    startMessaging();
//                    item.setTitle(R.string.socket_interrupt);
//                } else {
//                    isMessaging = false;
//                    mDisplay.setText("[System]: Socket interrupt.\n");
//                    text.setText("");
//                    text.setEnabled(false);
//                    text.setFocusableInTouchMode(false);
//                    item.setTitle(R.string.socket_connect);
//                }
//                return true;
//            case R.id.clean:
//                mDisplay.setText("");
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    private void startMessaging() {
//        new AsyncTask<Void, Void, Void>() {
//            String messageText;
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                isMessaging = true;
//                while (isMessaging) {
//                    if (isSending) {
//                        publishProgress();
//                        if (socket.getPointAddress() != null && messageText != null) {
//                            socket.doSend(messageText);
//                            socket.broadcast("[me]: " + messageText);
//                            isSending = false;
//                            messageText = null;
//                        }
//                    }
//                }
//                socket.stop();
//                return null;
//            }
//
//            @Override
//            protected void onProgressUpdate(Void... values) {
//                messageText = text.getText().toString();
//            }
//
//        }.execute();
//    }
//
//    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
//            mDisplay.append(newMessage + "\n");
//        }
//    };
//}
