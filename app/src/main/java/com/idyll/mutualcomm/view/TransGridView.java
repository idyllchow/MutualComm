package com.idyll.mutualcomm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author shibo
 * @packageName com.sponia.basketballstats.view
 * @description
 * @date 15/12/1
 */
public class TransGridView extends GridView {

    public TransGridView(Context context) {
        super(context);
    }

    public TransGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TransGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
