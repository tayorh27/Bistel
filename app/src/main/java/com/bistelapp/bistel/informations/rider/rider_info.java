package com.bistelapp.bistel.informations.rider;

/**
 * Created by tayo on 3/28/2016.
 */
public class rider_info {

    public int id;
    public float voucher_code_percent;
    public String firstname, lastname, email, mobile, password,current_location,voucher,voucher_status,playerID;

    public rider_info(int id, String firstname, String lastname, String email, String mobile, String password, String current_location
    ,String voucher,String voucher_status,String playerID,float voucher_code_percent){
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.current_location=current_location;
        this.voucher = voucher;
        this.voucher_status = voucher_status;
        this.playerID = playerID;
        this.voucher_code_percent = voucher_code_percent;
    }
}
