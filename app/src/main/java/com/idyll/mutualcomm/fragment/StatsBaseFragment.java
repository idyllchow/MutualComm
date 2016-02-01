package com.idyll.mutualcomm.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.idyll.mutualcomm.R;
import com.sponia.foundationmoudle.bean.SponiaBaseBean;
import com.sponia.foundationmoudle.net.IHttpListener;
import com.sponia.foundationmoudle.net.NetMessage;

/**
 * @packageName com.sponia.soccerstats.fragments
 * @description fragment基类
 * @date 15/9/8
 * @auther shibo
 */
public class StatsBaseFragment extends Fragment implements View.OnClickListener, IHttpListener {


    //错误暂不在fragment中处理,以免重复
    @Override
    public void onHttpError(NetMessage netMsg, int errorCode) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(getActivity(), getErrorUnKnown(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onHttpSuccess(NetMessage netMsg, String responceData) {
        String errorStr = "";
        SponiaBaseBean baseBean = JSON.parseObject(responceData, SponiaBaseBean.class);
        int errcode = baseBean.error_code;
        if (baseBean != null && errcode != 0) {
            switch (errcode) {
                case IHttpListener.ERROR_VERIFY_EXCEPTION:
                    errorStr = getString(R.string.error_verify_exception);
                    break;
                case IHttpListener.ERROR_JSON_PARSE_EXCEPTION:
                    errorStr = getString(R.string.error_json_parse_exception);
                    break;
                case IHttpListener.ERROR_REQUEST_VERIFY_EXCEPTION:
                    errorStr = getString(R.string.error_request_verify_exception);
                    break;
                case IHttpListener.ERROR_PERMISSION_EXCEPTION:
                    errorStr = getString(R.string.error_permission_exception);
                    break;
                case IHttpListener.ERROR_TOKEN_EXCEPTION:
                    errorStr = getString(R.string.error_token_exception);
                    break;
                case IHttpListener.ERROR_NO_ACCOUNT_EXCEPTION:
                    errorStr = getString(R.string.error_no_account_exception);
                    break;
                case IHttpListener.ERROR_PHONE_PWD_EXCEPTION:
                    errorStr = getString(R.string.error_phone_pwd_exception);
                    break;
                case IHttpListener.ERROR_PHONE_USED_EXCEPTION:
                    errorStr = getString(R.string.error_phone_used_exception);
                    break;
                case IHttpListener.ERROR_VERIFY_CODE_EXCEPTION:
                    errorStr = getString(R.string.error_verify_code_exception);
                    break;
                case IHttpListener.ERROR_INVITATION_CODE_EXCEPTION:
                    errorStr = getString(R.string.error_invitation_code_exception);
                    break;
                case IHttpListener.ERROR_NO_RESOUCE_EXCEPTION:
                    errorStr = getString(R.string.error_no_resouce_exception);
                    break;
                case IHttpListener.ERROR_ACCOUNT_ACTIVATION_EXCEPTION:
                    errorStr = getString(R.string.error_account_activation_exception);
                    break;
                case IHttpListener.ERROR_SHIRT_NO_EXCEPTION:
                    errorStr = getString(R.string.error_shirt_no_exception);
                    break;
                case IHttpListener.ERROR_EMAIL_USED_EXCEPTION:
                    errorStr = getString(R.string.error_email_used_exception);
                    break;
                case IHttpListener.ERROR_NO_UPDATE_EXCEPTION:
                    errorStr = getString(R.string.error_no_update_exception);
                    break;
                case IHttpListener.ERROR_NO_JOIN_EXCEPTION:
                    errorStr = getString(R.string.error_no_join_exception);
                    break;
                case IHttpListener.ERROR_PARAMS_EXCEPTION:
                    errorStr = getString(R.string.error_params_exception);
                    break;
                default:
                    break;
            }
            final String errorMsg = errorStr;
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public String getErrorNoNetWorkStr() {
        return getResources().getString(R.string.network_error);
    }

    public String getErrorClientTimeoutStr() {
        return getResources().getString(R.string.client_timeout);
    }

    public String getErrorcodeStr() {
        return getResources().getString(R.string.errorCode);
    }

    public String getErrorSSLStr() {
        return getResources().getString(R.string.sslVerifyFail);
    }

    public String getErrorUnKnown() {
        return getResources().getString(R.string.unknown);
    }

    @Override
    public void onClick(View v) {
    }
}
