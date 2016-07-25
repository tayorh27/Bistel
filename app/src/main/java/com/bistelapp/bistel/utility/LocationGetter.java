package com.bistelapp.bistel.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.bistelapp.bistel.GPSService;
import com.bistelapp.bistel.callbacks.rider.LoadAddressLocation;
import com.bistelapp.bistel.internet.driver.UpdateLocation;
import com.bistelapp.bistel.internet.rider.GetLocationFromServer;

/**
 * Created by Control & Inst. LAB on 09-Jun-16.
 */
public class LocationGetter implements LoadAddressLocation {

    Context context;
    GPSService mGPSService;
    GetLocationFromServer getLocationFromServer;
    AlertDialog alertDialog;
    public LocationGetter(Context context){
        this.context = context;
        alertDialog = new AlertDialog.Builder(context).create();
    }

    public void runOnStart() {
        String address = "";
        mGPSService.getLocation();

        if (mGPSService.isLocationAvailable == false) {

            // Here you can ask the user to try again, using return; for that
            //Toast.makeText(this, "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
            alertDialog.setTitle("Location error");
            alertDialog.setMessage("Your location is not available, please try again.");
            alertDialog.setButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    runOnStart();
                }
            });
            alertDialog.show();
            return;

            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
            // address = "Location not available";
        } else {

            // Getting location co-ordinates
            double latitude = mGPSService.getLatitude();
            double longitude = mGPSService.getLongitude();
            //Toast.makeText(this, "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();

            //address = mGPSService.getLocationAddress();
            //address = mGPSService.getLocation_(latitude,longitude);

            getLocationFromServer = new GetLocationFromServer(context,latitude,longitude,this);
            getLocationFromServer.UpdateLocation();
            //address = getLocationFromServer.getLocation();

        }

        //Toast.makeText(RiderActivity.this, "Your address is: " + address, Toast.LENGTH_LONG).show();

        // make sure you close the gps after using it. Save user's battery power
        mGPSService.closeGPS();

    }

    @Override
    public void onLoadAddressLocation(String location) {
        if(location.contentEquals("")){
            getLocationFromServer.UpdateLocation();
        }else {
            UpdateLocation updateLocation = new UpdateLocation(context, location);
            updateLocation.update_rider_location();
        }
    }
}
