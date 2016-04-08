package com.bistelapp.bistel.async.rider;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.bistelapp.bistel.callbacks.rider.LoadOnlineDrivers;
import com.bistelapp.bistel.informations.rider.online_driver;
import com.bistelapp.bistel.json.rider.RiderUtils;
import com.bistelapp.bistel.network.VolleySingleton;

import java.util.ArrayList;

/**
 * Created by tayo on 4/3/2016.
 */
public class TaskLoadOnlineDrivers extends AsyncTask<Void, Void, ArrayList<online_driver>> {

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private LoadOnlineDrivers component;
    private RiderUtils riderUtils;

    public TaskLoadOnlineDrivers(LoadOnlineDrivers component){
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        this.component = component;
        riderUtils = new RiderUtils();
    }

    @Override
    protected ArrayList<online_driver> doInBackground(Void... params) {
        ArrayList<online_driver> listRecent = riderUtils.loadDrivers(requestQueue);
        return listRecent;
    }

    @Override
    protected void onPostExecute(ArrayList<online_driver> list) {
        if(component != null){
            component.onLoadOnlineDrivers(list);
        }
    }
}
