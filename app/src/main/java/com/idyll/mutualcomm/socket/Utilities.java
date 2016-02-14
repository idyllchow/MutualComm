package com.idyll.mutualcomm.socket;

/**
 * @author shibo
 * @packageName com.idyll.mutualcomm.socket
 * @description
 * @date 16/2/1
 */
public class Utilities {
    public interface SocketInterface {
         String TAG = "Message P2P Socket";
         String SERVER_IP = "Your IP";
         int SERVER_PORT = 8765;
    }

    public interface ActivityInterface {
         String TAG = "Message P2P Activity";
         String DISPLAY_MESSAGE_ACTION = "jhen.example.app.DISPLAY_MESSAGE";
         String EXTRA_MESSAGE = "message";
    }
}
