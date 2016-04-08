package com.bistelapp.bistel.json.rider;

import com.android.volley.RequestQueue;
import com.bistelapp.bistel.AppConfig;
import com.bistelapp.bistel.informations.rider.online_driver;
import com.bistelapp.bistel.utility.Requestor;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by tayo on 4/3/2016.
 */
public class RiderUtils {

    public static final String web_url = AppConfig.RIDER_WEB_URL+"php_fetch/FetchVoicenoteData.php";
    public RiderUtils(){
    }

    public ArrayList<online_driver> loadDrivers(RequestQueue requestQueue){

        JSONArray response = Requestor.sendRequestDrivers(requestQueue, web_url);
        ArrayList<online_driver> lists = Parser.parseJSONResponse(response);
        return lists;
    }

}
