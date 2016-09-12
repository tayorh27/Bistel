package com.bistelapp.bistel.database;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Control & Inst. LAB on 12-Sep-16.
 */
public class Route {

    public String distance,duration,endAddress,startAddress;
    public LatLng startLocation,endLocation;
    public List<LatLng> points;
}
