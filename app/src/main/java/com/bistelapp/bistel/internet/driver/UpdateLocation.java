package com.bistelapp.bistel.internet.driver;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bistelapp.bistel.AppConfig;
import com.bistelapp.bistel.database.driver.DriverLocalStorage;
import com.bistelapp.bistel.database.rider.UserLocalStorage;
import com.bistelapp.bistel.informations.driver.driver_info;
import com.bistelapp.bistel.informations.rider.rider_info;
import com.bistelapp.bistel.network.VolleySingleton;
import com.bistelapp.bistel.utility.General;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tayo on 4/8/2016.
 */
public class UpdateLocation {

    private String location;
    Context context;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    private String url = AppConfig.DRIVER_WEB_URL + "update_current_location.php";
    General general;
    UserLocalStorage userLocalStorage;
    DriverLocalStorage driverLocalStorage;
    private driver_info di;

    public UpdateLocation(Context context, String location) {
        this.context = context;
        this.location = location;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        general = new General(context);
        userLocalStorage = new UserLocalStorage(context);
        driverLocalStorage = new DriverLocalStorage(context);
    }

    public void update_rider_location() {

        final rider_info ri = userLocalStorage.getRiderInfo();
        di = driverLocalStorage.getDriverInfo();
        int id = di.id;

        String web = url + "?id=" + id + "&current_location=" + location;

        general.displayProgressDialog("updating rider location...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, web, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    int success;
                    JSONObject object = new JSONObject(response);
                    success = object.getInt("success");
                    if (success == 1) {
                        Toast.makeText(context, "location update successful.", Toast.LENGTH_LONG).show();

                        //rider_info current = new rider_info(ri.id, ri.firstname, ri.lastname, ri.email, ri.mobile, ri.password, location,ri.voucher,ri.voucher_status,ri.playerID,ri.voucher_code_percent);
                        //userLocalStorage.storeUser(current);

                        //context.startActivity(new Intent(context, RiderActivity.class));
                        general.dismissProgressDialog();
                    } else if (success == 0) {
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
