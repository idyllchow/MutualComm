package com.idyll.mutualcomm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * @author shibo
 * @packageName com.sponia.basketballstats.view
 * @description
 * @date 15/12/1
 */
public class MCGridView extends GridView {

    public MCGridView(Context context) {
        super(context);
    }

    public MCGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MCGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;
        }
        return super.onTouchEvent(ev);
    }
}
