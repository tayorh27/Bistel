package com.bistelapp.bistel.internet.rider;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bistelapp.bistel.AppConfig;
import com.bistelapp.bistel.callbacks.rider.LoadDistanceDuration;
import com.bistelapp.bistel.network.VolleySingleton;
import com.bistelapp.bistel.utility.General;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tayo on 4/19/2016.
 */
public class GetDistanceDuration {

    String origin,destination;
    Context context;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    General general;
    LoadDistanceDuration loadDistanceDuration;
    String distance = "";

    public GetDistanceDuration(Context context,String origin,String destination, LoadDistanceDuration loadDistanceDuration){
        this.origin = origin;
        this.destination = destination;
        this.context = context;
        this.loadDistanceDuration = loadDistanceDuration;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        general = new General(context);
    }

    public GetDistanceDuration(Context context){
        this.context = context;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        general = new General(context);
    }

    public void getDistanceDuration(){

        general.displayProgressDialog("calculating distance and duration");

        String url = AppConfig.GET_DISTANCE_AND_DURATION+origin+"&destinations="+destination+"&key="+AppConfig.API_KEY_FOR_DISTANCE_DURATION;

        StringRequest stringRequest = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("OK")){
                        JSONArray array = object.getJSONArray("rows");
                        JSONObject object1 = array.getJSONObject(0);
                        JSONArray array1 = object1.getJSONArray("elements");
                        JSONObject object2 = array1.getJSONObject(0);
                        JSONObject object3 = object2.getJSONObject("distance");
                        JSONObject object4 = object2.getJSONObject("duration");

                        String g_distance = object3.getString("text");
                        String g_duration = object4.getString("text");

                        if(loadDistanceDuration != null){
                            loadDistanceDuration.onLoadDistanceDuration(g_distance,g_duration);
                        }

                    }else {
                        Toast.makeText(context,"distance duration error - "+status,Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                general.dismissProgressDialog();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                general.dismissProgressDialog();
                General.handleVolleyError(error, context);
            }
        });
        requestQueue.add(stringRequest);
    }

    public String getDistance(String ori, String dest){

        //general.displayProgressDialog("calculating distance and duration");

        String url = AppConfig.GET_DISTANCE_AND_DURATION+ori+"&destinations="+dest+"&key="+AppConfig.API_KEY_FOR_DISTANCE_DURATION;

        StringRequest stringRequest = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("OK")){
                        JSONArray array = object.getJSONArray("rows");
                        JSONObject object1 = array.getJSONObject(0);
                        JSONArray array1 = object1.getJSONArray("elements");
                        JSONObject object2 = array1.getJSONObject(0);
                        JSONObject object3 = object2.getJSONObject("distance");
                        JSONObject object4 = object2.getJSONObject("duration");

                        String g_distance = object3.getString("text");
                        String g_duration = object4.getString("text");

                        distance = g_distance;


                    }else {
                        Toast.makeText(context,"Error from me - "+status,Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //general.dismissProgressDialog();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //general.dismissProgressDialog();
                General.handleVolleyError(error, context);
            }
        });
        requestQueue.add(stringRequest);

        return distance;
    }
}
