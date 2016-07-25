package com.bistelapp.bistel.internet.rider;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bistelapp.bistel.AppConfig;
import com.bistelapp.bistel.callbacks.rider.LoadOnlineDrivers;
import com.bistelapp.bistel.database.rider.UserLocalStorage;
import com.bistelapp.bistel.informations.driver.driver_info;
import com.bistelapp.bistel.informations.rider.rider_info;
import com.bistelapp.bistel.network.VolleySingleton;
import com.bistelapp.bistel.utility.General;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tayo on 4/12/2016.
 */
public class FetchOnlineDrivers {

    Context context;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    private String url = AppConfig.RIDER_WEB_URL + "FetchOnlineDrivers.php?status=online";
    General general;
    LoadOnlineDrivers component;
    GetDistanceDuration getDistanceDuration;
    UserLocalStorage userLocalStorage;


    public FetchOnlineDrivers(Context context, LoadOnlineDrivers component) {
        this.context = context;
        this.component = component;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        general = new General(context);
        userLocalStorage = new UserLocalStorage(context);
        getDistanceDuration = new GetDistanceDuration(context);
    }

    public void OnlineDrivers() {

        final ArrayList<driver_info> customData = new ArrayList<>();
        final rider_info ri = userLocalStorage.getRiderInfo();

        general.displayProgressDialog("contacting drivers...");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(context, response, Toast.LENGTH_LONG).show();

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i< jsonArray.length(); i++){
                        JSONObject json = jsonArray.getJSONObject(i);

                        int id = json.getInt("id");
                        String first_name = json.getString("first_name");
                        String last_name = json.getString("last_name");
                        String email = json.getString("email");
                        String plate_number = json.getString("plate_number");
                        String mobile = json.getString("mobile");
                        String image = json.getString("image");
                        String current_location = json.getString("current_location");
                        String distance = getDistanceDuration.getDistance(current_location,ri.current_location);
                        String status = json.getString("status");
                        String playerID = json.getString("playerID");


                        driver_info current = new driver_info(id,first_name,last_name,email,plate_number,mobile,"",image,status,current_location,distance,playerID);
                        customData.add(current);
                    }

                    if(component != null){
                        component.onLoadOnlineDrivers(customData);
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
}

