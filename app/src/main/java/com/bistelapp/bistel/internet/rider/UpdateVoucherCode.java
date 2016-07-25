package com.bistelapp.bistel.internet.rider;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bistelapp.bistel.AppConfig;
import com.bistelapp.bistel.database.rider.UserLocalStorage;
import com.bistelapp.bistel.informations.rider.rider_info;
import com.bistelapp.bistel.network.VolleySingleton;
import com.bistelapp.bistel.utility.General;

/**
 * Created by Control & Inst. LAB on 10-Jun-16.
 */
public class UpdateVoucherCode {

    Context context;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    private String url = AppConfig.RIDER_WEB_URL+"update_voucher.php";
    General general;
    UserLocalStorage userLocalStorage;

    public UpdateVoucherCode(Context context){
        this.context = context;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        general = new General(context);
        userLocalStorage = new UserLocalStorage(context);
    }

    public void updateCode(){
        final rider_info ri = userLocalStorage.getRiderInfo();
        String web = url + "?id=" + ri.id + "&voucher=" + " ";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, web, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }
}
