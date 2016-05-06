package com.bistelapp.bistel.internet.rider;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bistelapp.bistel.AppConfig;
import com.bistelapp.bistel.database.rider.UserLocalStorage;
import com.bistelapp.bistel.informations.rider.rider_info;
import com.bistelapp.bistel.network.VolleySingleton;
import com.bistelapp.bistel.riders.RiderActivity;
import com.bistelapp.bistel.utility.General;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tayo on 4/8/2016.
 */
public class LoginRepo {

    private String email,password;
    Context context;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    private String url = AppConfig.RIDER_WEB_URL+"login.php";
    General general;
    UserLocalStorage userLocalStorage;

    public LoginRepo(Context context, String email,String password){
        this.context = context;
        this.email = email;
        this.password = password;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        general = new General(context);
        userLocalStorage = new UserLocalStorage(context);
    }

    public void LogRiderIn(){
        String web = url+"?email="+email;
        general.displayProgressDialog("logging rider in");

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,web,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject object = response.getJSONObject(0);
                    String get_password = object.getString("password");
                    String get_active = object.getString("active");
                    String email_activated = object.getString("email_activated");
                    if(!get_active.contentEquals("true")){
                        general.dismissProgressDialog();
                        general.displayAlertDialog("Login Error", "Your account has been deactivated!");
                    }else if (get_password.contentEquals(password)){
                        if(email_activated.contentEquals("false")){
                            general.dismissProgressDialog();
                            general.displayAlertDialog("Login Error", "Your email has not been activated!");
                        }else {
                            int id = object.getInt("id");
                            String first = object.getString("first_name");
                            String last = object.getString("last_name");
                            String email = object.getString("email");
                            String mobile = object.getString("mobile");
                            String current_location = object.getString("current_location");

                            rider_info current = new rider_info(id, first, last, email, mobile, get_password, current_location);
                            userLocalStorage.storeUser(current);
                            userLocalStorage.setUserLogged(true);
                            general.dismissProgressDialog();

                            context.startActivity(new Intent(context, RiderActivity.class));
                        }
                    }else {
                        general.dismissProgressDialog();
                        general.displayAlertDialog("Login Error", "incorrect email or password");
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
        requestQueue.add(jsonArrayRequest);
    }
}
