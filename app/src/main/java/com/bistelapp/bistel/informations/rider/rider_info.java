package com.bistelapp.bistel.informations.rider;

/**
 * Created by tayo on 3/28/2016.
 */
public class rider_info {

    public int id;
    public String firstname, lastname, email, mobile, password,current_location;

    public rider_info(int id, String firstname, String lastname, String email, String mobile, String password, String current_location){
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.current_location=current_location;
    }
}
