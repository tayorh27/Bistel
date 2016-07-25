package com.bistelapp.bistel.riders;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bistelapp.bistel.R;
import com.bistelapp.bistel.adapter.rider.PlaceArrayAdapter;
import com.bistelapp.bistel.async.rider.TaskUpdateVoucherStatus;
import com.bistelapp.bistel.callbacks.rider.LoadDistanceDuration;
import com.bistelapp.bistel.database.rider.UserLocalStorage;
import com.bistelapp.bistel.informations.rider.rider_info;
import com.bistelapp.bistel.internet.rider.BookingsRepo;
import com.bistelapp.bistel.internet.rider.GetDistanceDuration;
import com.bistelapp.bistel.utility.General;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.Calendar;

public class RequestActivity extends ActionBarActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LoadDistanceDuration {

    private int year, month, day;
    private Calendar calendar;
    private String setDate = "";

    private int hour, min;
    private String setTime = "";

    Button date, time, book_now, call_now;

    CardView cardView;
    TextView total_amt, tv_dis, tv_dur;
    General general;
    String mobile, name;
    int driver_id;
    UserLocalStorage userLocalStorage;
    SharedPreferences save_details;

    private static final String LOG_TAG = "RequestActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;
    private AutoCompleteTextView mAutocompleteTextView2;

    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(0, -0), new LatLng(0, -0));
    private GetDistanceDuration getDistanceDuration;

    private BookingsRepo bookingsRepo;
    private String getDistance, getDuration, getTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        save_details = getSharedPreferences("saveRequestDetails", 0);

        mGoogleApiClient = new GoogleApiClient.Builder(RequestActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id
                .autoCompleteTextView);
        mAutocompleteTextView.setThreshold(3);

        mAutocompleteTextView2 = (AutoCompleteTextView) findViewById(R.id
                .autoCompleteTextView2);
        mAutocompleteTextView2.setThreshold(3);

        calendar = Calendar.getInstance();
        general = new General(RequestActivity.this);
        userLocalStorage = new UserLocalStorage(RequestActivity.this);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //setDate = showDate(year, month + 1, day,false);

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);

        date = (Button) findViewById(R.id.date);
        time = (Button) findViewById(R.id.time);
        book_now = (Button) findViewById(R.id.book_now);
        call_now = (Button) findViewById(R.id.call_now);
        cardView = (CardView) findViewById(R.id.card);
        total_amt = (TextView) findViewById(R.id.tv_total_amount_price);
        tv_dis = (TextView) findViewById(R.id.tv_distance);
        tv_dur = (TextView) findViewById(R.id.tv_duration);
        //ampm = (Button) findViewById(R.id.ampm);


        date.setOnClickListener(this);
        time.setOnClickListener(this);
        book_now.setOnClickListener(this);
        call_now.setOnClickListener(this);
        //ampm.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        mobile = bundle.getString("mobile");
        driver_id = bundle.getInt("driver_id");

        getSupportActionBar().setSubtitle(name);

        rider_info ri = userLocalStorage.getRiderInfo();
        mAutocompleteTextView.setText(ri.current_location);

        mAutocompleteTextView.setOnItemClickListener(mAutoCompleteClickListener);
        mAutocompleteTextView2.setOnItemClickListener(mAutoCompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
        mAutocompleteTextView2.setAdapter(mPlaceArrayAdapter);

    }

    private AdapterView.OnItemClickListener mAutoCompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // if (id == R.id.autoCompleteTextView2) {
            distanceDuration();
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
            //}
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            String pickUp = mAutocompleteTextView.getText().toString();
            String dest = mAutocompleteTextView2.getText().toString();
            //distanceDuration();
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();
        }
    };

    private void distanceDuration() {
        String pickUp = mAutocompleteTextView.getText().toString();
        String dest = mAutocompleteTextView2.getText().toString();
        getDistanceDuration = new GetDistanceDuration(RequestActivity.this, pickUp, dest, this);
        getDistanceDuration.getDistanceDuration();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.book_now:
                book_ride_now();
                break;
            case R.id.call_now:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                call_driver();
                break;
        }
    }

    @RequiresPermission(Manifest.permission.CALL_PHONE)
    private void call_driver() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel: " + mobile));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    private void update_voucher_status() {
        rider_info ri = userLocalStorage.getRiderInfo();
        rider_info update = new rider_info(ri.id, ri.firstname, ri.lastname, ri.email, ri.mobile, ri.password, ri.current_location, "", "used", ri.playerID, ri.voucher_code_percent);
        userLocalStorage.storeUser(update);
        new TaskUpdateVoucherStatus(RequestActivity.this).execute();
    }

    private void update_percent() {
        rider_info ri = userLocalStorage.getRiderInfo();
        rider_info update = new rider_info(ri.id, ri.firstname, ri.lastname, ri.email, ri.mobile, ri.password, ri.current_location, "", "used", ri.playerID, 0);
        userLocalStorage.storeUser(update);
    }

    private void book_ride_now() {
        rider_info ri = userLocalStorage.getRiderInfo();
        String driver_name = name;
        String driver_number = mobile;
        String rider_name = ri.firstname + " " + ri.lastname;
        String rider_number = ri.mobile;
        int rider_id = ri.id;
        String pickUp = mAutocompleteTextView.getText().toString();
        String dest = mAutocompleteTextView2.getText().toString();
        String ride_type = "booking";

        if (pickUp.contentEquals("") || dest.contentEquals("")) {
            Toast.makeText(RequestActivity.this, "All fields must be filled", Toast.LENGTH_LONG).show();
        } else {
            // push to online
            update_percent();
            update_voucher_status();
            String booking_date_time = setDate + " " + setTime + "";
            bookingsRepo = new BookingsRepo(RequestActivity.this, driver_name, driver_number, driver_id, "",rider_name, rider_number, rider_id, pickUp, dest, getDistance, getDuration, booking_date_time, getTotalPrice, "", ride_type);
            //bookingsRepo.UploadRide("",ride_type);
        }
    }

    private void SaveBookingDetails() {
        String pickUp = mAutocompleteTextView.getText().toString();
        String dest = mAutocompleteTextView2.getText().toString();

        String _amt = total_amt.getText().toString();
        String dist = tv_dis.getText().toString();
        String dur = tv_dur.getText().toString();

        SharedPreferences.Editor editor = save_details.edit();
        editor.putString("pickup", pickUp);
        editor.putString("destination", dest);
        editor.putString("amount", _amt);
        editor.putString("distance", dist);
        editor.putString("duration", dur);
        editor.putBoolean("card", true);
        editor.apply();
    }

    private void RestoreBookingDetails() {
        String pickUp = save_details.getString("pickup", "");
        String dest = save_details.getString("destination", "");

        String _amt = save_details.getString("amount", "");
        String dist = save_details.getString("distance", "");
        String dur = save_details.getString("duration", "");
        boolean check_card_visibility = save_details.getBoolean("card", false);

        if (check_card_visibility) {
            cardView.setVisibility(View.VISIBLE);
        }

        if (!_amt.contentEquals("")) {
            mAutocompleteTextView.setText(pickUp);
            mAutocompleteTextView2.setText(dest);
            total_amt.setText(_amt);
            tv_dis.setText(dist);
            tv_dur.setText(dur);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        RestoreBookingDetails();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = save_details.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SaveBookingDetails();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");
        Toast.makeText(RequestActivity.this, "Connected", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onLoadDistanceDuration(String distance, String duration, int distance_value, int duration_value) {
        if (distance.contentEquals("") || duration.contentEquals("")) {
            getDistanceDuration.getDistanceDuration();
            general.displayAlertDialog("Place Error", "Please select a valid place from the auto complete");
        } else {
            cardView.setVisibility(View.VISIBLE);
            tv_dis.setText(distance);
            tv_dur.setText(duration);
            double g_distance = Double.parseDouble(general.return_subString(distance));
            double g_duration = Double.parseDouble(general.return_subString(duration));

            getDistance = distance;
            getDuration = duration;

            int use_duration = duration_value / 60;

            double total = (g_distance * 80) + (use_duration * 20) + 500;

            rider_info ri = userLocalStorage.getRiderInfo();
            if (ri.voucher_status.contentEquals("unused")) {
                Toast.makeText(RequestActivity.this, "You got 10% off as a new user", Toast.LENGTH_LONG).show();
                total = total - ((10 / 100) * total);
            }

            if (ri.voucher_code_percent > 0) {
                if(ri.voucher_code_percent <= 100) {
                    Toast.makeText(RequestActivity.this, "You got " + (int) ri.voucher_code_percent + "% off", Toast.LENGTH_LONG).show();
                    total = total - ((ri.voucher_code_percent / 100) * total);
                }else if(ri.voucher_code_percent > 100){
                    Toast.makeText(RequestActivity.this, "You got ₦" + (int) ri.voucher_code_percent + " off", Toast.LENGTH_LONG).show();
                    total = total - ri.voucher_code_percent;
                }
            }

            total_amt.setText("₦" + (int) Math.nextUp(total));
            getTotalPrice = "NGN" + (int) Math.nextUp(total);
        }
    }
}
