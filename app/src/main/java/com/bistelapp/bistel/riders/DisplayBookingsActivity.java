package com.bistelapp.bistel.riders;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bistelapp.bistel.MyApplication;
import com.bistelapp.bistel.R;
import com.bistelapp.bistel.adapter.rider.BookingsAdapter;
import com.bistelapp.bistel.callbacks.rider.BookingsDelete;
import com.bistelapp.bistel.informations.rider.Bookings;
import com.bistelapp.bistel.utility.General;

import java.util.ArrayList;

public class DisplayBookingsActivity extends ActionBarActivity implements BookingsDelete {

    RecyclerView recyclerView;
    BookingsAdapter adapter;
    ArrayList<Bookings> customData = new ArrayList<>();
    TextView tv;
    General general;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_bookings);

        general = new General(DisplayBookingsActivity.this);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        tv = (TextView)findViewById(R.id.textView);
        adapter = new BookingsAdapter(DisplayBookingsActivity.this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(DisplayBookingsActivity.this));
        recyclerView.setAdapter(adapter);
        customData = MyApplication.getWritableDatabase().getAllMyPosts();
        if(!customData.isEmpty()) {
            adapter.LoadBookings(customData);
            tv.setVisibility(View.GONE);
        }else{
            tv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.display_booking,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_clear){
            ShowAlert();
        }
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void ShowAlert() {
        final AlertDialog alert = new AlertDialog.Builder(DisplayBookingsActivity.this).create();
        alert.setTitle("Delete Alert");
        alert.setMessage("Are you sure you want to delete all?");
        alert.setButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alert.dismiss();
            }
        });
        alert.setButton2("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyApplication.getWritableDatabase().deleteAll();
                general.LoadDB();
                customData.clear();
                customData = MyApplication.getWritableDatabase().getAllMyPosts();
                adapter.LoadBookings(customData);
            }
        });
        alert.show();
    }

    @Override
    public void onBookingsDelete(View view, int position) {
        final Bookings current = customData.get(position);
        final AlertDialog alert = new AlertDialog.Builder(DisplayBookingsActivity.this).create();
        alert.setTitle("Delete Alert");
        alert.setMessage("Are you sure you want to delete this?");
        alert.setButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alert.dismiss();
            }
        });
        alert.setButton2("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyApplication.getWritableDatabase().deleteDatabase(current.id);
                customData.clear();
                customData = MyApplication.getWritableDatabase().getAllMyPosts();
                adapter.LoadBookings(customData);
            }
        });
        alert.show();
    }
}
