package com.bistelapp.bistel.internet.rider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bistelapp.bistel.AppConfig;
import com.bistelapp.bistel.async.rider.TaskSendPush;
import com.bistelapp.bistel.async.rider.TaskUpdateVoucherStatus;
import com.bistelapp.bistel.database.rider.UserLocalStorage;
import com.bistelapp.bistel.informations.rider.rider_info;
import com.bistelapp.bistel.network.VolleySingleton;
import com.bistelapp.bistel.riders.RiderActivity;
import com.bistelapp.bistel.utility.General;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Control & Inst. LAB on 10-Jun-16.
 */
public class BookingsRepo {

    Context context;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    private String url = AppConfig.RIDER_WEB_URL+"BookingAndRequest.php";
    General general;
    UserLocalStorage userLocalStorage;
     String driver_name,driver_number,driver_plate_number,rider_name,rider_number,pickUp,destination,distance,duration,pickUp_time,normal_price,negotiated_price,rider_type;
    int driver_id,rider_id;
    public BookingsRepo(Context context, String driver_name,String driver_number,int driver_id,String driver_plate_number,String rider_name,String rider_number,int rider_id,String pickUp,String destination,String distance,String duration,
                        String pickUp_time,String normal_price,String negotiated_price,String rider_type){
        this.context = context;
        this.driver_name = driver_name;
        this.driver_number = driver_number;
        this.driver_id = driver_id;
        this.driver_plate_number = driver_plate_number;
        this.rider_name = rider_name;
        this.rider_number = rider_number;
        this.rider_id = rider_id;
        this.pickUp = pickUp;
        this.destination = destination;
        this.distance = distance;
        this.duration = duration;
        this.pickUp_time = pickUp_time;
        this.normal_price = normal_price;
        this.negotiated_price = negotiated_price;
        this.rider_type = rider_type;


        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        general = new General(context);
        userLocalStorage = new UserLocalStorage(context);
    }

    private void update_voucher_status() {
        rider_info ri = userLocalStorage.getRiderInfo();
        rider_info update = new rider_info(ri.id, ri.firstname, ri.lastname, ri.email, ri.mobile, ri.password, ri.current_location, "", "used", ri.playerID, ri.voucher_code_percent);
        userLocalStorage.storeUser(update);
        new TaskUpdateVoucherStatus(context).execute();
    }

    private void update_percent() {
        rider_info ri = userLocalStorage.getRiderInfo();
        rider_info update = new rider_info(ri.id, ri.firstname, ri.lastname, ri.email, ri.mobile, ri.password, ri.current_location, "", "used", ri.playerID, 0);
        userLocalStorage.storeUser(update);
    }

    public void UploadRide(final String payment_type, final String ride_type, final SharedPreferences data, final String vouchersUsed, final String driver_player_id, final String message){

        if(ride_type.contentEquals("Request")) {
            general.displayProgressDialog("Requesting for driver...");
        }else{
            general.displayProgressDialog("Booking for driver...");
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    int success;
                    JSONObject object = new JSONObject(response);
                    success = object.getInt("success");
                    if(success == 1){
                        general.dismissProgressDialog();
                        general.deletePreferenceData(data);
                        new TaskSendPush(context,driver_player_id,message).execute();
                        update_percent();
                        update_voucher_status();
                        Toast.makeText(context, "Ride already Booked.\n" +
                                "Thank you for choosing Bistel Ride.", Toast.LENGTH_LONG).show();
                        context.startActivity(new Intent(context, RiderActivity.class));
                    }
                    else if(success == 0){
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
                params.put("driver_name",driver_name);
                params.put("driver_number",driver_number);
                params.put("driver_id",driver_id+"");
                params.put("driver_plate_number",driver_plate_number);
                params.put("rider_name",rider_name);
                params.put("rider_number",rider_number);
                params.put("rider_id",rider_id+"");
                params.put("pickUp",pickUp);
                params.put("destination",destination);
                params.put("distance",distance);
                params.put("duration",duration);
                params.put("pickUp_time",pickUp_time);
                params.put("normal_price",normal_price);
                params.put("negotiated_price",negotiated_price);
                params.put("payment_type",payment_type);
                params.put("ride_type",rider_type);
                params.put("vouchersUsed",vouchersUsed);
                params.put("ride_status",ride_type+" - pending");
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
