package com.idyll.mutualcomm.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.idyll.mutualcomm.R;

import java.util.WeakHashMap;


public class FontTextView extends TextView {
    private static WeakHashMap<Integer, Typeface> sIconFontMap = new WeakHashMap<>();

    public final static int FONT_TYPE_DIM_BOLD = 0;

    public final static int FONT_TYPE_LEXIA_BOLD = 1;

    public final static int FONT_TYPE_LEXIA_REGULAR = 2;

    private int mFontType;

    public FontTextView(Context context) {
        this(context, null);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (null != attrs) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FontTextView, defStyle, 0);
            mFontType = a.getInteger(R.styleable.FontTextView_fontStyle, 0);
            a.recycle();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        this.setTypeface(getTypeface());
        this.setIncludeFontPadding(false);
    }

    @Override
    public Typeface getTypeface() {
        return getOrCreateTypeFace(getContext(), mFontType);
    }

    public static Typeface getOrCreateTypeFace(Context context, int fontType) {
        if (null == sIconFontMap.get(fontType)) {
            try {
                Typeface iconFont = null;
                switch (fontType) {
                    case FONT_TYPE_DIM_BOLD:
                        iconFont = Typeface.createFromAsset(context.getAssets(),
                                "fonts/dim_bold.ttf");
                        break;
                    case FONT_TYPE_LEXIA_BOLD:
                        iconFont = Typeface.createFromAsset(context.getAssets(),
                                "fonts/lexia_bold.ttf");
                        break;
                    case FONT_TYPE_LEXIA_REGULAR:
                        iconFont = Typeface.createFromAsset(context.getAssets(),
                                "fonts/lexia_regular.ttf");
                        break;
                }
                if (null != iconFont) {
                    sIconFontMap.put(fontType, iconFont);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sIconFontMap.get(fontType);
    }
}
