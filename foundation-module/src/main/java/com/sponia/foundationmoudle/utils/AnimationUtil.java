package com.sponia.foundationmoudle.utils;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

import com.sponia.foundationmoudle.common.Common;

/**
 * @author shibo
 * @packageName com.sponia.foundationmoudle.utils
 * @description
 * @date 16/2/3
 */
public class AnimationUtil {
    /***
     * 从屏幕的左边进入
     *
     * @return
     */
    public static final AnimationSet leftInAnim() {
        AnimationSet animSet = new AnimationSet(true);
        TranslateAnimation leftAnim = new TranslateAnimation(
                -Common.getScreenWidth(), 0f, 0f, 0f);
        leftAnim.setDuration(500);
        leftAnim.setFillAfter(true);
        animSet.addAnimation(leftAnim);
        animSet.addAnimation(alphaInAnim());
        return animSet;
    }

    /***
     * 从屏幕的左边出去
     *
     * @return
     */
    public static final AnimationSet leftOutAnim() {
        AnimationSet animSet = new AnimationSet(true);
        TranslateAnimation leftAnim = new TranslateAnimation(0,
                -Common.getScreenWidth(), 0f, 0f);
        leftAnim.setDuration(500);
        leftAnim.setFillAfter(true);
        animSet.addAnimation(leftAnim);
        animSet.addAnimation(alphaOutAnim());
        return animSet;
    }

    /***
     * 从屏幕的右边进入
     *
     * @return
     */
    public static final AnimationSet rightInAnim() {
        AnimationSet animSet = new AnimationSet(true);
        TranslateAnimation rightInAnim = new TranslateAnimation(
                Common.getScreenWidth(), 0f, 0f, 0f);
        rightInAnim.setDuration(500);
        rightInAnim.setFillAfter(true);
        animSet.addAnimation(rightInAnim);
        animSet.addAnimation(alphaInAnim());
        return animSet;
    }

    /***
     * 从屏幕的右边出去
     *
     * @return
     */
    public static final AnimationSet rightOutAnim() {
        AnimationSet animSet = new AnimationSet(true);
        TranslateAnimation rightOutAnim = new TranslateAnimation(0,
                Common.getScreenWidth(), 0f, 0f);
        rightOutAnim.setDuration(500);
        rightOutAnim.setFillAfter(true);
        animSet.addAnimation(rightOutAnim);
        animSet.addAnimation(alphaOutAnim());
        return animSet;
    }

    /***
     * 淡入动画
     *
     * @return
     */
    public static final AlphaAnimation alphaInAnim() {
        AlphaAnimation alphaInAnim = new AlphaAnimation(0.1f, 1.0f);
        alphaInAnim.setDuration(500);
        return alphaInAnim;
    }

    /***
     * 淡出动画
     *
     * @return
     */
    public static final AlphaAnimation alphaOutAnim() {
        AlphaAnimation alphaOutAnim = new AlphaAnimation(1.0f, 0.1f);
        alphaOutAnim.setDuration(500);
        return alphaOutAnim;
    }

    /**
     * loading旋转动画
     *
     * @return
     */
    public static final RotateAnimation circleAnim() {
        RotateAnimation rotateAnim = new RotateAnimation(0, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnim.setDuration(1500);
        rotateAnim.setRepeatCount(-1);
        rotateAnim.setStartOffset(-1);
        rotateAnim.setRepeatMode(Animation.RESTART);
        rotateAnim.setInterpolator(new LinearInterpolator());
        return rotateAnim;
    }
}
