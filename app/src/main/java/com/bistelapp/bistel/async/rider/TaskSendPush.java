package com.bistelapp.bistel.async.rider;

import android.content.Context;
import android.os.AsyncTask;

import com.bistelapp.bistel.internet.rider.SendPushToDriver;

/**
 * Created by Control & Inst. LAB on 15-Jul-16.
 */
public class TaskSendPush extends AsyncTask<Void,Void,Void> {

    Context context;
    String driver_player_id,message;
    SendPushToDriver sendPushToDriver;

    public TaskSendPush(Context context,String driver_player_id,String message){
        this.context = context;
        sendPushToDriver = new SendPushToDriver(context);
        this.driver_player_id = driver_player_id;
        this.message = message;
    }

    @Override
    protected Void doInBackground(Void... params) {
       sendPushToDriver.sendPush(driver_player_id,message);
        return null;
    }
}
