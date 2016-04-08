package com.bistelapp.bistel.json.rider;

import android.net.Uri;

import com.bistelapp.bistel.AppConfig;
import com.bistelapp.bistel.informations.rider.online_driver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tayo on 4/2/2016.
 */
public class Parser {

    public static ArrayList<online_driver> parseJSONResponse(JSONArray jsonArray) {

        ArrayList<online_driver> customData = new ArrayList<>();

        String serverUrl = AppConfig.RIDER_WEB_URL + "images/";
        List<String> customUsername = new ArrayList<>();
        List<String> customMobile = new ArrayList<>();
        List<String> customImage = new ArrayList<>();
        List<String> customCategory = new ArrayList<>();
        List<String> customId = new ArrayList<>();

        String[] uses, mobs, imgs, cats, ids;

        if (jsonArray != null && jsonArray.length() > 0) {

            for (int i = 0; i < jsonArray.length(); i++) {

                try {
                    JSONObject json = jsonArray.getJSONObject(i);

                    customId.add(json.getString("id"));
                    customUsername.add(json.getString("username"));
                    customMobile.add(json.getString("mobile"));
                    customImage.add(json.getString("image"));
                    customCategory.add(json.getString("category"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            uses = new String[jsonArray.length()];
            mobs = new String[jsonArray.length()];
            imgs = new String[jsonArray.length()];
            cats = new String[jsonArray.length()];
            ids = new String[jsonArray.length()];

            for (int j = 0; j < customUsername.size(); j++) {

                uses[j] = customUsername.get(j);
                mobs[j] = customMobile.get(j);
                imgs[j] = customImage.get(j);
                cats[j] = customCategory.get(j);
                ids[j] = customId.get(j);
            }
            for (int i = 0; i < mobs.length; i++) {

                String get_id = ids[i];
                String get_username = uses[i];
                String get_mobile = mobs[i];
                String get_image = serverUrl + Uri.encode(imgs[i]);
                String get_category = cats[i];

                online_driver current = new online_driver(get_id, get_username, get_mobile, get_category, get_image);
                customData.add(current);

            }

        } else {
            //customData = MyApplication.getWriteableDatabase().getAllFavorites();
        }

        return customData;
    }

}
