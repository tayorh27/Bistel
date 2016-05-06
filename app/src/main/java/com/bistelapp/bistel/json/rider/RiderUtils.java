package com.bistelapp.bistel.json.rider;

import com.android.volley.RequestQueue;
import com.bistelapp.bistel.AppConfig;
import com.bistelapp.bistel.informations.driver.driver_info;
import com.bistelapp.bistel.utility.Requestor;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by tayo on 4/3/2016.
 */
public class RiderUtils {

    public static final String web_url = AppConfig.RIDER_WEB_URL+"FetchOnlineDrivers.php?status=online";
    public RiderUtils(){
    }

    public ArrayList<driver_info> loadDrivers(RequestQueue requestQueue){

        JSONArray response = Requestor.sendRequestDrivers(requestQueue, web_url);
        ArrayList<driver_info> driverInfos_lists = Parser.parseJSONResponse(response);
        return driverInfos_lists;
    }

}
