package com.bistelapp.bistel.internet.driver;

import android.content.Context;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bistelapp.bistel.AppConfig;
import com.bistelapp.bistel.R;
import com.bistelapp.bistel.database.driver.DriverLocalStorage;
import com.bistelapp.bistel.informations.driver.driver_info;
import com.bistelapp.bistel.network.VolleySingleton;
import com.bistelapp.bistel.utility.General;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Control & Inst. LAB on 13-Jul-16.
 */
public class UpdateStatus {

    Context context;
    Button button;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private String status = "";
    private int color = 0;
    private General general;
    DriverLocalStorage driverLocalStorage;
    private driver_info di;

    public UpdateStatus(Context context, Button button){
        this.context = context;
        this.button = button;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        general = new General(context);
        driverLocalStorage = new DriverLocalStorage(context);
    }

    public void updateDriverStatus(){
        String getTag = button.getTag().toString();
        if(getTag.contentEquals("online")){
            status = "offline";
            color = R.color.myAccentColor;
        }else {
            status = "online";
            color = R.color.green;
        }
        di = driverLocalStorage.getDriverInfo();
        int id = di.id;
        String url = AppConfig.DRIVER_WEB_URL+"update_status.php?id="+id+"&status="+status;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int success;
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    success = jsonObject.getInt("success");
                    if(success == 0){
                        general.dismissProgressDialog();
                        button.setText(status);
                        button.setText("GO "+status);
                        button.setBackgroundColor(color);
                    }else {
                        general.dismissProgressDialog();
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
