package com.sponia.foundationmoudle.net;

/**
 * com.sponia.stats.net
 * 处理http状态码非200的情况，封装的异常类型
 * 15/9/6
 * shibo
 */
public class HttpServiceErrorException extends Exception {

	private static final long serialVersionUID = 1L;

	public HttpServiceErrorException() {
		super();
	}

	public HttpServiceErrorException(String message) {
		super(message);
	}
	
}
