package com.bistelapp.bistel;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.bistelapp.bistel.riders.BookingActivity;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by tayo on 3/29/2016.
 */
public class MyApplication extends Application{

    private static MyApplication sInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this).setNotificationOpenedHandler(new OneSignal.NotificationOpenedHandler() {
            @Override
            public void notificationOpened(String s, JSONObject jsonObject, boolean b) {
                startActivity(new Intent(getAppContext(), BookingActivity.class));
            }
        }).init();
        sInstance = this;
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("avenir_light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    public static MyApplication getInstance(){
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }
}
