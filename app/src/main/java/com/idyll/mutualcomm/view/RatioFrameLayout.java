package com.idyll.mutualcomm.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.idyll.mutualcomm.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class RatioFrameLayout extends FrameLayout {

    public static final int HORIZONTAL = 0;

    public static final int VERTICAL = 1;

    private float mRatio = 1.0f;

    private int mOrientation;

    @IntDef({HORIZONTAL, VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationMode {

    }

    public RatioFrameLayout(Context context) {
        this(context, null);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (null != attrs) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatioFrameLayout);
            if (null != a) {
                mRatio = a.getFloat(R.styleable.RatioFrameLayout_rf_ratio, mRatio);
                mOrientation = a.getInt(R.styleable.RatioFrameLayout_op_orientation, HORIZONTAL);
                a.recycle();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mRatio > 0) {
            int widthSize;
            int heightSize;
            if (HORIZONTAL == mOrientation) {
                heightSize = (int) (MeasureSpec.getSize(widthMeasureSpec) * mRatio);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
            } else {
                widthSize = (int) (MeasureSpec.getSize(heightMeasureSpec) * mRatio);
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int getOrientation() {
        return mOrientation;
    }

    /**
     * @param orientation Pass {@link #HORIZONTAL} or {@link #VERTICAL}. Default
     *                    value is {@link #HORIZONTAL}.
     * @attr ref R.styleable#GridLayout_gridlayout_orientation
     */
    public void setOrientation(@OrientationMode int orientation) {
        if (mOrientation != orientation) {
            mOrientation = orientation;
            requestLayout();
        }
    }

}
