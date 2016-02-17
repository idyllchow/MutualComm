package com.idyll.mutualcomm.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.idyll.mutualcomm.adapter.StatsPlayerAdapter;
import com.idyll.mutualcomm.comm.MCConstants;
import com.sponia.foundationmoudle.utils.LogUtil;

import java.util.ArrayList;

/**
 * @author shibo
 * @packageName com.idyll.mutualcomm.view
 * @description
 * @date 16/2/14
 */
public class LineGridView extends GridView {
    /**
     * 是否在快速滚动
     */
    private boolean isFling = false;
    private int mDownX;
    private int mDownY;
    /**
     * 正在拖拽的position
     */
    private int mDragPosition;

    /**
     * 刚开始拖拽的item对应的View
     */
    private View mStartDragItemView = null;
    /**
     * 震动器
     */
    private Vibrator mVibrator;
    private WindowManager mWindowManager;

    private StatsPlayerAdapter mDragAdapter;
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
    /**
     * 起始点号码
     */
    private String startNum;
    /**
     * 停止点号码
     */
    private String stopNum;
    Paint paint = new Paint();
    ArrayList<Line> lines = new ArrayList<>();
    boolean firstPress = true;

    public LineGridView(Context context) {
        this(context, null);
    }

    public LineGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (!mNumColumnsSet) {
            mNumColumns = AUTO_FIT;
        }
        initPaint();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        isFling = true;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);

        if (adapter instanceof StatsPlayerAdapter) {
            mDragAdapter = (StatsPlayerAdapter) adapter;
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
    }

    /**
     * 若列数设置为AUTO_FIT，我们在这里面计算具体的列数
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mNumColumns == AUTO_FIT) {
            int numFittedColumns;
            if (mColumnWidth > 0) {
                int gridWidth = Math.max(MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
                        - getPaddingRight(), 0);
                numFittedColumns = gridWidth / mColumnWidth;
                if (numFittedColumns > 0) {
                    while (numFittedColumns != 1) {
                        if (numFittedColumns * mColumnWidth + (numFittedColumns - 1)
                                * mHorizontalSpacing > gridWidth) {
                            numFittedColumns--;
                        } else {
                            break;
                        }
                    }
                } else {
                    numFittedColumns = 1;
                }
            } else {
                numFittedColumns = 2;
            }
            mNumColumns = numFittedColumns;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
                //根据position获取该item所对应的View
                mStartDragItemView = getChildAt(mDragPosition - getFirstVisiblePosition());
                break;

        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP && !isFling) {
            if (mDragPosition != AdapterView.INVALID_POSITION &&
                    (int) mDragAdapter.getItemId(mDragPosition) != -1 &&
                    mDragPosition == pointToPosition((int) ev.getX(), (int) ev.getY())) {
            }
        } else { //禁止上下滑动
            if (action == MotionEvent.ACTION_DOWN) {
                lines.clear();
                int startPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
                startNum = mDragAdapter.getItemText(startPosition);
                //根据position获取该item所对应的View
                mStartDragItemView = getChildAt(mDragPosition - getFirstVisiblePosition());
                if (firstPress) {
                    if (mStartDragItemView != null) {
                        lines.add(new Line(mStartDragItemView.getX() + mStartDragItemView.getWidth() / 2, mStartDragItemView.getY() + mStartDragItemView.getHeight() / 2));
                    }
                } else {
                    lines.add(new Line(ev.getX(), ev.getY()));
                }
                firstPress = false;
            } else if ((action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_UP) && lines.size() > 0) {
                Line current = lines.get(lines.size() - 1);
                current.stopX = ev.getX();
                current.stopY = ev.getY();
                if (mDragAdapter != null) {
                    int stopPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
                    stopNum = mDragAdapter.getItemText(stopPosition);
                    View stopItemView;
                    stopItemView = getChildAt(stopPosition);
                    if (action == MotionEvent.ACTION_UP && stopItemView != null) {
                        firstPress = true;
                        current.stopX = stopItemView.getX() + stopItemView.getWidth() / 2;
                        current.stopY = stopItemView.getY() + stopItemView.getHeight() / 2;
                    }
                    LogUtil.defaultLog("action move------>" + stopNum);

                    if (!TextUtils.isEmpty(startNum) && startNum != MCConstants.FILL_LAYOUT && !TextUtils.isEmpty(stopNum) && stopNum != MCConstants.FILL_LAYOUT && startNum != stopNum) {
                            MCConstants.LINE_PASS = true;
                    }

                }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lines.clear();
                        invalidateViews();
                    }
                }, 300);
            }
        }
        return super.onTouchEvent(ev);
    }

    public String getStopNum() {
        return stopNum;
    }

    private Handler mHandler = new Handler();

    private void initPaint() {
        paint.setAntiAlias(true);
        paint.setStrokeWidth(20f);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (TextUtils.isEmpty(startNum) || startNum == MCConstants.FILL_LAYOUT || TextUtils.isEmpty(stopNum) || stopNum == MCConstants.FILL_LAYOUT || startNum == stopNum) {
            return;
        }
        for (Line l : lines) {
            if (Math.abs(l.startX - l.stopX) > 50 || Math.abs(l.startY - l.stopY) > 50) {
                canvas.drawLine(l.startX, l.startY, l.stopX, l.stopY, paint);
            }
        }
    }

    class Line {
        float startX, startY, stopX, stopY;

        public Line(float startX, float startY, float stopX, float stopY) {
            this.startX = startX;
            this.startY = startY;
            this.stopX = stopX;
            this.stopY = stopY;
        }

        public Line(float startX, float startY) { // for convenience
            this(startX, startY, startX, startY);
        }
    }
}
