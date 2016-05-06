package com.bistelapp.bistel.internet.rider;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bistelapp.bistel.AppConfig;
import com.bistelapp.bistel.callbacks.rider.LoadAddressLocation;
import com.bistelapp.bistel.network.VolleySingleton;
import com.bistelapp.bistel.utility.General;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tayo on 4/19/2016.
 */
public class GetLocationFromServer {

    double latitude,longitude;
    Context context;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    General general;
    String location = "";
    LoadAddressLocation loadAddressLocation;


    public GetLocationFromServer(Context context, double lat, double longi,LoadAddressLocation loadAddressLocation){
        this.latitude = lat;
        this.longitude = longi;
        this.context = context;
        this.loadAddressLocation = loadAddressLocation;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        general = new General(context);
    }

    public void UpdateLocation(){

        String url = AppConfig.GET_LOCATION_FROM_SERVER+latitude+","+longitude+"&sensor=true&key="+AppConfig.API_KEY_FOR_DISTANCE_DURATION;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("OK")) {
                        JSONArray array = object.getJSONArray("results");
                        JSONObject object1 = array.getJSONObject(0);
                        String location = object1.getString("formatted_address");

                        if (loadAddressLocation != null) {
                            loadAddressLocation.onLoadAddressLocation(location);
                        }
                    }else {
                        Toast.makeText(context,status,Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                General.handleVolleyError(error, context);
            }
        });

        requestQueue.add(stringRequest);
    }

    public String getLocation(){
        return location;
    }
}
