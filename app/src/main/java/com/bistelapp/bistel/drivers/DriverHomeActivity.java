package com.bistelapp.bistel.drivers;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.bistelapp.bistel.R;
import com.bistelapp.bistel.internet.driver.UpdateStatus;
import com.bistelapp.bistel.utility.LocationGetter;

public class DriverHomeActivity extends ActionBarActivity {

    Button setStatus;
    private UpdateStatus updateStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);

        setStatus = (Button)findViewById(R.id.status);
        updateStatus = new UpdateStatus(DriverHomeActivity.this,setStatus);

        //new UpdateLocationTask(DriverHomeActivity.this).execute();
        LocationGetter locationGetter = new LocationGetter(DriverHomeActivity.this);
        locationGetter.runOnStart();
    }

    public void DriverStatus(View view){
        updateStatus.updateDriverStatus();
    }
}
