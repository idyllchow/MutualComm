package com.sponia.foundationmoudle.common;

import android.app.Application;

/**
 * @packageName com.sponia.foundationmoudle.utils
 * @description base application
 * @date 15/9/8
 * @auther shibo
 */
public class SponiaBaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Common.application = this;
    }
}
