package com.bistelapp.bistel.internet.rider;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bistelapp.bistel.AppConfig;
import com.bistelapp.bistel.network.VolleySingleton;
import com.bistelapp.bistel.utility.General;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Control & Inst. LAB on 26-Jun-16.
 */
public class SendPushToDriver {

    Context context;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    private String url = AppConfig.OneSignalPush;
    General general;

    public SendPushToDriver(Context context){
        this.context = context;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        general = new General(context);
    }

    public void sendPush(final String driver_player_id, final String message){



        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //general.displayAlertDialog("OneSignal",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                General.handleVolleyError(error,context);
            }
        }){
            @Override
            public String getPostBodyContentType() {
                return "application/json; charset=UTF-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {

                String post_body ="";
                try {
                    JSONObject jsonObject_main = new JSONObject();
                    jsonObject_main.put("app_id",AppConfig.OneSignalPushAppID);

                    JSONObject jsonObject_content = new JSONObject();
                    jsonObject_content.put("en",message);

                    JSONArray jsonArray_player_ids = new JSONArray();
                    jsonArray_player_ids.put(driver_player_id);

                    jsonObject_main.put("contents", jsonObject_content.toString());
                    jsonObject_main.put("include_player_ids",jsonArray_player_ids.toString());

                    post_body = jsonObject_main.toString();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return post_body.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization","Basic ONESIGNAL_REST_API_KEY");
                params.put("Content-Type","application/json");
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

}
