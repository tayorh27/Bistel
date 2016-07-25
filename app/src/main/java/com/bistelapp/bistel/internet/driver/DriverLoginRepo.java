package com.bistelapp.bistel.internet.driver;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bistelapp.bistel.AppConfig;
import com.bistelapp.bistel.database.driver.DriverLocalStorage;
import com.bistelapp.bistel.drivers.DriverHomeActivity;
import com.bistelapp.bistel.informations.driver.driver_info;
import com.bistelapp.bistel.network.VolleySingleton;
import com.bistelapp.bistel.utility.General;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Control & Inst. LAB on 15-Jul-16.
 */
public class DriverLoginRepo {

    private String email,password;
    Context context;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    private String url = AppConfig.DRIVER_WEB_URL+"login.php";
    General general;
    DriverLocalStorage driverLocalStorage;

    public DriverLoginRepo(Context context, String email,String password){
        this.context = context;
        this.email = email;
        this.password = password;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        general = new General(context);
        driverLocalStorage = new DriverLocalStorage(context);
    }

    public void Login(){
        String web = url+"?email="+email;
        general.displayProgressDialog("logging driver in");

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, web, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject object = response.getJSONObject(0);
                    String get_password = object.getString("password");
                    String get_active = object.getString("activate_driver");
                    String email_activated = object.getString("email_activated");
                    if(!get_active.contentEquals("true")){
                        general.dismissProgressDialog();
                        general.displayAlertDialog("Login Error", "Your account has been deactivated or not yet activated!");
                    }else if (get_password.contentEquals(password)){
                        if(email_activated.contentEquals("false")){
                            general.dismissProgressDialog();
                            general.displayAlertDialog("Login Error", "Your email address has not been activated!");
                        }else {
                            int id = object.getInt("id");
                            String first = object.getString("first_name");
                            String last = object.getString("last_name");
                            String email = object.getString("email");
                            String plate_number = object.getString("plate_number");
                            String mobile = object.getString("mobile");
                            String image = object.getString("image");
                            String status = object.getString("status");
                            String current_location = object.getString("current_location");
                            String playerID = object.getString("playerID");

                            driver_info current = new driver_info(id, first, last, email,plate_number, mobile, get_password, image,status,current_location,"",playerID);
                            driverLocalStorage.storeDriver(current);
                            driverLocalStorage.setDriverLogged(true);
                            general.dismissProgressDialog();

                            context.startActivity(new Intent(context, DriverHomeActivity.class));
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
