package com.bistelapp.bistel.informations.driver;

/**
 * Created by tayo on 3/31/2016.
 */
public class driver_info {

    public int id;
    public String firstname, lastname,email, plate_number, mobile, password,image,status,current_location,distance;

    public driver_info(int id, String firstname, String lastname,String email, String plate_number, String mobile, String password, String image,String status,String current_location,String distance){
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.plate_number = plate_number;
        this.mobile = mobile;
        this.password = password;
        this.image = image;
        this.status = status;
        this.current_location = current_location;
        this.distance = distance;
    }
}
