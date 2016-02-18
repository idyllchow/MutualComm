package com.sponia.foundationmoudle.utils;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
/**自定义文本字体
 *
 * 我们知道可以通过读取文件的方式得到自定义的typeface，因此完全可以通过掉包的方式实现自定义字体。
 * Typeface.createFromAsset
 * Typeface numTypeface = Typeface.createFromAsset(mContext.getAssets(),"fonts/290-CAI978.ttf");
 * 亦可以通过下面两种方法获取Typeface对象:
 * createFromFile(File path);
 * createFromFile(String path);
 * 获取到Typeface对象后,把Typeface对象作为此自定义字体类的构造函数的参数,传入SpannableString的setSapn方法即可.
 * 使用这种自定义字体时:
 * spannableString.setSpan(new CustomTypefaceSpan(numTypeface), 0, stringsize,Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
 *
 * */
public class CustomTypefaceSpan extends MetricAffectingSpan {
    private final Typeface typeface;

    public CustomTypefaceSpan(final Typeface typeface) {
        this.typeface = typeface;
    }

    /**更新绘制状态*/
    @Override
    public void updateDrawState(final TextPaint drawState) {
        apply(drawState);
    }
    /**更新测量状态*/
    @Override
    public void updateMeasureState(final TextPaint paint) {
        apply(paint);
    }

    private void apply(final Paint paint) {
        //获取原本的Typeface
        final Typeface oldTypeface = paint.getTypeface();
        //获取原本的Typeface的style样式
        final int oldStyle = oldTypeface != null ? oldTypeface.getStyle() : 0;

        final int fakeStyle = oldStyle & ~typeface.getStyle();
        //如果本来不是粗体,则设置粗体
        if ((fakeStyle & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }
        //如果本来不是斜体,则设置斜体
        if ((fakeStyle & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(typeface);
    }
}
