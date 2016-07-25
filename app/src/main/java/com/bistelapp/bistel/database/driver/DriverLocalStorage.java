package com.bistelapp.bistel.database.driver;

import android.content.Context;
import android.content.SharedPreferences;

import com.bistelapp.bistel.informations.driver.driver_info;

/**
 * Created by tayo on 3/31/2016.
 */
public class DriverLocalStorage {

    Context context;
    private static String KEY = "sp_name";
    private SharedPreferences localStore;

    public DriverLocalStorage(Context context){
        this.context = context;
        localStore = context.getSharedPreferences(KEY,0);
    }

    public void storeDriver(driver_info ri){
        SharedPreferences.Editor editor = localStore.edit();
        editor.putInt("id",ri.id);
        editor.putString("firstname",ri.firstname);
        editor.putString("lastname",ri.lastname);
        editor.putString("plate_number",ri.plate_number);
        editor.putString("mobile",ri.mobile);
        editor.putString("password",ri.password);
        editor.putString("image",ri.image);
        editor.putString("status",ri.status);
        editor.putString("current_location",ri.current_location);
        editor.putString("distance",ri.distance);
        editor.putString("playerID",ri.playerID);
        editor.apply();
    }

    public driver_info getDriverInfo(){
        int id = localStore.getInt("id",0);
        String firstname = localStore.getString("firstname","");
        String lastname = localStore.getString("lastname","");
        String email = localStore.getString("email","");
        String plate_number = localStore.getString("plate_number","");
        String mobile = localStore.getString("mobile","");
        String password = localStore.getString("password","");
        String image = localStore.getString("image","");
        String status = localStore.getString("status","");
        String current_location = localStore.getString("current_location","");
        String distance = localStore.getString("distance","");
        String playerID = localStore.getString("playerID","");
        driver_info ri = new driver_info(id,firstname,lastname,email,plate_number,mobile,password,image,status,current_location,distance,playerID);
        return ri;
    }

    public void setDriverLogged(boolean logged){
        SharedPreferences.Editor editor = localStore.edit();
        editor.putBoolean("logged",logged);
        editor.apply();
    }

    public boolean getLoggedDriver(){
        if(localStore.getBoolean("logged",false)){
            return true;
        }else {
            return false;
        }

    }

    public void clearDatabase(){
        SharedPreferences.Editor editor = localStore.edit();
        editor.clear();
        editor.apply();
    }
}
