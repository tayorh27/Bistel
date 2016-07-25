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
import com.bistelapp.bistel.async.rider.TaskUpdateVouchers;
import com.bistelapp.bistel.database.rider.UserLocalStorage;
import com.bistelapp.bistel.informations.rider.rider_info;
import com.bistelapp.bistel.network.VolleySingleton;
import com.bistelapp.bistel.riders.RiderActivity;
import com.bistelapp.bistel.utility.General;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Control & Inst. LAB on 10-Jun-16.
 */
public class FetchVoucherCode {

    Context context;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    String email;
    UserLocalStorage userLocalStorage;
    private String url = AppConfig.RIDER_WEB_URL+"login.php";
    General general;

    public FetchVoucherCode(Context context, String email){
        this.context = context;
        this.email = email;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        userLocalStorage = new UserLocalStorage(context);
        general = new General(context);
    }

    public void FetchCode(final String input){
        String web = url+"?email="+email;
        general.displayProgressDialog("verifying voucher code");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, web, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                general.dismissProgressDialog();
                try {
                    JSONArray object = new JSONArray(response);
                    JSONObject jsonObject = object.getJSONObject(0);
                    String getCode = jsonObject.getString("voucher");
                    String getStatus = jsonObject.getString("voucher_status");
                    if(getCode.contentEquals(input)) {
                        int getPercent = Integer.parseInt(getCode.substring(10));
                        if(getPercent > 100){
                            Toast.makeText(context, "You just got ₦" + getPercent + " off", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(context, "You just got " + getPercent + "% off", Toast.LENGTH_LONG).show();
                        }
                        rider_info ri = userLocalStorage.getRiderInfo();
                        rider_info update = new rider_info(ri.id, ri.firstname, ri.lastname, ri.email, ri.mobile, ri.password, ri.current_location, "", getStatus, ri.playerID, getPercent);
                        userLocalStorage.storeUser(update);

                        new TaskUpdateVouchers(context).execute();
                        context.startActivity(new Intent(context, RiderActivity.class));
                    }else {
                        general.displayAlertDialog("Voucher Code Error","The code does not exist or has already been used by you.\nThank you for choosing Bistel Ride.");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                general.dismissProgressDialog();
                General.handleVolleyError(error,context);
            }
        });

        requestQueue.add(stringRequest);
    }
}
