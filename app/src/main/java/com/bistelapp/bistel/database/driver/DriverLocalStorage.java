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
        editor.putString("firstname",ri.firstname);
        editor.putString("lastname",ri.lastname);
        editor.putString("plate_number",ri.plate_number);
        editor.putString("mobile",ri.mobile);
        editor.putString("password",ri.password);
        editor.apply();
    }

    public driver_info getDriverInfo(){
        String firstname = localStore.getString("firstname","");
        String lastname = localStore.getString("lastname","");
        String plate_number = localStore.getString("plate_number","");
        String mobile = localStore.getString("mobile","");
        String password = localStore.getString("password","");
        driver_info ri = new driver_info(firstname,lastname,plate_number,mobile,password);
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
