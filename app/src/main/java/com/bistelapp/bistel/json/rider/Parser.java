package com.bistelapp.bistel.json.rider;

import android.net.Uri;
import android.widget.Toast;

import com.bistelapp.bistel.AppConfig;
import com.bistelapp.bistel.informations.driver.driver_info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tayo on 4/2/2016.
 */
public class Parser {

    public static ArrayList<driver_info> parseJSONResponse(JSONArray jsonArray) {

        ArrayList<driver_info> customData = new ArrayList<>();

        if (jsonArray != null && jsonArray.length() > 0) {

            for (int i = 0; i < jsonArray.length(); i++) {

                try {
                    JSONObject json = jsonArray.getJSONObject(i);

                    int id = json.getInt("id");
                    String firstname = json.getString("first_name");
                    String lastname = json.getString("last_name");
                    String email = json.getString("email");
                    String plate_number = json.getString("plate_number");
                    String mobile = json.getString("mobile");
                    String image = json.getString("image");
                    String current_location = json.getString("current_location");

                    driver_info current = new driver_info(id,firstname,lastname,email,plate_number,mobile,"",image,"",current_location,"");
                    customData.add(current);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {

        }

        return customData;
    }

}
