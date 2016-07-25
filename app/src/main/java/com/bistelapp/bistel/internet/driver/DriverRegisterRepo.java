package com.bistelapp.bistel.internet.driver;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bistelapp.bistel.AppConfig;
import com.bistelapp.bistel.MainActivity;
import com.bistelapp.bistel.network.VolleySingleton;
import com.bistelapp.bistel.utility.General;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tayo on 3/31/2016.
 */
public class DriverRegisterRepo {

    private String first_name,last_name,email,plate_number,mobile,password;
    Context context;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    private String url = AppConfig.DRIVER_WEB_URL+"Register.php";
    General general;

    public DriverRegisterRepo(Context context, String first_name,String last_name,String email,String plate_number, String mobile,String password){
        this.context = context;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.plate_number = plate_number;
        this.mobile = mobile;
        this.password = password;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        general = new General(context);
    }

    public void Register(final String player_id){

        general.displayProgressDialog("Registering rider...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    int success;
                    JSONObject object = new JSONObject(response);
                    success = object.getInt("success");
                    if(success == 1){
                        Toast.makeText(context, "Registration successful. Login now", Toast.LENGTH_LONG).show();
                        context.startActivity(new Intent(context, MainActivity.class));
                        general.dismissProgressDialog();
                    }else if(success == 2){
                        Toast.makeText(context, "Registration failed. Email address already exist.", Toast.LENGTH_LONG).show();
                        general.dismissProgressDialog();
                    } else if(success == 0){
                        Toast.makeText(context, "Registration failed. try again.", Toast.LENGTH_LONG).show();
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
        }){
            @Override
            public String getPostBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("first_name",first_name);
                params.put("last_name",last_name);
                params.put("email",email);
                params.put("plate_number",plate_number);
                params.put("mobile",mobile);
                params.put("password",password);
                params.put("image","avatar.png");
                params.put("status","offline");
                params.put("current_location","unilag");
                params.put("activate_driver","false");
                params.put("playerID",player_id);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
