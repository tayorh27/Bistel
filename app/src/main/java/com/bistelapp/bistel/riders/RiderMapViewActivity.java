package com.bistelapp.bistel.riders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bistelapp.bistel.R;
import com.bistelapp.bistel.callbacks.rider.LoadOnlineDrivers;
import com.bistelapp.bistel.informations.driver.driver_info;
import com.bistelapp.bistel.internet.rider.FetchOnlineDrivers;
import com.bistelapp.bistel.utility.DirectionFinder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class RiderMapViewActivity extends ActionBarActivity implements OnMapReadyCallback, LoadOnlineDrivers {

    private GoogleMap mMap;
    private FetchOnlineDrivers fetchOnlineDrivers;
    private DirectionFinder directionFinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_map_view);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fetchOnlineDrivers = new FetchOnlineDrivers(RiderMapViewActivity.this, this);
        fetchOnlineDrivers.OnlineDrivers();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(RiderMapViewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RiderMapViewActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onLoadOnlineDrivers(ArrayList<driver_info> list) {
        for (int i = 0; i < list.size(); i++) {
            String name = list.get(i).lastname + " " + list.get(i).firstname;
            String latlng = list.get(i).current_location.substring(list.get(i).current_location.indexOf("|") + 1);
            Log.e("LatLng Details" + i, latlng);
            String[] nL = latlng.split(",");
            //directionFinder = new DirectionFinder(RiderMapViewActivity.this,list.get(i).current_location,"lekki phase1");
            //LatitudeLongitude latitudeLongitude = directionFinder.getLatitudeLongitude();
            LatLng newLatLng = new LatLng(Double.parseDouble(nL[0]), Double.parseDouble(nL[1]));
            if (list.get(i).status.contentEquals("online")) {
                mMap.addMarker(new MarkerOptions().position(newLatLng).title(name + " (online)").snippet(list.get(i).current_location.substring(0, list.get(i).current_location.indexOf("|"))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            } else if (list.get(i).status.contentEquals("offline")) {
                mMap.addMarker(new MarkerOptions().position(newLatLng).title(name + " (offline)").snippet(list.get(i).current_location.substring(0, list.get(i).current_location.indexOf("|"))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 18));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rider_map, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RiderMapViewActivity.this, RiderActivity.class);
        startActivity(intent);
    }
}
