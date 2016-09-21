package com.bistelapp.bistel;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.bistelapp.bistel.database.rider.BookingsDatabase;
import com.bistelapp.bistel.drivers.DriverHomeActivity;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by tayo on 3/29/2016.
 */
public class MyApplication extends Application{

    private static MyApplication sInstance;
    private static BookingsDatabase database;


    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this).setNotificationOpenedHandler(new OneSignal.NotificationOpenedHandler() {
            @Override
            public void notificationOpened(String s, JSONObject jsonObject, boolean b) {
                startActivity(new Intent(getAppContext(), DriverHomeActivity.class));
            }
        }).init();
        sInstance = this;
        database = new BookingsDatabase(this);
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

    public synchronized static BookingsDatabase getWritableDatabase(){
        if (database == null){
            database = new BookingsDatabase(getAppContext());
        }
        return database;

    }
}
