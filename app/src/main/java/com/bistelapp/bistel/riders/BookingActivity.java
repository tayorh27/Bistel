package com.bistelapp.bistel.riders;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

public class BookingActivity extends ActionBarActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LoadDistanceDuration {

    private int year, month, day;
    private int _year, _month, _day;
    private Calendar calendar;
    private String setDate = "";

    private int hour, min;
    private String setTime = "";

    Button date, time, book_now, call_now;

    CardView cardView;
    TextView total_amt, tv_dis, tv_dur;
    General general;
    String mobile, name, playerID, plateNumber;
    int driver_id;
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

    private BookingsRepo bookingsRepo;
    private String getDistance, getDuration, getTotalPrice;
    Spinner payment;
    EditText negotiation;
    private String vouchers = "";
    private String message = "";
    String getLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        save_details = getSharedPreferences("saveBookingDetails", 0);

        mGoogleApiClient = new GoogleApiClient.Builder(BookingActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        payment = (Spinner) findViewById(R.id.payment_type);
        negotiation = (EditText) findViewById(R.id.negotiated);
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
        driver_id = bundle.getInt("driver_id");
        playerID = bundle.getString("driver_player_id");
        plateNumber = bundle.getString("driver_plate_number");

        getSupportActionBar().setSubtitle(name);

        rider_info ri = userLocalStorage.getRiderInfo();
        mAutocompleteTextView.setText(ri.current_location);

        //mAutocompleteTextView.setOnItemClickListener(mAutoCompleteClickListener);
        mAutocompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupCustomDialog();
            }
        });
        mAutocompleteTextView2.setOnItemClickListener(mAutoCompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
       // mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
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

    private void distanceDuration() {
        String pickUp = mAutocompleteTextView.getText().toString();
        String dest = mAutocompleteTextView2.getText().toString();
        getDistanceDuration = new GetDistanceDuration(BookingActivity.this, pickUp, dest, this);
        getDistanceDuration.getDistanceDuration();
    }

    private String showDate(int year, int month, int day, boolean show_) {
        String _setDate = String.valueOf(new StringBuilder().append(day).append("/")
                .append(month + 1).append("/").append(year));
        if (show_) {
            date.setText(_setDate);
        }
        _year = year;
        _month = month;
        _day = day;
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
            Calendar c = Calendar.getInstance();
            if(year < c.get(Calendar.YEAR) || monthOfYear <  c.get(Calendar.MONTH) || dayOfMonth < c.get(Calendar.DAY_OF_MONTH)){
                general.displayAlertDialog("Date Error", "Please select a future date");
            }else {
                showDate(year, monthOfYear, dayOfMonth, true);
            }
        }
    };

    private TimePickerDialog.OnTimeSetListener mTime = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar c = Calendar.getInstance();
            if(!setDate.isEmpty()) {
                if(_year == c.get(Calendar.YEAR) && _month ==  c.get(Calendar.MONTH) && _day == c.get(Calendar.DAY_OF_MONTH)){
                    if(hourOfDay <= c.get(Calendar.HOUR_OF_DAY) || minute <=  c.get(Calendar.MINUTE)){
                        general.displayAlertDialog("Date Error", "Please select a future time");
                    }else {
                        showTime(hourOfDay, minute, true);
                    }
                }else {
                    showTime(hourOfDay, minute, true);
                }
            }else {
                general.displayAlertDialog("DateTime Error", "Please select date first");
            }
        }
    };

    public void popupCustomDialog(){
        final Dialog dialog = new Dialog(BookingActivity.this);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View custom_view = inflater1.inflate(R.layout.custom_pickup_location, (ViewGroup) findViewById(R.id.custom_pickup), false);
        final AutoCompleteTextView pick = (AutoCompleteTextView) custom_view.findViewById(R.id.autoCompleteTextView1);
        //final Button cn = (Button)custom_view.findViewById(R.id.btn_cancel);
        //final Button pk = (Button)custom_view.findViewById(R.id.btn_pickup);
        pick.setThreshold(3);
        pick.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getLocation = pick.getText().toString();
                mAutocompleteTextView.setText(getLocation);
                dialog.dismiss();
            }
        });
        pick.setAdapter(mPlaceArrayAdapter);
        dialog.setContentView(custom_view);
        dialog.show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this, mDate, year, month, day);
        }
        if (id == 888) {
            return new TimePickerDialog(this, mTime, hour, min, true);
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
        new TaskUpdateVoucherStatus(BookingActivity.this).execute();
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
        String negotiate = "NGN" + negotiation.getText().toString();
        int normal_accepted = 0;
        if (negotiation.getText().length() == 0) {
            negotiate = getTotalPrice;
        }
        String payment_type = payment.getSelectedItem().toString();
        String ride_type = "booking";

        if (negotiation.getText().length() > 0) {
            int get_negotiated = Integer.parseInt(negotiation.getText().toString());
            int get_total = Integer.parseInt(getTotalPrice.substring(3));
            normal_accepted = get_total - get_negotiated;
        }

        if (pickUp.contentEquals("") || dest.contentEquals("")) {
            Toast.makeText(BookingActivity.this, "All fields must be filled", Toast.LENGTH_LONG).show();
        } else {
            if (setDate.contentEquals("") || setTime.contentEquals("")) {
                general.displayAlertDialog("DateTime Error", "please select date and time");
            } else if (normal_accepted > 1000) {
                general.displayAlertDialog("Booking Details", "Sorry negotiated price cannot be slashed more than ₦1000.");
            } else {
                // push to online
//                update_percent();
//                update_voucher_status();
                String booking_date_time = setDate + " " + setTime + "";
                if (vouchers.isEmpty()) {
                    vouchers = "none";
                }
                bookingsRepo = new BookingsRepo(BookingActivity.this, driver_name, driver_number, driver_id, plateNumber, rider_name, rider_number, rider_id, pickUp, dest, getDistance, getDuration, booking_date_time, getTotalPrice, negotiate, ride_type);
                bookingsRepo.UploadRide(payment_type, ride_type, save_details, vouchers, "2080556c-8ac9-48bd-b087-b4689b5379c7", message);
                //testing playerID

            }
        }
    }


    private void SaveBookingDetails() {
        String pickUp = mAutocompleteTextView.getText().toString();
        String dest = mAutocompleteTextView2.getText().toString();

        String _amt = total_amt.getText().toString();
        String dist = tv_dis.getText().toString();
        String dur = tv_dur.getText().toString();

        String gDate = date.getText().toString();
        String gTime = time.getText().toString();
        String n = negotiation.getText().toString();

        SharedPreferences.Editor editor = save_details.edit();
        editor.putString("pickup", pickUp);
        editor.putString("destination", dest);
        editor.putString("amount", _amt);
        editor.putString("distance", dist);
        editor.putString("duration", dur);
        editor.putString("date", gDate);
        editor.putString("time", gTime);
        editor.putString("n", n);
        editor.putBoolean("card", true);
        editor.apply();
    }

    private void RestoreBookingDetails() {
        String pickUp = save_details.getString("pickup", "");
        String dest = save_details.getString("destination", "");

        String _amt = save_details.getString("amount", "");
        String dist = save_details.getString("distance", "");
        String dur = save_details.getString("duration", "");
        String gDate = save_details.getString("date", "");
        String gTime = save_details.getString("time", "");
        String n = save_details.getString("n", "");
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
            date.setText(gDate);
            setDate = gDate;
            time.setText(gTime);
            setTime = gTime;
            negotiation.setText(n);
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
    public void onLoadDistanceDuration(String distance, String duration, int distance_value, int duration_value) {
        if (distance.contentEquals("") || duration.contentEquals("")) {
            getDistanceDuration.getDistanceDuration();
            general.displayAlertDialog("Place Error", "Please select a valid place from the auto complete");
        } else {
            cardView.setVisibility(View.VISIBLE);
            tv_dis.setText(distance);
            tv_dur.setText(duration);
            double g_distance = Double.parseDouble(general.return_subString(distance.replace(",", "")));
            double g_duration = Double.parseDouble(general.return_subString(duration));

            getDistance = distance;
            getDuration = duration;

            int use_duration = duration_value / 60;

            double total = (g_distance * 80) + (use_duration * 20) + 500;

            rider_info ri = userLocalStorage.getRiderInfo();
            if (ri.voucher_status.contentEquals("unused")) {
                Toast.makeText(BookingActivity.this, "You got 10% off as a new user", Toast.LENGTH_LONG).show();
                total = total - ((10 / 100) * total);
                vouchers += "You got 10% off as a new user_";
            }
            if (ri.voucher_code_percent > 0) {
                if (ri.voucher_code_percent <= 100) {
                    Toast.makeText(BookingActivity.this, "You got " + (int) ri.voucher_code_percent + "% off", Toast.LENGTH_LONG).show();
                    vouchers += "You got " + (int) ri.voucher_code_percent + "% off";
                    total = total - ((ri.voucher_code_percent / 100) * total);
                } else if (ri.voucher_code_percent > 100) {
                    Toast.makeText(BookingActivity.this, "You got ₦" + (int) ri.voucher_code_percent + " off", Toast.LENGTH_LONG).show();
                    vouchers += "You got ₦" + (int) ri.voucher_code_percent + " off";
                    total = total - ri.voucher_code_percent;
                }
            }

            if (total < 0) {
                total = 0;
            }

            total_amt.setText("₦" + general.totalAmount(total));
            getTotalPrice = "NGN" + general.totalAmount(total);
            message += ri.firstname + " " + ri.lastname + " just booked a ride.\nClick to view details";
        }
    }
}
