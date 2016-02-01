package com.idyll.mutualcomm.net;

import com.sponia.foundationmoudle.net.NetMessage;

/**
 * com.sponia.soccerstats.net
 * 网络链接，异常处理封装相关类
 * 15/9/6
 * shibo
 */
public interface IHttpListener {


	/**
	 * 网络链接超时
	 */
	 int ERROR_SOCKETTIMEOUTEXCEPTION = 0x0001;
	/**
	 * 无网络活动状态
	 */
	int ERROR_NONETWORKACTIVITYEXCEPTION = 0x0002;
	/**
	 * 服务端异常，httpRespCode非200
	 */
	int ERROR_HTTPSERVICEERROREXCEPTION = 0x0003;
	/**
	 * 网络链接异常
	 */
	int ERROR_IOEXCEPTION = 0x0004;
	/**
	 * SSL证书信任失败
	 */
	int ERROR_SSLVERIFYEXCEPTION = 0x0005;

	/**
	 * 请求参数错误
	 */
	int ERROR_PARAMS_EXCEPTION = 400;

	/**
	 * 资源不存在
	 */
	int ERROR_NO_PERMISSION_EXCEPTION = 403;

	/**
	 * 资源不存在
	 */
	int ERROR_RESOURCE_NOT_FOUND_EXCEPTION = 404;
	/**
	 * 服务端异常
	 */
	int ERROR_SERVER_EXCEPTION = 500;
	/**
	 * 服务端异常
	 */
	int ERROR_SERVER_ONE_EXCEPTION = 501;
	/**
	 * 服务端异常
	 */
	int ERROR_SERVER_TWO_EXCEPTION = 502;
	/**
	 * 服务端异常
	 */
	int ERROR_SERVER_THREE_EXCEPTION = 503;


	/**
	 * 认证错误
	 */
	int ERROR_VERIFY_EXCEPTION = 1001;

	/**
	 * json解析错误
	 */
	int ERROR_JSON_PARSE_EXCEPTION = 1002;

	/**
	 * 请求参数验证失败错误
	 */
	int ERROR_REQUEST_VERIFY_EXCEPTION = 1003;

	/**
	 * 没有相关权限错误
	 */
	int ERROR_PERMISSION_EXCEPTION = 1004;

	/**
	 * 无效的token错误
	 */
	int ERROR_TOKEN_EXCEPTION = 1005;

	/**
	 * 账户信息不存在错误
	 */
	int ERROR_NO_ACCOUNT_EXCEPTION = 1006;

	/**
	 * 手机号或密码错误
	 */
	int ERROR_PHONE_PWD_EXCEPTION = 1007;

	/**
	 * 手机号码已被使用错误
	 */
	int ERROR_PHONE_USED_EXCEPTION = 1008;

	/**
	 * 验证码无效错误
	 */
	int ERROR_VERIFY_CODE_EXCEPTION = 1009;

	/**
	 * 邀请码无效错误
	 */
	int ERROR_INVITATION_CODE_EXCEPTION = 1010;

	/**
	 * 资源不存在错误
	 */
	int ERROR_NO_RESOUCE_EXCEPTION = 1011;

	/**
	 * 邀请码无效错误
	 */
	int ERROR_ACCOUNT_ACTIVATION_EXCEPTION = 1012;

	/**
	 * 同一球队球员相同背号错误
	 */
	int ERROR_SHIRT_NO_EXCEPTION = 1013;

	/**
	 * 邮箱已被使用错误
	 */
	int ERROR_EMAIL_USED_EXCEPTION = 1014;

	/**
	 * 无可更新字段错误
	 */
	int ERROR_NO_UPDATE_EXCEPTION = 1015;

	/**
	 * 球员尚未加入到指定球队错误
	 */
	int ERROR_NO_JOIN_EXCEPTION = 1016;

	/**
	 * 
	 * @param netMsg 网络请求的消息类型
	 * @param errorCode
	 */
	void onHttpError(NetMessage netMsg, int errorCode, String errorMsg) ;
	
	/**
	 * 
	 * @param netMsg 网络请求的消息类型
	 * @param responceData 网络请求下来的数据
	 */
	void onHttpSuccess(NetMessage netMsg, String responceData) ;
	
}
