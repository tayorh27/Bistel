package com.bistelapp.bistel.async.driver;

import android.content.Context;
import android.os.AsyncTask;

import com.bistelapp.bistel.utility.LocationGetter;

/**
 * Created by Control & Inst. LAB on 09-Jun-16.
 */
public class UpdateLocationTask extends AsyncTask<Void,Void,Void> {

    Context context;
    public UpdateLocationTask(Context context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        LocationGetter locationGetter = new LocationGetter(context);
        locationGetter.runOnStart();
        return null;
    }
}
