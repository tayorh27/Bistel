package com.bistelapp.bistel.internet.rider;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bistelapp.bistel.AppConfig;
import com.bistelapp.bistel.MainActivity;
import com.bistelapp.bistel.database.rider.UserLocalStorage;
import com.bistelapp.bistel.informations.rider.rider_info;
import com.bistelapp.bistel.network.VolleySingleton;
import com.bistelapp.bistel.riders.RiderActivity;
import com.bistelapp.bistel.utility.General;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tayo on 4/8/2016.
 */
public class UpdateRepo {

    private String first_name,last_name,mobile;
    Context context;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    private String url = AppConfig.RIDER_WEB_URL+"update.php";
    General general;
    UserLocalStorage userLocalStorage;

    public UpdateRepo(Context context,String first_name,String last_name,String mobile){
        this.context = context;
        this.first_name = first_name;
        this.last_name = last_name;
        this.mobile = mobile;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        general = new General(context);
        userLocalStorage = new UserLocalStorage(context);
    }

    public void updateRider(){

        final rider_info ri = userLocalStorage.getRiderInfo();

        String web = url+"?id="+ri.id+"&first_name="+first_name+"&last_name="+last_name+"&mobile="+mobile;

        general.displayProgressDialog("updating rider info...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, web, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    int success;
                    JSONObject object = new JSONObject(response);
                    success = object.getInt("success");
                    if(success == 1){
                        Toast.makeText(context, "update successful.", Toast.LENGTH_LONG).show();

                        rider_info current = new rider_info(ri.id,first_name,last_name,ri.email,mobile,ri.password,ri.current_location);
                        userLocalStorage.storeUser(current);

                        context.startActivity(new Intent(context, RiderActivity.class));
                        general.dismissProgressDialog();
                    }else if(success == 0){
                        Toast.makeText(context, "update failed. try again.", Toast.LENGTH_LONG).show();
                        general.dismissProgressDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                General.handleVolleyError(error, context);
                general.dismissProgressDialog();
            }
        });
        requestQueue.add(stringRequest);
    }
}
