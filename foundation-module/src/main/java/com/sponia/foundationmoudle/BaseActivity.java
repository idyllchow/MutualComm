package com.sponia.foundationmoudle;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sponia.foundationmoudle.bean.SponiaBaseBean;
import com.sponia.foundationmoudle.common.PreventContinuousClick;
import com.sponia.foundationmoudle.net.IHttpListener;
import com.sponia.foundationmoudle.net.NetMessage;
import com.sponia.foundationmoudle.utils.LogUtil;
import com.sponia.foundationmoudle.utils.SponiaToastUtil;
import com.sponia.foundationmoudle.view.sweetalert.SweetAlertDialog;


/**
 * @packageName com.sponia.sponiabasketballstats
 * @description
 * @date 15/9/7
 * @auther shibo
 */
public class BaseActivity extends FragmentActivity implements IHttpListener, View.OnClickListener {
    protected static Context mContext;
    //中部视图
    public FrameLayout layoutMid;
    public View midView;
    //顶部标题栏
    protected View actionBar;
    protected ImageView imgTitleLeft;
    protected TextView tvLeft;
    protected TextView tvRight1;
    protected TextView tvRight2;
    protected TextView tvRight3;
    protected ImageView imgRight1;
    protected ImageView imgRight2;
    protected ImageView imgRight3;
    public FrameLayout flyTitleMiddle;
    public View flyTitleRight1;
    public View flyTitleRight2;
    public View flyTitleRight3;
    private TextView tvTitle;
    //title左边按钮
    protected FrameLayout flyTitleLeft;
    //loading dialog
    private SweetAlertDialog loadingDialog;

    /**
     * 主线程ID
     */
    private long uiThreadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(com.sponia.foundationmoudle.R.layout.activity_base);
        initUI();
        uiThreadId = Thread.currentThread().getId();
        mContext = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        actionBar = findViewById(com.sponia.foundationmoudle.R.id.actionbar);
        flyTitleLeft = (FrameLayout) findViewById(com.sponia.foundationmoudle.R.id.fly_title_left);
        imgTitleLeft = (ImageView) findViewById(com.sponia.foundationmoudle.R.id.img_title_left);
        tvLeft = (TextView) findViewById(com.sponia.foundationmoudle.R.id.tv_left);
        flyTitleMiddle = (FrameLayout) findViewById(com.sponia.foundationmoudle.R.id.fly_title_middle);
        tvRight1 = (TextView) findViewById(com.sponia.foundationmoudle.R.id.tv_right1);
        tvRight2 = (TextView) findViewById(com.sponia.foundationmoudle.R.id.tv_right2);
        tvRight3 = (TextView) findViewById(com.sponia.foundationmoudle.R.id.tv_right3);
        imgRight1 = (ImageView) findViewById(com.sponia.foundationmoudle.R.id.img_right1);
        imgRight2 = (ImageView) findViewById(com.sponia.foundationmoudle.R.id.img_right2);
        imgRight3 = (ImageView) findViewById(com.sponia.foundationmoudle.R.id.img_right3);
        tvTitle = (TextView) findViewById(com.sponia.foundationmoudle.R.id.tv_title);
        layoutMid = (FrameLayout) findViewById(com.sponia.foundationmoudle.R.id.base_content);
        flyTitleRight1 = findViewById(com.sponia.foundationmoudle.R.id.fly_title_right1);
        flyTitleRight2 = findViewById(com.sponia.foundationmoudle.R.id.fly_title_right2);
        flyTitleRight3 = findViewById(com.sponia.foundationmoudle.R.id.fly_title_right3);

        flyTitleLeft.setOnClickListener(new PreventContinuousClick(this));
        flyTitleRight1.setOnClickListener(new PreventContinuousClick(this));
        flyTitleRight2.setOnClickListener(new PreventContinuousClick(this));
        flyTitleRight3.setOnClickListener(new PreventContinuousClick(this));
    }

    /**
     * 设置actionbar背景啊颜色
     *
     * @param color
     */
    public void setActionBarBackground(int color) {
        actionBar.setBackgroundResource(color);
    }

    /**
     * 设置actionbar高度
     *
     * @param height
     */
    public void setActionBarHeight(int height) {
        actionBar.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height));
    }

    /**
     * 设置ActionBar不可见
     */
    public void setActionBarGone() {
        actionBar.setVisibility(View.GONE);
    }

    /**
     * 设置ActionBar可见
     */
    public void setActionBarVisiable() {
        actionBar.setVisibility(View.VISIBLE);
    }

    /**
     * 设置标题居中
     */
    public void setActionBarTitle2Center() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        tvTitle.setLayoutParams(layoutParams);
    }

    /**
     * 设置标题居左
     */
    public void setActionBarTitle2Left() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.RIGHT_OF, com.sponia.foundationmoudle.R.id.fly_title_left);
    }

    /**
     * 设置左边title为图片
     *
     * @param resId
     */
    public void setTitleLeftImg(int resId) {
        flyTitleLeft.setVisibility(View.VISIBLE);
        imgTitleLeft.setVisibility(View.VISIBLE);
        imgTitleLeft.setImageResource(resId);
    }

    /**
     * 设置左边标题
     *
     * @param text
     */
    public void setTitleLeftText(String text) {
        tvLeft.setText(text);
    }

    /**
     * 设置左边标题不可见
     */
    public void setTitleTitleGone() {
        flyTitleLeft.setVisibility(View.GONE);
    }

    /**
     * 文本格式设置actionbar title
     *
     * @param title
     */
    public void setActionbarTitle(String title) {
        tvTitle.setText(title);
    }

    public void setActionbarTitleVisble() {
        tvTitle.setVisibility(View.VISIBLE);
    }

    public void setActionbarTitleGone() {
        tvTitle.setVisibility(View.GONE);
    }

    public void setTitleMiddleView(View view) {
        flyTitleMiddle.removeAllViews();
        flyTitleMiddle.addView(view, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
    }

    /**
     * 以html格式设置标题
     *
     * @param title
     */
    public void setActionbarTitle(Spanned title) {
        tvTitle.setText(title);
    }

    /**
     * 设置标题文字大小
     *
     * @param size
     */
    public void setTitleSize(int size) {
        tvTitle.setTextSize(size);
    }

    /**
     * 设置标题旁边的文字
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setTitleCompoundDrawable(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        tvTitle.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
    }

    /**
     * 设置右边第一个标题可见
     */
    public void setTitleRight1Visible() {
        flyTitleRight1.setVisibility(View.VISIBLE);
    }
    /**
     * 设置右边第一个标题不可见
     */
    public void setTitleRight1Gone() {
        flyTitleRight1.setVisibility(View.GONE);
    }

    /**
     * 设置右边第一个标题文字
     *
     * @param text
     */
    public void setTitleRight1Text(String text) {
        flyTitleRight1.setVisibility(View.VISIBLE);
        tvRight1.setText(text);
    }

    /**
     * 设置右边第一个标题颜色
     *
     * @param color
     */
    public void setTitleRight1TextColor(int color) {
        flyTitleRight1.setVisibility(View.VISIBLE);
        tvRight1.setTextColor(color);
    }

    /**
     * 设置右边第二个标题文字
     *
     * @param text
     */
    public void setTitleRight2Text(String text) {
        flyTitleRight2.setVisibility(View.VISIBLE);
        tvRight2.setText(text);
    }

    /**
     * 设置右边第三个标题文字
     *
     * @param text
     */
    public void setTitleRight3Text(String text) {
        flyTitleRight3.setVisibility(View.VISIBLE);
        tvRight3.setText(text);
    }

    /**
     * 设置右边第二个标题文字颜色
     *
     * @param color
     */
    public void setTitleRight2TextColor(int color) {
        tvRight2.setTextColor(color);
    }

    /**
     * 设置右边第三个标题文字颜色
     *
     * @param color
     */
    public void setTitleRight3TextColor(int color) {
        tvRight3.setTextColor(color);
    }

    /**
     * 设置右边第一个标题文字大小
     *
     * @param size
     */
    public void setTitleRight1TextSize(int size) {
        tvRight1.setTextSize(size);
    }

    /**
     * 设置右边第二个标题文字大小
     *
     * @param size
     */
    public void setTitleRight2TextSize(int size) {
        tvRight2.setTextSize(size);
    }

    /**
     * 设置右边第三个标题文字大小
     *
     * @param size
     */
    public void setTitleRight3TextSize(int size) {
        tvRight3.setTextSize(size);
    }

    /**
     * 设置左边标题背景图片
     *
     * @param resId
     */
    public void setTitleLeftBackground(int resId) {
        flyTitleLeft.setVisibility(View.VISIBLE);
        flyTitleLeft.setBackgroundResource(resId);
    }

    /**
     * 设置右边第一个标题背景图片
     *
     * @param resId
     */
    public void setTitleRight1Background(int resId) {
        flyTitleRight1.setVisibility(View.VISIBLE);
        flyTitleRight1.setBackgroundResource(resId);
    }

    /**
     * 设置右边第二个标题背景图片
     *
     * @param resId
     */
    public void setTitleRight2Background(int resId) {
        flyTitleRight2.setVisibility(View.VISIBLE);
        flyTitleRight2.setBackgroundResource(resId);
    }

    /**
     * 设置右边第三个标题背景颜色
     *
     * @param resId
     */
    public void setTitleRight3Background(int resId) {
        flyTitleRight3.setVisibility(View.VISIBLE);
        flyTitleRight3.setBackgroundResource(resId);
    }

    /**
     * 设置右边第二个标题图片
     *
     * @param resId
     */
    public void setTitleRight2Image(int resId) {
        imgRight2.setImageResource(resId);
        flyTitleRight2.setVisibility(View.VISIBLE);
    }

    /**
     * 设置右边第三个标题图片
     *
     * @param resId
     */
    public void setTitleRight3Image(int resId) {
        imgRight3.setImageResource(resId);
        flyTitleRight3.setVisibility(View.VISIBLE);
    }

    /**
     * 添加中部类视图
     *
     * @param resId
     */
    public void addMidView(int resId) {
        midView = LayoutInflater.from(this).inflate(resId, null);
        layoutMid.removeAllViews();
        layoutMid.addView(midView, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
    }

    /**
     * 添加中部类视图
     *
     * @param view
     */
    public void addMidView(View view) {
        this.midView = view;
        layoutMid.removeAllViews();
        layoutMid.addView(midView, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == com.sponia.foundationmoudle.R.id.fly_title_left) {
            onBackPressed();
        }
    }

    /**
     * 展示"取消","确定"两个按钮的dialog
     *
     * @param dialogType                    dialog类型: SweetAlertDialog.NORMAL_TYPE, SweetAlertDialog.WARNING_TYPE
     * @param title                         dialog标题
     * @param contentMsg                    提示内容
     * @param cancelListener,传null默认dismiss dialog
     * @param confirmListener
     */
    public void show2BtnDialog(int dialogType, String title, String contentMsg, SweetAlertDialog.OnSweetClickListener cancelListener, SweetAlertDialog.OnSweetClickListener confirmListener) {

        final SweetAlertDialog dialog = new SweetAlertDialog(this, dialogType);
        if (!TextUtils.isEmpty(title)) {
            dialog.setTitleText(title);
        }
        if (!TextUtils.isEmpty(contentMsg)) {
            dialog.setContentText(contentMsg);
        }

        if (cancelListener == null) {
            cancelListener = new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    dialog.dismiss();
                }
            };
        }
        dialog.setCancelText("取消")
                .setConfirmText("确定")
                .showCancelButton(true)
                .setCancelClickListener(cancelListener)
                .setConfirmClickListener(confirmListener).show();

    }

    /**
     * 显示一个button dialog
     *
     * @param dialogType
     * @param title
     * @param contentMsg
     * @param confirmListener
     */
    public void show1BtnDialog(int dialogType, String title, String contentMsg, SweetAlertDialog.OnSweetClickListener confirmListener) {

        final SweetAlertDialog dialog = new SweetAlertDialog(this, dialogType);
        if (!TextUtils.isEmpty(title)) {
            dialog.setTitleText(title);
        }
        if (!TextUtils.isEmpty(contentMsg)) {
            dialog.setContentText(contentMsg);
        }

        dialog.setConfirmText("确定")
                .showCancelButton(false)
                .setConfirmClickListener(confirmListener).show();

    }

    /**
     * 展示加载中...
     */
    public void showLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissLoading();
                if (!isFinishing()) {
                    getLoadingDialog().show();
                }
            }
        });
    }

    /**
     * loading dialog
     * @return
     */
    private SweetAlertDialog getLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("加载中,请稍候...");
        }
        return loadingDialog;
    }

    /**
     * 取消加载中dialog
     */
    public void dismissLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onHttpError(NetMessage netMsg, final int errorCode) {
        LogUtil.defaultLog("onHttpError nerMsg " + netMsg + "; errorCode: " + errorCode);
        dismissLoading();
        String errorStr;
        switch (errorCode) {
            case IHttpListener.ERROR_SOCKETTIMEOUTEXCEPTION:
                errorStr = getErrorClientTimeoutStr();
                break;
            case IHttpListener.ERROR_NONETWORKACTIVITYEXCEPTION:
                errorStr = getErrorNoNetWorkStr();
                break;
            case IHttpListener.ERROR_IOEXCEPTION:
                errorStr = getErrorcodeStr();
                break;
            case IHttpListener.ERROR_HTTPSERVICEERROREXCEPTION:
                errorStr = getErrorcodeStr();
                break;
            case IHttpListener.ERROR_SSLVERIFYEXCEPTION:
                errorStr = getErrorSSLStr();
                break;
            //服务端定义错误码
            case IHttpListener.ERROR_VERIFY_EXCEPTION:
                errorStr = getString(com.sponia.foundationmoudle.R.string.error_verify_exception);
//                StatsAppConfig.setToken("");
//                CommUtils.cleanToken();
                break;
            case IHttpListener.ERROR_JSON_PARSE_EXCEPTION:
                errorStr = getString(com.sponia.foundationmoudle.R.string.error_json_parse_exception);
                break;
            case IHttpListener.ERROR_REQUEST_VERIFY_EXCEPTION:
                errorStr = getString(com.sponia.foundationmoudle.R.string.error_request_verify_exception);
                break;
            case IHttpListener.ERROR_PERMISSION_EXCEPTION:
                errorStr = getString(com.sponia.foundationmoudle.R.string.error_permission_exception);
                break;
            case IHttpListener.ERROR_TOKEN_EXCEPTION:
                errorStr = getString(com.sponia.foundationmoudle.R.string.error_token_exception);
                break;
            case IHttpListener.ERROR_NO_ACCOUNT_EXCEPTION:
                errorStr = getString(com.sponia.foundationmoudle.R.string.error_no_account_exception);
                break;
            case IHttpListener.ERROR_PHONE_PWD_EXCEPTION:
                errorStr = getString(com.sponia.foundationmoudle.R.string.error_phone_pwd_exception);
                break;
            case IHttpListener.ERROR_PHONE_USED_EXCEPTION:
                errorStr = getString(com.sponia.foundationmoudle.R.string.error_phone_used_exception);
                break;
            case IHttpListener.ERROR_VERIFY_CODE_EXCEPTION:
                errorStr = getString(com.sponia.foundationmoudle.R.string.error_verify_code_exception);
                break;
            case IHttpListener.ERROR_INVITATION_CODE_EXCEPTION:
                errorStr = getString(com.sponia.foundationmoudle.R.string.error_invitation_code_exception);
                break;
            case IHttpListener.ERROR_NO_RESOUCE_EXCEPTION:
                errorStr = getString(com.sponia.foundationmoudle.R.string.error_no_resouce_exception);
                break;
            case IHttpListener.ERROR_ACCOUNT_ACTIVATION_EXCEPTION:
                errorStr = getString(com.sponia.foundationmoudle.R.string.error_account_activation_exception);
                break;
            case IHttpListener.ERROR_SHIRT_NO_EXCEPTION:
                errorStr = getString(com.sponia.foundationmoudle.R.string.error_shirt_no_exception);
                break;
            case IHttpListener.ERROR_EMAIL_USED_EXCEPTION:
                errorStr = getString(com.sponia.foundationmoudle.R.string.error_email_used_exception);
                break;
            case IHttpListener.ERROR_NO_UPDATE_EXCEPTION:
                errorStr = getString(com.sponia.foundationmoudle.R.string.error_no_update_exception);
                break;
            case IHttpListener.ERROR_NO_JOIN_EXCEPTION:
                errorStr = getString(com.sponia.foundationmoudle.R.string.error_no_join_exception);
                break;
            //此部分返回码应该和http返回码对应,或者401统一是否返回bean
            case IHttpListener.ERROR_RESOURCE_NOT_FOUND_EXCEPTION:
                errorStr = getString(com.sponia.foundationmoudle.R.string.error_no_resouce_exception);
                break;
            case IHttpListener.ERROR_SERVER_EXCEPTION:
                errorStr = getString(com.sponia.foundationmoudle.R.string.error_server_exception);
                break;
            case IHttpListener.ERROR_SERVER_ONE_EXCEPTION:
                errorStr = getString(com.sponia.foundationmoudle.R.string.error_server_exception);
                break;
            case IHttpListener.ERROR_SERVER_TWO_EXCEPTION:
                errorStr = getString(com.sponia.foundationmoudle.R.string.error_server_exception);
                break;
            case IHttpListener.ERROR_SERVER_THREE_EXCEPTION:
                errorStr = getString(com.sponia.foundationmoudle.R.string.error_server_exception);
                break;
            case IHttpListener.ERROR_NO_PERMISSION_EXCEPTION:
                errorStr = getString(com.sponia.foundationmoudle.R.string.error_permission_exception);
                break;
            case IHttpListener.ERROR_PARAMS_EXCEPTION:
                errorStr = getString(com.sponia.foundationmoudle.R.string.error_params_exception);
                break;
            default:
                errorStr = getErrorUnKnown();
                break;
        }
        final String errorMessage = errorStr;
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                //此处应dismiss Dialog
                SponiaToastUtil.showShortToast(errorMessage);
            }
        });
    }

    public String getErrorNoNetWorkStr() {
        return getResources().getString(com.sponia.foundationmoudle.R.string.network_error);
    }

    public String getErrorClientTimeoutStr() {
        return getResources().getString(com.sponia.foundationmoudle.R.string.client_timeout);
    }

    public String getErrorcodeStr() {
        return getResources().getString(com.sponia.foundationmoudle.R.string.errorCode);
    }

    public String getErrorSSLStr() {
        return getResources().getString(com.sponia.foundationmoudle.R.string.sslVerifyFail);
    }

    public String getErrorUnKnown() {
        return getResources().getString(com.sponia.foundationmoudle.R.string.unknown);
    }

    @Override
    public void onHttpSuccess(NetMessage netMsg, String responceData) {
        SponiaBaseBean baseBean = JSON.parseObject(responceData, SponiaBaseBean.class);
        if (baseBean != null && baseBean.error_code != 0) {
            onHttpError(netMsg, baseBean.error_code);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoading();
    }
}
