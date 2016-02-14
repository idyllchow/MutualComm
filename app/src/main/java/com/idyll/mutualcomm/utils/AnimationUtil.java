package com.idyll.mutualcomm.utils;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

/**
 * @author shibo
 * @packageName com.idyll.mutualcomm.utils
 * @description
 * @date 16/2/3
 */
public class AnimationUtil {

    public static AnimationSet transAndAlaph(int position) {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.ABSOLUTE, 0,  //ABSOLUTE绝对值 动画的原点坐标为它所在的view的左上角
                Animation.ABSOLUTE, position,
                Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, 0);
        translateAnimation.setDuration(3000);//动画的持续时间 毫秒值
        translateAnimation.setFillAfter(true); //是否保持动画完成时的状态
        animationSet.addAnimation(translateAnimation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setStartOffset(0);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setFillBefore(false);
        animationSet.setFillAfter(true);
        return animationSet;
    }
}
