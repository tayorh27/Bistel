package com.bistelapp.bistel.riders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bistelapp.bistel.GPSService;
import com.bistelapp.bistel.R;
import com.bistelapp.bistel.adapter.rider.RiderAdapter;
import com.bistelapp.bistel.callbacks.rider.LoadAddressLocation;
import com.bistelapp.bistel.callbacks.rider.LoadDistanceDuration;
import com.bistelapp.bistel.callbacks.rider.LoadOnlineDrivers;
import com.bistelapp.bistel.callbacks.rider.OnClickListener;
import com.bistelapp.bistel.database.rider.UserLocalStorage;
import com.bistelapp.bistel.informations.driver.driver_info;
import com.bistelapp.bistel.informations.rider.rider_info;
import com.bistelapp.bistel.internet.rider.FetchOnlineDrivers;
import com.bistelapp.bistel.internet.rider.GetDistanceDuration;
import com.bistelapp.bistel.internet.rider.GetLocationFromServer;
import com.bistelapp.bistel.utility.General;
import com.onesignal.OneSignal;

import java.util.ArrayList;


public class RiderActivity extends ActionBarActivity implements NavigationDrawerCallbacks, OnClickListener, LoadOnlineDrivers, LoadDistanceDuration, LoadAddressLocation {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private RiderAdapter adapter;
    private Button refresh;
    private TextView check;
    private General general;
    GPSService mGPSService;

    private UserLocalStorage userLocalStorage;
    private ArrayList<driver_info> customData = new ArrayList<>();
    private FetchOnlineDrivers fetchOnlineDrivers;
    private GetLocationFromServer getLocationFromServer;
    private GetDistanceDuration getDistanceDuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        general = new General(RiderActivity.this);
        userLocalStorage = new UserLocalStorage(RiderActivity.this);
        mGPSService = new GPSService(this);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        refresh = (Button)findViewById(R.id.get_driver);
        check = (TextView)findViewById(R.id.textView);

        check.setVisibility(View.GONE);

        adapter = new RiderAdapter(RiderActivity.this, this);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(RiderActivity.this));
        recyclerView.setAdapter(adapter);
        fetchOnlineDrivers = new FetchOnlineDrivers(RiderActivity.this,this);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rider_info ri = userLocalStorage.getRiderInfo();
                  runOnStart();
               //startContactingOnlineDrivers();
            }
        });

        //distanceDuration();
        //showPlayerID();

    }

    private void distanceDuration(){
        getDistanceDuration = new GetDistanceDuration(RiderActivity.this,"palmgrove,Lagos,Nigeria","bariga",this);
        getDistanceDuration.getDistanceDuration();
    }

    private void runOnStart() {
        general.displayProgressDialog("getting location");
        String address = "";
        mGPSService.getLocation();

        if (mGPSService.isLocationAvailable == false) {

            // Here you can ask the user to try again, using return; for that
            Toast.makeText(this, "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
            general.dismissProgressDialog();
            return;

            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
            // address = "Location not available";
        } else {

            // Getting location co-ordinates
            double latitude = mGPSService.getLatitude();
            double longitude = mGPSService.getLongitude();
            Toast.makeText(this, "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();

            //address = mGPSService.getLocationAddress();
            //address = mGPSService.getLocation_(latitude,longitude);

            getLocationFromServer = new GetLocationFromServer(RiderActivity.this,latitude,longitude,this);
            getLocationFromServer.UpdateLocation();
            //address = getLocationFromServer.getLocation();


            general.dismissProgressDialog();

        }

        //Toast.makeText(RiderActivity.this, "Your address is: " + address, Toast.LENGTH_LONG).show();

        // make sure you close the gps after using it. Save user's battery power
        mGPSService.closeGPS();
        general.dismissProgressDialog();

    }

    private void startContactingOnlineDrivers() {
       fetchOnlineDrivers.OnlineDrivers();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        //Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else{
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.rider, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        if (id == R.id.action_refresh){
            runOnStart();
        }

        return super.onOptionsItemSelected(item);
    }

    public void displayAlertDialog(String title){
        final AlertDialog alertDialog = new AlertDialog.Builder(RiderActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);
        Button date;

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_booking, (ViewGroup)findViewById(R.id.root),false);

        final TextInputLayout tPick,tDest;
        final EditText etPick,etDest;


        tPick = (TextInputLayout)view.findViewById(R.id.pickup_location);
        tDest = (TextInputLayout)view.findViewById(R.id.dest);
        etPick = (EditText)view.findViewById(R.id.tv_pickup_location);
        etDest = (EditText)view.findViewById(R.id.tv_dest);
        date = (Button)view.findViewById(R.id.date);



        alertDialog.setButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setButton2("BOOK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(general.rider_book_all("pickup location", "destination", tPick, etPick, tDest, etDest)){

                }
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }

    public void showPlayerID(){
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String s, String s2) {
                Toast.makeText(RiderActivity.this, "player id = "+s, Toast.LENGTH_LONG).show();
            }
        });
    }




    @Override
    public void OnClick(View view, int position) {
        switch (view.getId()){
            case R.id.book:
                Bundle bundle = new Bundle();
                driver_info di = customData.get(position);
                bundle.putString("name",di.lastname+" "+di.firstname);
                bundle.putString("mobile",di.mobile);
                bundle.putInt("driver_id",di.id);
                bundle.putString("driver_player_id",di.playerID);
                bundle.putString("driver_plate_number",di.plate_number);
                Intent intent = new Intent(RiderActivity.this, BookingActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                //displayAlertDialog("Booking Details");
                break;
            case R.id.request:
                Bundle bundle1 = new Bundle();
                driver_info di1 = customData.get(position);
                bundle1.putString("name",di1.lastname+" "+di1.firstname);
                bundle1.putString("mobile",di1.mobile);
                bundle1.putInt("driver_id",di1.id);
                bundle1.putString("driver_player_id",di1.playerID);
                bundle1.putString("driver_plate_number",di1.plate_number);
                Intent intent1 = new Intent(RiderActivity.this, RequestActivity.class);
                intent1.putExtras(bundle1);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onLoadOnlineDrivers(ArrayList<driver_info> list) {
        if(!list.isEmpty()){
            customData = list;
            adapter.setList(list);
            refresh.setVisibility(View.GONE);
            check.setVisibility(View.GONE);
        }else {
            refresh.setVisibility(View.VISIBLE);
            check.setVisibility(View.VISIBLE);
            check.setText("no driver available");
        }
        //general.dismissProgressDialog();
    }

    @Override
    public void onLoadAddressLocation(String location) {
        if(location.contentEquals("")){
            Toast.makeText(RiderActivity.this,"retrying to get location",Toast.LENGTH_LONG).show();
            getLocationFromServer.UpdateLocation();
        }else {
            Toast.makeText(RiderActivity.this, "Location is - " + location, Toast.LENGTH_LONG).show();
            rider_info ri = userLocalStorage.getRiderInfo();
            rider_info current = new rider_info(ri.id, ri.firstname, ri.lastname, ri.email, ri.mobile, ri.password, location,ri.voucher,ri.voucher_status,ri.playerID,ri.voucher_code_percent);
            userLocalStorage.storeUser(current);
            startContactingOnlineDrivers();
        }
    }

    @Override
    public void onLoadDistanceDuration(String distance, String duration, int distance_value, int duration_value) {
        Toast.makeText(RiderActivity.this, "distance = "+distance+"\nduration = "+duration,Toast.LENGTH_LONG).show();
    }
}
