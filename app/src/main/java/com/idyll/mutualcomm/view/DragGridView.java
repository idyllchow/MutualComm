package com.idyll.mutualcomm.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.idyll.mutualcomm.R;
import com.idyll.mutualcomm.adapter.DragGridBaseAdapter;
import com.sponia.foundationmoudle.utils.LogUtil;

/**
 * 可拖拽的GridView, 添加Item的交换效果
 *
 * @author leejay
 */
public class DragGridView extends GridView {
    private static final String TAG = "DragGridView";
    /**
     * DragGridView的item长按响应的时间， 默认是300毫秒，也可以自行设置
     */
    private long dragResponseMS = 300;

    /**
     * 是否可以拖拽，默认不可以
     */
    private boolean isDrag = false;

    /**
     * 是否在快速滚动
     */
    private boolean isFling = false;
    private int mDownX;
    private int mDownY;
    private int moveX;
    private int moveY;
    /**
     * 正在拖拽的position
     */
    private int mDragPosition;

    /**
     * 刚开始拖拽的item对应的View
     */
    private View mStartDragItemView = null;

    /**
     * 用于拖拽的镜像，这里直接用一个ImageView
     */
    private ImageView mDragImageView;

    /**
     * 震动器
     */
    private Vibrator mVibrator;

    private WindowManager mWindowManager;
    /**
     * item镜像的布局参数
     */
    private WindowManager.LayoutParams mWindowLayoutParams;

    /**
     * 我们拖拽的item对应的Bitmap
     */
    private Bitmap mDragBitmap;

    /**
     * 按下的点到所在item的上边缘的距离
     */
    private int mPoint2ItemTop;

    /**
     * 按下的点到所在item的左边缘的距离
     */
    private int mPoint2ItemLeft;

    /**
     * DragGridView距离屏幕顶部的偏移量
     */
    private int mOffset2Top;

    /**
     * DragGridView距离屏幕左边的偏移量
     */
    private int mOffset2Left;

    /**
     * 状态栏的高度
     */
    private int mStatusHeight;

    /**
     * DragGridView自动向下滚动的边界值
     */
    private int mDownScrollBorder;

    /**
     * DragGridView自动向上滚动的边界值
     */
    private int mUpScrollBorder;

    /**
     * DragGridView自动滚动的速度
     */
    private static final int speed = 20;
    /**
     * item的移动动画是否结束
     */
    private boolean mAnimationEnd = true;

    private DragGridBaseAdapter mDragAdapter;
    /**
     * GridView的列数
     */
    private int mNumColumns;
    /**
     * 当GridView的numColumns设置为AUTO_FIT，我们需要计算GirdView具体的列数
     */
    private int mColumnWidth;
    /**
     * GridView是否设置了numColumns为具体的数字
     */
    private boolean mNumColumnsSet;
    private int mHorizontalSpacing;
    private int mVerticalSpacing;

    public interface OnSwapItemCallback {
        /**
         * 用于两个GridView之间交换item
         *
         * @param moveX
         * @param moveY
         * @param oldPos
         * @return newPos值，若返回值为-1,则表示未交换
         */
        int onSwapItem(int moveX, int moveY, int oldPos);

        /**
         * @param newPos  另一个GridView中的位置
         * @param oldPos  自己GridView中的位置
         * @param oldView 需要做动画的View
         */
        void onSwapAnimate(int newPos, int oldPos, View oldView);

        boolean checkHasPosition();

        void onItemClick(int clickedPos, View oldView);
    }

    private OnSwapItemCallback mOnSwapItemCallback;

    public void setOnSwapItemCallback(OnSwapItemCallback callback) {
        this.mOnSwapItemCallback = callback;
    }

    public DragGridView(Context context) {
        this(context, null);
    }

    public DragGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mStatusHeight = getStatusHeight(context); //获取状态栏的高度
        if (!mNumColumnsSet) {
            mNumColumns = AUTO_FIT;
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        isFling = true;
    }

    /**
     * 删除item的动画效果
     *
     * @param position
     */
    public void removeItemAnimation(final int position) {
        mDragAdapter.removeItem(position);
        final ViewTreeObserver observer = getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                animateReorder(position, getLastVisiblePosition() + 1);
                return true;
            }
        });
    }

    private Handler mHandler = new Handler();

    //用来处理是否为长按的Runnable
    private Runnable mLongClickRunnable = new Runnable() {

        @Override
        public void run() {
            isDrag = true; //设置可以拖拽
            mVibrator.vibrate(50); //震动一下
            mDragAdapter.setHideItem(mDragPosition);
            //开启mDragItemView绘图缓存
            if (mStartDragItemView instanceof ViewGroup) {
                if (((ViewGroup) mStartDragItemView).getChildCount() > 0) {
                    ((ViewGroup) mStartDragItemView).getChildAt(0).setBackgroundResource(R.drawable.gray_circle_bg);
                    LogUtil.defaultLog("child count --------> " + ((ViewGroup) mStartDragItemView).getChildCount());
                }
            }
            mStartDragItemView.setDrawingCacheEnabled(true);
            //获取mDragItemView在缓存中的Bitmap对象
            mDragBitmap = Bitmap.createBitmap(mStartDragItemView.getDrawingCache());
            //这一步很关键，释放绘图缓存，避免出现重复的镜像
            mStartDragItemView.destroyDrawingCache();

            if (mStartDragItemView instanceof ViewGroup) {
                if (((ViewGroup) mStartDragItemView).getChildCount() > 0) {
                    LogUtil.defaultLog("child count --------> " + ((ViewGroup) mStartDragItemView).getChildCount());
                            ((ViewGroup) mStartDragItemView).getChildAt(0).setBackgroundResource(0);
                }
            }
            mStartDragItemView.setVisibility(View.INVISIBLE);//隐藏该item

            //根据我们按下的点显示item镜像
            createDragImage(mDragBitmap, mDownX, mDownY);
        }
    };

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);

        if (adapter instanceof DragGridBaseAdapter) {
            mDragAdapter = (DragGridBaseAdapter) adapter;
        } else {
            throw new IllegalStateException("the adapter must be implements DragGridAdapter");
        }
    }

    /**
     * 获取列数
     */
    @Override
    public void setNumColumns(int numColumns) {
        super.setNumColumns(numColumns);
        mNumColumnsSet = true;
        this.mNumColumns = numColumns;
    }

    /**
     * 获取设置的列宽
     */
    @Override
    public void setColumnWidth(int columnWidth) {
        super.setColumnWidth(columnWidth);
        mColumnWidth = columnWidth;
    }

    /**
     * 获取水平方向的间隙
     */
    @Override
    public void setHorizontalSpacing(int horizontalSpacing) {
        super.setHorizontalSpacing(horizontalSpacing);
        this.mHorizontalSpacing = horizontalSpacing;
    }


    /**
     * 获取竖直方向的间隙
     */
    @Override
    public void setVerticalSpacing(int verticalSpacing) {
        super.setVerticalSpacing(verticalSpacing);
        this.mVerticalSpacing = verticalSpacing;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    /**
     * 设置响应拖拽的毫秒数，默认是200毫秒
     *
     * @param dragResponseMS
     */
    public void setDragResponseMS(long dragResponseMS) {
        this.dragResponseMS = dragResponseMS;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isFling = false;
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();

                //根据按下的X,Y坐标获取所点击item的position
                mDragPosition = pointToPosition(mDownX, mDownY);

                int id = (int) mDragAdapter.getItemId(mDragPosition);

                if (mDragPosition == AdapterView.INVALID_POSITION || -1 == id) { //点击非球员区域 静止滑动
                    return super.dispatchTouchEvent(ev);
                }

                //使用Handler延迟dragResponseMS执行mLongClickRunnable
                mHandler.postDelayed(mLongClickRunnable, dragResponseMS);

                //根据position获取该item所对应的View
                mStartDragItemView = getChildAt(mDragPosition - getFirstVisiblePosition());

                //下面这几个距离大家可以参考我的博客上面的图来理解下
                mPoint2ItemTop = mDownY - mStartDragItemView.getTop();
                mPoint2ItemLeft = mDownX - mStartDragItemView.getLeft();

                mOffset2Top = (int) (ev.getRawY() - mDownY);
                mOffset2Left = (int) (ev.getRawX() - mDownX);

                //获取DragGridView自动向上滚动的偏移量，小于这个值，DragGridView向下滚动
                mDownScrollBorder = getHeight() / 6;
                //获取DragGridView自动向下滚动的偏移量，大于这个值，DragGridView向上滚动
                mUpScrollBorder = getHeight() * 5 / 6;
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();

                //如果我们在按下的item上面移动，只要不超过item的边界我们就不移除mRunnable
                if (!isTouchInItem(mStartDragItemView, moveX, moveY) && !isDrag) {
                    LogUtil.e(TAG, "MOVE remove mRunnable.");
                    mHandler.removeCallbacks(mLongClickRunnable);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                LogUtil.e(TAG, "UP time:" + System.currentTimeMillis() + ", isFling:" + isFling);

                mHandler.removeCallbacks(mLongClickRunnable);
                mHandler.removeCallbacks(mScrollRunnable);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 是否点击在GridView的item上面
     */
    private boolean isTouchInItem(View dragView, int x, int y) {
        if (dragView == null) {
            return false;
        }
        int leftOffset = dragView.getLeft();
        int topOffset = dragView.getTop();
        if (x < leftOffset || x > leftOffset + dragView.getWidth()) {
            return false;
        }

        if (y < topOffset || y > topOffset + dragView.getHeight()) {
            return false;
        }

        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (isDrag && mDragImageView != null) {
            moveX = (int) ev.getX();
            moveY = (int) ev.getY();
            switch (action) {
                case MotionEvent.ACTION_MOVE:
                    //拖动item
                    onDragItem(moveX, moveY);
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    onSwapItem(moveX, moveY);
                    onStopDrag();
                    isDrag = false;
                    break;
            }
            return true;
        } else if (action == MotionEvent.ACTION_UP && !isFling && null != mOnSwapItemCallback) {
            if (mDragPosition != AdapterView.INVALID_POSITION &&
                    (int) mDragAdapter.getItemId(mDragPosition) != -1 &&
                    mDragPosition == pointToPosition((int) ev.getX(), (int) ev.getY())) {
                if (mOnSwapItemCallback.checkHasPosition()) {
                    mOnSwapItemCallback.onItemClick(mDragPosition, getChildAt(mDragPosition - getFirstVisiblePosition()));
                }
            }
        } else { //禁止上下滑动
            return true;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 停止拖拽我们将之前隐藏的item显示出来，并将镜像移除
     */
    private void onStopDrag() {
        View view = getChildAt(mDragPosition - getFirstVisiblePosition());
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
        mDragAdapter.setHideItem(-1);
        removeDragImage();
    }

    /**
     * 创建拖动的镜像
     *
     * @param bitmap
     * @param downX  按下的点相对父控件的X坐标
     * @param downY  按下的点相对父控件的X坐标
     */
    private void createDragImage(Bitmap bitmap, int downX, int downY) {
        mWindowLayoutParams = new WindowManager.LayoutParams();
        mWindowLayoutParams.format = PixelFormat.TRANSLUCENT; //图片之外的其他地方透明
        mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        //点击时生成image初始位置
        mWindowLayoutParams.x = downX - mPoint2ItemLeft + mOffset2Left;
        mWindowLayoutParams.y = downY - mPoint2ItemTop + mOffset2Top - mStatusHeight;
//        mWindowLayoutParams.alpha = 0.55f; //透明度
        mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT - 80;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT - 80;
        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        mDragImageView = new ImageView(getContext());
        mDragImageView.setImageBitmap(bitmap);

        mWindowManager.addView(mDragImageView, mWindowLayoutParams);
    }

    /**
     * 从界面上面移动拖动镜像
     */
    private void removeDragImage() {
        if (mDragImageView != null) {
            mWindowManager.removeView(mDragImageView);
            mDragImageView = null;
        }
    }

    /**
     * 拖动item，在里面实现了item镜像的位置更新，以及GridView的自行滚动
     *
     * @param moveX
     * @param moveY
     */
    private void onDragItem(int moveX, int moveY) {
        mWindowLayoutParams.x = moveX - mPoint2ItemLeft + mOffset2Left;
        mWindowLayoutParams.y = moveY - mPoint2ItemTop + mOffset2Top - mStatusHeight;
        mWindowManager.updateViewLayout(mDragImageView, mWindowLayoutParams); //更新镜像的位置

        //GridView自动滚动
        mHandler.post(mScrollRunnable);
    }


    /**
     * 当moveY的值大于向上滚动的边界值，触发GridView自动向上滚动
     * 当moveY的值小于向下滚动的边界值，触发GridView自动向下滚动
     * 否则不进行滚动
     */
    private Runnable mScrollRunnable = new Runnable() {

        @Override
        public void run() {
            int scrollY;
            if (getFirstVisiblePosition() == 0 || getLastVisiblePosition() == getCount() - 1) {
                mHandler.removeCallbacks(mScrollRunnable);
            }
            if (moveX > 0) {
                if (moveY > mUpScrollBorder) {
                    scrollY = speed;
                    mHandler.postDelayed(mScrollRunnable, 25);
                } else if (moveY < mDownScrollBorder) {
                    scrollY = -speed;
                    mHandler.postDelayed(mScrollRunnable, 25);
                } else {
                    scrollY = 0;
                    mHandler.removeCallbacks(mScrollRunnable);
                }

                smoothScrollBy(scrollY, 10);
            }
        }
    };

    /**
     * 交换item,并且控制item之间的显示与隐藏效果
     *
     * @param moveX
     * @param moveY
     */
    private void onSwapItem(final int moveX, final int moveY) {
        //获取我们手指移动到的那个item的position
        final int tempPosition = pointToPosition(moveX, moveY);
        //假如tempPosition 改变了并且tempPosition不等于-1,则进行交换
        if (tempPosition != mDragPosition && tempPosition != AdapterView.INVALID_POSITION && mAnimationEnd) {

            //交换item
            mDragAdapter.reorderItems(mDragPosition, tempPosition);
            //shibo
//            mDragAdapter.notifyDataSetChanged(mDragPosition, tempPosition, true);
            //等notifyDataSetChanged()执行完成，界面有变动时再作动画，否则动画作用的View已经被销毁，动画不起效果
            final ViewTreeObserver observer = getViewTreeObserver();
            observer.addOnPreDrawListener(new OnPreDrawListener() {

                @Override
                public boolean onPreDraw() {
                    observer.removeOnPreDrawListener(this);
                    animateReorder(mDragPosition, tempPosition);
                    return true;
                }
            });
        } else {
            if (null != mOnSwapItemCallback) {
                final int newPos = mOnSwapItemCallback.onSwapItem(moveX, moveY, mDragPosition);
                if (newPos != AdapterView.INVALID_POSITION) {
                    //等待等notifyDataSetChanged()执行完毕，执行下面回调
                    final ViewTreeObserver observer = getViewTreeObserver();
                    observer.addOnPreDrawListener(new OnPreDrawListener() {

                        @Override
                        public boolean onPreDraw() {
                            observer.removeOnPreDrawListener(this);
                            mOnSwapItemCallback.onSwapAnimate(newPos, mDragPosition, getChildAt(mDragPosition - getFirstVisiblePosition()));
                            return true;
                        }
                    });
                }
            }
        }
    }

    /**
     * item的交换动画效果
     */
    private void animateReorder(final int oldPosition, final int newPosition) {
        View view = getChildAt(oldPosition - getFirstVisiblePosition());
        if (null != view) {
            float transX = (newPosition % mNumColumns - oldPosition % mNumColumns) * (view.getWidth() + mHorizontalSpacing);
            float transY = (newPosition / mNumColumns - oldPosition / mNumColumns) * (view.getHeight() + mVerticalSpacing);

            AnimatorSet resultSet = createTranslationAnimations(view, transX, 0, transY, 0);
            resultSet.setDuration(300);
            resultSet.setInterpolator(new AccelerateDecelerateInterpolator());
            resultSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mAnimationEnd = false;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mAnimationEnd = true;
                }
            });
            resultSet.start();
        }
    }

    /**
     * 创建移动动画
     */
    private AnimatorSet createTranslationAnimations(View view, float startX,
                                                    float endX, float startY, float endY) {
        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "translationX",
                startX, endX);
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "translationY",
                startY, endY);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(animX, animY);
        return animSetXY;
    }

    public void setStatusHeight(int statusHeight) {
        mStatusHeight = statusHeight;
    }

    /**
     * 获取状态栏的高度
     */
    private static int getStatusHeight(Context context) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

}
