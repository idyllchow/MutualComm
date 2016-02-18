package com.sponia.foundationmoudle.net;

/**
 * com.sponia.stats.net
 * 无网络异常
 * 15/9/7
 * shibo
 */
public class NoNetworkActivityException extends Exception {

    private static final long serialVersionUID = 1L;

    public NoNetworkActivityException() {
        super();
    }

    public NoNetworkActivityException(String message) {
        super(message);
    }

}
