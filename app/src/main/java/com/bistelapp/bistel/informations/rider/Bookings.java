package com.bistelapp.bistel.informations.rider;

/**
 * Created by Control & Inst. LAB on 20-Sep-16.
 */
public class Bookings {

    public int id;
    public String get_id;
    public String driver_name;
    public String driver_number;
    public String driver_player_id;
    public String plateNumber;
    public String pickUp;
    public String destination;
    public String distance;
    public String time;
    public String amount;
    public String payment_type;
    public String booked_date;

    public Bookings() {

    }

    public Bookings(String get_id
            , String driver_name
            , String driver_number
            , String driver_player_id
            , String plateNumber
            , String pickUp
            , String destination
            , String distance
            , String time
            , String amount
            , String payment_type
            , String booked_date) {

        this.get_id = get_id;
        this.driver_name = driver_name;
        this.driver_number = driver_number;
        this.driver_player_id = driver_player_id;
        this.plateNumber = plateNumber;
        this.pickUp = pickUp;
        this.destination = destination;
        this.distance = distance;
        this.time = time;
        this.amount = amount;
        this.payment_type =payment_type;
        this.booked_date = booked_date;

    }
}
