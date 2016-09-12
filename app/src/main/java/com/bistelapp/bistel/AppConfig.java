package com.bistelapp.bistel;

/**
 * Created by tayo on 3/30/2016.
 */
public class AppConfig {

    public static String DRIVER_WEB_URL = "http://www.bistelint.com.ng/bistelapp/driver/";
    public static String RIDER_WEB_URL = "http://www.bistelint.com.ng/bistelapp/rider/";
    public static String OneSignalPush = "https://onesignal.com/api/v1/notifications";
    public static String OneSignalPushAppID = "e10f0802-3cd4-4466-8281-5ff5d7051743";
    public static String API_KEY_FOR_DISTANCE_DURATION = "AIzaSyCp0vrFKhLhFcq8lyc2Z6Zf6UFeZnsZcnY";

//FetchRiderHistory.php include driver email and rider email
    public static String GET_LOCATION_FROM_SERVER = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
    public static String GET_DISTANCE_AND_DURATION = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=";
    public static String GET_DISTANCE_AND_DURATION_DIRECTION = "https://maps.googleapis.com/maps/api/directions/json?origin=";
}



