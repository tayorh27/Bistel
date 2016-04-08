package com.bistelapp.bistel.database.rider;

import android.content.Context;
import android.content.SharedPreferences;

import com.bistelapp.bistel.informations.rider.rider_info;

/**
 * Created by tayo on 3/29/2016.
 */
public class UserLocalStorage {

    Context context;
    private static String KEY = "sp_name";
    private SharedPreferences localStore;

    public UserLocalStorage(Context context){
        this.context = context;
        localStore = context.getSharedPreferences(KEY,0);
    }

    public void storeUser(rider_info ri){
        SharedPreferences.Editor editor = localStore.edit();
        editor.putInt("id",ri.id);
        editor.putString("firstname",ri.firstname);
        editor.putString("lastname",ri.lastname);
        editor.putString("email",ri.email);
        editor.putString("mobile",ri.mobile);
        editor.putString("password",ri.password);
        editor.putString("current_location",ri.current_location);
        editor.apply();
    }

    public rider_info getRiderInfo(){
        int id = localStore.getInt("id",0);
        String firstname = localStore.getString("firstname","");
        String lastname = localStore.getString("lastname","");
        String email = localStore.getString("email","");
        String mobile = localStore.getString("mobile","");
        String password = localStore.getString("password","");
        String current_location = localStore.getString("current_location","");
        rider_info ri = new rider_info(id,firstname,lastname,email,mobile,password,current_location);
        return ri;
    }

    public void setUserLogged(boolean logged){
        SharedPreferences.Editor editor = localStore.edit();
        editor.putBoolean("logged",logged);
        editor.apply();
    }

    public boolean getLoggedUser(){
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
