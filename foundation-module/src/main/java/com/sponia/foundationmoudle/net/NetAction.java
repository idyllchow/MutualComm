package com.sponia.foundationmoudle.net;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.sponia.foundationmoudle.bean.SponiaBaseBean;
import com.sponia.foundationmoudle.utils.CellphoneUtil;
import com.sponia.foundationmoudle.utils.LogUtil;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * com.sponia.stats.net
 * 网络链接动作行为类
 * 15/9/6
 * shibo
 */
public class NetAction {

    private IHttpListener httpListener;

    private final OkHttpClient okClient = new OkHttpClient();

    private static final MediaType M_JSON = MediaType.parse("application/json; charset=utf-8");

//    private static final UploadManager uploadManager = new UploadManager();
    //超时时间
    private static final int TIME_OUT = 15;

    /**
     * 取消网络请求
     *
     * @param netMsg
     */
    public void cancelAction(NetMessage netMsg) {
        if (null != netMsg) {
            netMsg.isCancelMsg = true;
        }
    }

    public NetAction(IHttpListener httpListener) {
        this.httpListener = httpListener;
    }


    /**
     * ok get request
     *
     * @param path     请求地址
     * @param netMsg   消息标志
     * @param token    token,不需要时传null或“”
     */
    public void excuteGet(final String path, final NetMessage netMsg, String token) {
        checkNetWork(netMsg);
        Request request;
        if (TextUtils.isEmpty(token)) {
            request = new Request.Builder()
                    .url(path)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(path)
                    .header("token", token)
                    .build();
        }
        LogUtil.defaultLog("get path: " + path + "; header: " + request.headers() + "; params: " + request.body());
        okResponse(request, netMsg);
    }

    /**
     * ok post request
     *
     * @param path     请求地址
     * @param params   参数
     * @param netMsg   消息标志
     * @param token    token
     */
    public void excutePost(final String path, final String params, final NetMessage netMsg, String token) {
        checkNetWork(netMsg);
        Request request;
        if (TextUtils.isEmpty(token)) {
            request = new Request.Builder()
                    .url(path)
                    .post(RequestBody.create(M_JSON, params))
                    .build();
        } else {
            request = new Request.Builder()
                    .url(path)
                    .header("token", token)
                    .post(RequestBody.create(M_JSON, params))
                    .build();
        }
        LogUtil.defaultLog("post path: " + path + "; header: " + request.headers() + "; params: " + request.body());
        okResponse(request, netMsg);
    }

    /**
     * ok put请求
     *
     * @param path     请求地址
     * @param params   参数
     * @param netMsg   消息标志
     * @param token    token
     */
    public void excutePut(final String path, final String params, final NetMessage netMsg, String token) {
        checkNetWork(netMsg);
        Request request;
        if (TextUtils.isEmpty(token)) {
            request = new Request.Builder()
                    .url(path)
                    .put(RequestBody.create(M_JSON, params))
                    .build();
        } else {
            request = new Request.Builder()
                    .url(path)
                    .header("token", token)
                    .put(RequestBody.create(M_JSON, params))
                    .build();
        }
        LogUtil.defaultLog("put path: " + path + "; header: " + request.headers() + "; params: " + request.body());
        okResponse(request, netMsg);
    }

    /**
     * ok delete请求
     *
     * @param path     请求地址
     * @param params   请求参数
     * @param netMsg   请求消息标志
     * @param token    token
     */
    public void excuteDelete(final String path, final String params, final NetMessage netMsg, String token) {
        checkNetWork(netMsg);
        Request request;
        if (TextUtils.isEmpty(token)) {
            request = new Request.Builder()
                    .url(path)
                    .delete(RequestBody.create(M_JSON, params))
                    .build();
        } else {
            request = new Request.Builder()
                    .url(path)
                    .header("token", token)
                    .delete(RequestBody.create(M_JSON, params))
                    .build();
        }
        LogUtil.defaultLog("delete path: " + path + "; header: " + request.headers() + "; params: " + request.body());
        okResponse(request, netMsg);
    }

    /**
     * 7牛上传图片
     * @param file
     * @param key
     * @param token
     * @param handler
     */
//    public void excuteUploadPic(File file,String key,String token,UpCompletionHandler handler) {
//        uploadManager.put(file, key, token, handler, null);
//    }

    /**
     * 发起网络请求,得到响应(返回NetMessagem和json字符串)
     *
     * @param request
     * @param netMsg
     */
    private void okResponse(Request request, final NetMessage netMsg) {
        if (CellphoneUtil.checkNetWorkAvailable()) {
            okClient.setConnectTimeout(TIME_OUT, TimeUnit.SECONDS);
            okClient.setReadTimeout(TIME_OUT, TimeUnit.SECONDS);
            okClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    int errorCode = -1;
                    LogUtil.defaultLog(netMsg.getMessageId() + " onFailure");
                    e.printStackTrace();
                    httpListener.onHttpError(netMsg, errorCode);
                }

                @Override
                public void onResponse(Response response) throws IOException {

                    String str = response.body().string();
                    SponiaBaseBean baseBean;
                    int code = response.code();
                    LogUtil.defaultLog(netMsg.getMessageId() + "----isSuccessful-----" + response.isSuccessful() + "; response code: " + code + "; respon str: " + str);
                    if ((code == 200 || code == 201 || code == 204)) { //200,201-请求成功，或执行成功,204-删除成功
                            httpListener.onHttpSuccess(netMsg, str);
                    } else {
                        if (code != 404 && code != 500 && code != 501 && code != 502 && code != 503 && code != 403) { //404资源不存在,500,服务器异常, 403被劫持严重,故加上
                            baseBean = JSON.parseObject(str, SponiaBaseBean.class);
                            if (baseBean != null && baseBean.error_code != 0) {
                                code = baseBean.error_code;
                            }
                        }
                        httpListener.onHttpError(netMsg, code);
                    }
                }
            });
        } else {
            httpListener.onHttpError(netMsg, IHttpListener.ERROR_NONETWORKACTIVITYEXCEPTION);
        }
    }

    /**
     * 检查网络是否可用
     * @param netMsg
     */
    private void checkNetWork(NetMessage netMsg) {
        if (!CellphoneUtil.checkNetWorkAvailable()) {
            httpListener.onHttpError(netMsg, IHttpListener.ERROR_NONETWORKACTIVITYEXCEPTION);
            return;
        }
    }
}
