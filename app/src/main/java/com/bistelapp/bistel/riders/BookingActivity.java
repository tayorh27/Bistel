package com.bistelapp.bistel.riders;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bistelapp.bistel.R;
import com.bistelapp.bistel.adapter.rider.PlaceArrayAdapter;
import com.bistelapp.bistel.callbacks.rider.LoadDistanceDuration;
import com.bistelapp.bistel.database.rider.UserLocalStorage;
import com.bistelapp.bistel.informations.rider.rider_info;
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

public class BookingActivity extends ActionBarActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LoadDistanceDuration {

    private int year, month, day;
    private Calendar calendar;
    private String setDate = "";

    private int hour, min;
    private String setTime = "";

    Button date, time, book_now,call_now;

    CardView cardView;
    TextView total_amt, tv_dis, tv_dur;
    General general;
    String mobile, name;
    UserLocalStorage userLocalStorage;
    SharedPreferences save_details;

    private static final String LOG_TAG = "BookingActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;
    private AutoCompleteTextView mAutocompleteTextView2;

    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(0, -0), new LatLng(0, -0));
    private GetDistanceDuration getDistanceDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        save_details = getSharedPreferences("saveBookingDetails",0);

        mGoogleApiClient = new GoogleApiClient.Builder(BookingActivity.this)
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
        general = new General(BookingActivity.this);
        userLocalStorage = new UserLocalStorage(BookingActivity.this);

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
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
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
            distanceDuration(pickUp, dest);
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

//            String text = Html.fromHtml(place.getName() + "\n").toString();
//            text += Html.fromHtml(place.getAddress() + "\n").toString();
//            text += Html.fromHtml(place.getId() + "\n").toString();
//            text += Html.fromHtml(place.getPhoneNumber() + "\n").toString();
//            text += place.getWebsiteUri() + "\n";
//            if (attributions != null) {
//                text += Html.fromHtml(attributions.toString());
//            }
//
//            book_now.setText(text);
        }
    };

    private void distanceDuration(String origin, String destination) {
        getDistanceDuration = new GetDistanceDuration(BookingActivity.this, origin, destination, this);
        getDistanceDuration.getDistanceDuration();
    }

    private String showDate(int year, int month, int day, boolean show_) {
        String _setDate = String.valueOf(new StringBuilder().append(day).append("/")
                .append(month + 1).append("/").append(year));
        if (show_) {
            date.setText(_setDate);
        }
        setDate = _setDate;

        return setDate;
    }

    private String showTime(int hour, int min, boolean show_) {
        String _setTime = String.valueOf(new StringBuilder().append(hour).append(":")
                .append(min));
        if (show_) {
            time.setText(_setTime);
        }
        setTime = _setTime;

        return setTime;
    }

    private void popupDate() {
        showDialog(999);
    }

    private void popupTime() {
        showDialog(888);
    }

    private DatePickerDialog.OnDateSetListener mDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            showDate(year, monthOfYear, dayOfMonth, true);
        }
    };

    private TimePickerDialog.OnTimeSetListener mTime = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            showTime(hourOfDay, minute, true);
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this, mDate, year, month, day);
        }
        if (id == 888) {
            return new TimePickerDialog(this, mTime, hour, min, false);
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_booking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date:
                popupDate();
                break;
            case R.id.time:
                popupTime();
                break;
            case R.id.book_now:
                book_ride_now();
                break;
            case R.id.call_now:
                call_driver();
                break;
        }
    }

    private void call_driver() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel: " + mobile));
        startActivity(intent);
    }

    private void book_ride_now() {

        String pickUp = mAutocompleteTextView.getText().toString();
        String dest = mAutocompleteTextView2.getText().toString();

        if (pickUp.contentEquals("") || dest.contentEquals("")) {
            Toast.makeText(BookingActivity.this, "All fields must be filled", Toast.LENGTH_LONG).show();
        } else {
            if (setDate.contentEquals("") || setTime.contentEquals("")) {
                general.displayAlertDialog("DateTime Error", "please select date and time");
            } else {
                // push to driver
                String booking_date_time = setDate + " " + setTime + "";
                //Toast.makeText(this, booking_date_time, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void SaveBookingDetails(){
        String pickUp = mAutocompleteTextView.getText().toString();
        String dest = mAutocompleteTextView2.getText().toString();

        String _amt = total_amt.getText().toString();
        String dist = tv_dis.getText().toString();
        String dur = tv_dur.getText().toString();

        SharedPreferences.Editor editor = save_details.edit();
        editor.putString("pickup",pickUp);
        editor.putString("destination",dest);
        editor.putString("amount",_amt);
        editor.putString("distance",dist);
        editor.putString("duration",dur);
        editor.putBoolean("card",true);
        editor.apply();
    }

    private void RestoreBookingDetails(){
        String pickUp = save_details.getString("pickup","");
        String dest = save_details.getString("destination","");

        String _amt = save_details.getString("amount","");
        String dist = save_details.getString("distance","");
        String dur = save_details.getString("duration","");
        boolean check_card_visibility = save_details.getBoolean("card",false);

        if(check_card_visibility){
            cardView.setVisibility(View.VISIBLE);
        }

        if (!_amt.contentEquals("")){
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
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");
        Toast.makeText(BookingActivity.this, "Connected", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }



    private void showTimeDialog() {
        final Dialog dialog = new Dialog(BookingActivity.this);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View custom_view = inflater1.inflate(R.layout.custom_dialog, (ViewGroup) findViewById(R.id.root), false);

        dialog.setContentView(custom_view);
        dialog.show();
    }

    @Override
    public void onLoadDistanceDuration(String distance, String duration) {
        if (distance.contentEquals("") || duration.contentEquals("")) {
            getDistanceDuration.getDistanceDuration();
            general.displayAlertDialog("Place Error", "Please select a valid place from the auto complete");
        } else {
            cardView.setVisibility(View.VISIBLE);
            tv_dis.setText(distance+" Km");
            tv_dur.setText(duration+ "mins");
            double g_distance = Double.parseDouble(general.return_subString(distance));
            double g_duration = Double.parseDouble(general.return_subString(duration));

            double total = (g_distance * 80) + (g_duration * 20);
            total_amt.setText("₦" + total+".00K");
        }
    }
}
