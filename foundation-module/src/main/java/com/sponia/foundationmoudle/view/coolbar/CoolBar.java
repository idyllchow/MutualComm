package com.sponia.foundationmoudle.view.coolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.sponia.foundationmoudle.R;
import com.sponia.foundationmoudle.utils.DensityUtil;

/**
 * @author shibo
 * @packageName com.sponia.foundationmoudle.view.coolbar
 * @description 标题栏view,同BaseActivity中ActionBar一样,后面将根据产品设计固定使用其中一个
 * @date 16/1/12
 */
public class CoolBar extends FrameLayout implements View.OnClickListener {
    public static final int HOME_MODE = 0;
    public static final int SEARCH_MODE = 1;
    public static final int BACK_MODE = 2;
    public static final int NEWS = 0;
    public static final int BACK = 1;
    public static final int MENU = 2;
    public static final int SELECT_TICK = 3;
    public static final int SHARE = 4;

    private boolean showBottomLine;
    private boolean showMenu;
    private boolean showNews;
    private boolean showSelectTick;
    private boolean showShareView;
    private int mode;

    private TextView newsNumber;
    private ImageView newsLogo;
    private TextView coolTitle;
    private View bottomLine;
    private PopupWindow popupWindow;
    private String title;

    private OnCoolBarItemClickedListener listener;
    private TextView menuTitle;
    private View menuGroup;
    private MaterialEditText searchMaterialEditText;
    private ImageView coolMenu;

    public CoolBar(Context context) {
        super(context);
    }

    public CoolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CoolBar);
        mode = typedArray.getInteger(R.styleable.CoolBar_coolMode, BACK_MODE);
        showBottomLine = typedArray.getBoolean(R.styleable.CoolBar_showBottomLine, true);
        showSelectTick = typedArray.getBoolean(R.styleable.CoolBar_showSelectTick, false);
        showNews = typedArray.getBoolean(R.styleable.CoolBar_showNews, true);
        showMenu = typedArray.getBoolean(R.styleable.CoolBar_showMenu, true);
        showShareView = typedArray.getBoolean(R.styleable.CoolBar_showShareView, false);
        title = typedArray.getString(R.styleable.CoolBar_coolTitle);
        typedArray.recycle();
        initView();
    }

    public CoolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView() {
        switch (mode) {
            case HOME_MODE:
                LayoutInflater.from(getContext()).inflate(R.layout.item_coolbar_home, this, true);
                newsLogo = (ImageView) findViewById(R.id.iv_news);
                newsNumber = (TextView) findViewById(R.id.tv_news_number);
                coolTitle = (TextView) findViewById(R.id.tv_title);
                coolTitle.setText(title);
                findViewById(R.id.v_news).setOnClickListener(this);
                newsLogo.setOnClickListener(this);
                newsNumber.setOnClickListener(this);
                if (showNews) {
                    newsLogo.setVisibility(VISIBLE);
                    newsNumber.setVisibility(VISIBLE);
                } else {
                    newsLogo.setVisibility(GONE);
                    newsNumber.setVisibility(GONE);
                }
                break;
            case BACK_MODE:
                LayoutInflater.from(getContext()).inflate(R.layout.item_coolbar_back, this, true);
                coolMenu = (ImageView) findViewById(R.id.iv_menu);
                menuGroup = LayoutInflater.from(getContext()).inflate(R.layout.item_popup_window, null);
                View selectTick = findViewById(R.id.iv_select_tick);
                View shareView = findViewById(R.id.iv_share);
                findViewById(R.id.v_back).setOnClickListener(this);
                findViewById(R.id.iv_back).setOnClickListener(this);
                selectTick.setOnClickListener(this);
                shareView.setOnClickListener(this);
                coolTitle = (TextView) findViewById(R.id.tv_title);
                coolTitle.setText(title);

                if (showMenu) {
                    menuTitle = (TextView) menuGroup.findViewById(R.id.tv_menu);
                    coolMenu.setVisibility(VISIBLE);
                } else {
                    coolMenu.setVisibility(GONE);
                }
                if (showSelectTick) {
                    selectTick.setVisibility(VISIBLE);
                    coolMenu.setVisibility(GONE);
                } else {
                    selectTick.setVisibility(GONE);
                }
                if (showShareView) {
                    shareView.setVisibility(VISIBLE);
                } else {
                    shareView.setVisibility(GONE);
                }
                coolMenu.setOnClickListener(this);
                break;
            case SEARCH_MODE:
                LayoutInflater.from(getContext()).inflate(R.layout.item_coolbar_search, this, true);
                findViewById(R.id.iv_back).setOnClickListener(this);
                findViewById(R.id.v_back).setOnClickListener(this);
                searchMaterialEditText = (MaterialEditText) findViewById(R.id.met_content);
                break;
            default:
                break;
        }

        bottomLine = findViewById(R.id.v_bottom_line);
        showLine(showBottomLine);
    }

    public void setNewsNumberColor(int color) {
        if (null != newsNumber) {
            newsNumber.setTextColor(color);
        }
    }

    public void setNewsCount(int count) {
        if (null != newsNumber) {
            if (count < 10) {
                newsNumber.setText(String.valueOf(count));
            } else {
                newsNumber.setText("...");
            }
        }
    }

    public void showNewsNumber(boolean showNews) {
        if (null != newsNumber) {
            newsNumber.setVisibility(showNews ? VISIBLE : GONE);
        }

    }

    public void setNewsNumberSize(float size) {
        if (null != newsNumber) {
            newsNumber.setTextSize(size);
        }
    }

    public void setNewsNumberBackground(int resid) {
        if (null != newsNumber) {
            newsNumber.setBackgroundResource(resid);
        }
    }

    public void setNewsLogo(int resid) {
        if (null != newsLogo) {
            newsLogo.setImageDrawable(getResources().getDrawable(resid));
        }
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
        if (coolMenu != null) {
            coolMenu.setVisibility(showMenu ? VISIBLE : GONE);
        }
    }

    public void setShowBottomLine(boolean showBottomLine) {
        showLine(showBottomLine);
    }

    public void setOnCoolBarClickedListener(OnCoolBarItemClickedListener listener) {
        this.listener = listener;
    }

    public void setTitle(String title) {
        if (null != title) {
            coolTitle.setText(title);
        }
    }

    public void setTitle(CharSequence title) {
        if (null != title) {
            coolTitle.setText(title);
        }
    }

    public void setMenuTitle(String title) {
        if (null != menuTitle) {
            menuTitle.setText(title);
        }
    }

    private void showLine(boolean showBottomLine) {
        if (showBottomLine) {
            bottomLine.setVisibility(VISIBLE);
        } else {
            bottomLine.setVisibility(GONE);
        }
    }

    public MaterialEditText getSearchMaterialEditText() {
        return searchMaterialEditText;
    }

    public void setMaterialEditTextHint(String hint) {
        if (searchMaterialEditText != null) {
            searchMaterialEditText.setHint(hint);
        }
    }

    @Override
    public void onClick(View v) {
        if (null != listener) {
            if (v.getId() == R.id.iv_news) {
                listener.onCoolBarItemClicked(NEWS);
            } else if (v.getId() == R.id.v_news) {
                listener.onCoolBarItemClicked(NEWS);
            } else if (v.getId() == R.id.tv_news_number) {
                listener.onCoolBarItemClicked(NEWS);
            } else if (v.getId() == R.id.iv_back) {
                listener.onCoolBarItemClicked(BACK);
            } else if (v.getId() == R.id.v_back) {
                listener.onCoolBarItemClicked(BACK);
            } else if (v.getId() == R.id.iv_select_tick) {
                listener.onCoolBarItemClicked(SELECT_TICK);
            } else if (v.getId() == R.id.iv_share) {
                listener.onCoolBarItemClicked(SHARE);
            } else if (v.getId() == R.id.iv_menu) {
                if (popupWindow == null) {
                    popupWindow = new PopupWindow(getContext());
                    if (menuTitle != null) {
                        menuTitle.setOnClickListener(this);
                    }
                    popupWindow.setContentView(menuGroup);
                    popupWindow.setWidth(DensityUtil.dip2px(200));
                    popupWindow.setHeight(DensityUtil.dip2px(70));
                    popupWindow.setFocusable(true);
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
                }
                popupWindow.showAtLocation(this, Gravity.TOP | Gravity.END, DensityUtil.dip2px(2), DensityUtil.dip2px(25));
            } else if (v.getId() == R.id.tv_menu) {
                listener.onCoolBarItemClicked(MENU);
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        }
    }

    public interface OnCoolBarItemClickedListener {
        void onCoolBarItemClicked(int view);
    }
}
