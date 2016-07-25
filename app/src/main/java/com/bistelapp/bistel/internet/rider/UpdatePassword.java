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
public class UpdatePassword {

    private String newPassword,currentPass;
    Context context;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    private String url = AppConfig.RIDER_WEB_URL+"update_password.php";
    General general;
    UserLocalStorage userLocalStorage;

    public UpdatePassword(Context context,String newPassword, String currentPass){
        this.context = context;
        this.newPassword = newPassword;
        this.currentPass = currentPass;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        general = new General(context);
        userLocalStorage = new UserLocalStorage(context);
    }

    public void updatePassword(){

        final rider_info ri = userLocalStorage.getRiderInfo();
        String current_password = ri.password;

        if (!currentPass.contentEquals(current_password)){
            general.displayAlertDialog("Password Error", "incorrect current password");
        }else {

            String web = url + "?id=" + ri.id + "&password=" + newPassword;

            general.displayProgressDialog("updating rider password...");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, web, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        int success;
                        JSONObject object = new JSONObject(response);
                        success = object.getInt("success");
                        if (success == 1) {
                            Toast.makeText(context, "update successful.", Toast.LENGTH_LONG).show();

                            rider_info current = new rider_info(ri.id, ri.firstname, ri.lastname, ri.email, ri.mobile, newPassword, ri.current_location,ri.voucher,ri.voucher_status,ri.playerID,ri.voucher_code_percent);
                            userLocalStorage.storeUser(current);

                            context.startActivity(new Intent(context, RiderActivity.class));
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

}
