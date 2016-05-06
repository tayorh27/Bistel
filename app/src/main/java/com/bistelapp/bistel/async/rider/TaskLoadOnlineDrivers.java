package com.bistelapp.bistel.async.rider;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.bistelapp.bistel.callbacks.rider.LoadOnlineDrivers;
import com.bistelapp.bistel.informations.driver.driver_info;
import com.bistelapp.bistel.json.rider.RiderUtils;
import com.bistelapp.bistel.network.VolleySingleton;
import com.bistelapp.bistel.utility.General;

import java.util.ArrayList;

/**
 * Created by tayo on 4/3/2016.
 */
public class TaskLoadOnlineDrivers extends AsyncTask<Void, Void, ArrayList<driver_info>> {

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private LoadOnlineDrivers component;
    private RiderUtils riderUtils;
    private General general;
    private Context context;

    public TaskLoadOnlineDrivers(Context context, LoadOnlineDrivers component){
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        this.component = component;
        this.context = context;
        riderUtils = new RiderUtils();
        general = new General(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //general.displayProgressDialog("contacting drivers");
    }

    @Override
    protected ArrayList<driver_info> doInBackground(Void... params) {
        ArrayList<driver_info> listRecent = riderUtils.loadDrivers(requestQueue);
        return listRecent;
    }

    @Override
    protected void onPostExecute(ArrayList<driver_info> list) {
        if(component != null){
            component.onLoadOnlineDrivers(list);
            //general.dismissProgressDialog();
        }
    }
}
