package com.bistelapp.bistel.riders;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bistelapp.bistel.R;
import com.bistelapp.bistel.adapter.rider.RiderAdapter;
import com.bistelapp.bistel.callbacks.rider.OnClickListener;
import com.bistelapp.bistel.informations.rider.online_driver;
import com.bistelapp.bistel.utility.General;

import java.util.ArrayList;
import java.util.Calendar;


public class RiderActivity extends ActionBarActivity implements NavigationDrawerCallbacks, OnClickListener {

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
    private int year, month, day;
    private DatePicker datePicker;
    private Calendar calendar;
    private String setDate = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        general = new General(RiderActivity.this);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        setDate = showDate(year, month+1, day);

        refresh = (Button)findViewById(R.id.get_driver);
        check = (TextView)findViewById(R.id.textView);

        refresh.setVisibility(View.GONE);
        check.setVisibility(View.GONE);

        adapter = new RiderAdapter(RiderActivity.this, this);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(RiderActivity.this));
        recyclerView.setAdapter(adapter);
        adapter.setList(list());
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

    private ArrayList<online_driver> showList(ArrayList<online_driver> l){
        if(!l.isEmpty()){
            refresh.setVisibility(View.GONE);
            check.setVisibility(View.GONE);
            return null;
        }else {
            return l;
        }
    }

    private ArrayList<online_driver> list(){

        ArrayList<online_driver> current = new ArrayList<>();
        current.add(new online_driver("Aderinsola","IKJABCDE","Apartment","avater.jpg","08189884270"));
        current.add(new online_driver("Adetayo","HYKISDK","Mariere Hall","avater.jpg","08166076456"));
        current.add(new online_driver("Jesutofunmi","JLHDUIH","Esca Chapel","avater.jpg","08168884202"));
        current.add(new online_driver("Karo","LIHIDHI","Always on my bed","avater.jpg","08089541225"));
        current.add(new online_driver("Kay","KIJDIFF","Outside school","avater.jpg","08169066589"));
        current.add(new online_driver("Ede","IJHDIFHI","SUB with babes","avater.jpg","07080119470"));
        return  current;
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

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == 999){
            return new DatePickerDialog(this,mDate,year,month,day);
        }
        return null;
    }



    private String showDate(int year, int month, int day) {
        String date1 = String.valueOf(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));


        StringBuilder builder = new StringBuilder().append(day).append("/").append(month).append("/").append(year);
//        date.setText(builder.toString());
        return  builder.toString();
    }

    private void popupDate() {
        showDialog(999);
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

        date.setText(setDate);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDate();
            }
        });



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


    private DatePickerDialog.OnDateSetListener mDate = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            showDate(year,monthOfYear,dayOfMonth);
        }
    };

    @Override
    public void OnClick(View view, int position) {
        switch (view.getId()){
            case R.id.book:
                displayAlertDialog("Booking Details");
                break;
        }
    }
}
