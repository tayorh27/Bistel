package com.bistelapp.bistel.utility;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bistelapp.bistel.AppConfig;
import com.bistelapp.bistel.callbacks.DirectionFinderListener;
import com.bistelapp.bistel.database.Route;
import com.bistelapp.bistel.database.rider.LatitudeLongitude;
import com.bistelapp.bistel.network.VolleySingleton;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Control & Inst. LAB on 12-Sep-16.
 */
public class DirectionFinder {

    String origin, destination;
    Context context;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    General general;
    DirectionFinderListener onLoadDistanceDuration;
    LatitudeLongitude route;

    public DirectionFinder(Context context, String origin, String destination, DirectionFinderListener onLoadDistanceDuration) {
        this.origin = origin;
        this.destination = destination;
        this.context = context;
        this.onLoadDistanceDuration = onLoadDistanceDuration;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        general = new General(context);
    }

    public DirectionFinder(Context context, String origin, String destination) {
        this.origin = origin;
        this.destination = destination;
        this.context = context;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        general = new General(context);
    }

    public LatitudeLongitude getLatitudeLongitude(){
        String _url = AppConfig.GET_DISTANCE_AND_DURATION_DIRECTION + origin + "&destination=" + destination + "&key=" + AppConfig.API_KEY_FOR_DISTANCE_DURATION;
        String nUrl = _url.replace(" ", "%20");


        StringRequest stringRequest = new StringRequest(Request.Method.GET, nUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonData = new JSONObject(response);
                    String status = jsonData.getString("status");
                    JSONArray jsonRoutes = jsonData.getJSONArray("routes");
                    for (int i = 0; i < jsonRoutes.length(); i++) {
                        JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
                        JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
                        JSONObject jsonLeg = jsonLegs.getJSONObject(0);
                        JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");

                        route = new LatitudeLongitude(jsonStartLocation.getDouble("lat"), jsonStartLocation.getDouble("lng"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                general.dismissProgressDialog();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                general.dismissProgressDialog();
                General.handleVolleyError(error, context);
                //general.displayAlertDialog("distance duration error",error.toString());
            }
        });
        requestQueue.add(stringRequest);
        return route;

    }

    public void getDirections() {

        general.displayProgressDialog("Finding path routes...");

        String _url = AppConfig.GET_DISTANCE_AND_DURATION_DIRECTION + origin + "&destination=" + destination + "&key=" + AppConfig.API_KEY_FOR_DISTANCE_DURATION;
        String nUrl = _url.replace(" ", "%20");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, nUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonData = new JSONObject(response);
                    String status = jsonData.getString("status");
                    List<Route> routes = new ArrayList<>();
                    JSONArray jsonRoutes = jsonData.getJSONArray("routes");
                    for (int i = 0; i < jsonRoutes.length(); i++) {
                        JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
                        Route route = new Route();

                        JSONObject overview_polylineJson = jsonRoute.getJSONObject("overview_polyline");
                        JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
                        JSONObject jsonLeg = jsonLegs.getJSONObject(0);
                        JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
                        JSONObject jsonDuration = jsonLeg.getJSONObject("duration");
                        JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
                        JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");

                        route.distance = jsonDistance.getString("text");
                        route.duration = jsonDuration.getString("text");
                        route.endAddress = jsonLeg.getString("end_address");
                        route.startAddress = jsonLeg.getString("start_address");
                        route.startLocation = new LatLng(jsonStartLocation.getDouble("lat"), jsonStartLocation.getDouble("lng"));
                        route.endLocation = new LatLng(jsonEndLocation.getDouble("lat"), jsonEndLocation.getDouble("lng"));
                        route.points = decodePolyLine(overview_polylineJson.getString("points"));

                        routes.add(route);

                    }

                    if (onLoadDistanceDuration != null) {
                        onLoadDistanceDuration.LoadDistanceDuration(routes);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                general.dismissProgressDialog();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                general.dismissProgressDialog();
                General.handleVolleyError(error, context);
                //general.displayAlertDialog("distance duration error",error.toString());
            }
        });
        requestQueue.add(stringRequest);
    }

    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }


        return decoded;
    }
}
