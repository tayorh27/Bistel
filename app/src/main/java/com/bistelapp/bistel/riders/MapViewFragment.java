package com.bistelapp.bistel.riders;


import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bistelapp.bistel.R;
import com.bistelapp.bistel.callbacks.rider.LoadOnlineDrivers;
import com.bistelapp.bistel.database.rider.LatitudeLongitude;
import com.bistelapp.bistel.informations.driver.driver_info;
import com.bistelapp.bistel.internet.rider.FetchOnlineDrivers;
import com.bistelapp.bistel.utility.DirectionFinder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * A fragment with a Google +1 button.
 */
public class MapViewFragment extends Fragment implements OnMapReadyCallback, LoadOnlineDrivers {

    private GoogleMap mMap;
    private ArrayList<driver_info> customData = new ArrayList<>();
    private FetchOnlineDrivers fetchOnlineDrivers;
    private DirectionFinder directionFinder;


    public MapViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_view, container, false);
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fetchOnlineDrivers = new FetchOnlineDrivers(getActivity(), this);
        fetchOnlineDrivers.OnlineDrivers();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        for (int i = 0; i < list.size(); i++){
            String name = list.get(i).lastname + " " + list.get(i).firstname;
            directionFinder = new DirectionFinder(getActivity(),list.get(i).current_location,"lekki phase1");
            LatitudeLongitude latitudeLongitude = directionFinder.getLatitudeLongitude();
            LatLng newLatLng = new LatLng(latitudeLongitude.latitude,latitudeLongitude.longitude);
            if(list.get(i).status.contentEquals("online")) {
                mMap.addMarker(new MarkerOptions().position(newLatLng).title(name+" (online)").snippet(list.get(i).current_location).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            }else if(list.get(i).status.contentEquals("offline")){
                mMap.addMarker(new MarkerOptions().position(newLatLng).title(name+" (offline)").snippet(list.get(i).current_location).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 18));
        }
    }
}
