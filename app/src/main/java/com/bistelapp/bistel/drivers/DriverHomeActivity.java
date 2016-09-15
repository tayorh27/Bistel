package com.bistelapp.bistel.drivers;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bistelapp.bistel.GPSService;
import com.bistelapp.bistel.R;
import com.bistelapp.bistel.callbacks.rider.LoadAddressLocation;
import com.bistelapp.bistel.internet.driver.UpdateLocation;
import com.bistelapp.bistel.internet.driver.UpdateStatus;
import com.bistelapp.bistel.internet.rider.GetLocationFromServer;

public class DriverHomeActivity extends ActionBarActivity implements LoadAddressLocation {

    Button setStatus;
    private UpdateStatus updateStatus;
    GPSService mGPSService;
    ProgressBar progressBar;
    private GetLocationFromServer getLocationFromServer;
    double latitude,longitude;
    RelativeLayout driver_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        driver_root = (RelativeLayout)findViewById(R.id.driver_root);

        setStatus = (Button)findViewById(R.id.status);
        updateStatus = new UpdateStatus(DriverHomeActivity.this,setStatus);

        runOnStart();

        //new UpdateLocationTask(DriverHomeActivity.this).execute();
        //LocationGetter locationGetter = new LocationGetter(DriverHomeActivity.this);
        //locationGetter.runOnStart();
    }

    private void runOnStart() {
        String address = "";
        progressBar.setVisibility(View.VISIBLE);
        mGPSService.getLocation();

        if (!mGPSService.isLocationAvailable) {

            // Here you can ask the user to try again, using return; for that
            Snackbar.make(driver_root, "Your location is not available, please try again.",Snackbar.LENGTH_LONG).setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    runOnStart();
                }
            }).show();
            //Toast.makeText(this, "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;

            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
            // address = "Location not available";
        } else {

            // Getting location co-ordinates
            latitude = mGPSService.getLatitude();
            longitude = mGPSService.getLongitude();
            Toast.makeText(this, "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();
            //userLocalStorage.setLatLng(latitude,longitude);

            //address = mGPSService.getLocationAddress();
            //address = mGPSService.getLocation_(latitude,longitude);

            getLocationFromServer = new GetLocationFromServer(DriverHomeActivity.this,latitude,longitude,this);
            getLocationFromServer.UpdateLocation();
            //address = getLocationFromServer.getLocation();


            progressBar.setVisibility(View.GONE);

        }

        //Toast.makeText(RiderActivity.this, "Your address is: " + address, Toast.LENGTH_LONG).show();

        // make sure you close the gps after using it. Save user's battery power
        mGPSService.closeGPS();
        progressBar.setVisibility(View.GONE);

    }

    public void DriverStatus(View view){
        updateStatus.updateDriverStatus();
    }

    @Override
    public void onLoadAddressLocation(String location) {
        if(location.contentEquals("")){
            getLocationFromServer.UpdateLocation();
        }else {
            UpdateLocation updateLocation = new UpdateLocation(DriverHomeActivity.this, location+ "|" + latitude + "," + longitude);
            updateLocation.update_rider_location();
        }
    }
}
