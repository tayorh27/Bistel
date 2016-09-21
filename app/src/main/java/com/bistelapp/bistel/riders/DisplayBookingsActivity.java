package com.bistelapp.bistel.riders;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bistelapp.bistel.MyApplication;
import com.bistelapp.bistel.R;
import com.bistelapp.bistel.adapter.rider.BookingsAdapter;
import com.bistelapp.bistel.informations.rider.Bookings;

import java.util.ArrayList;

public class DisplayBookingsActivity extends ActionBarActivity {

    RecyclerView recyclerView;
    BookingsAdapter adapter;
    ArrayList<Bookings> customData = new ArrayList<>();
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_bookings);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        tv = (TextView)findViewById(R.id.textView);
        adapter = new BookingsAdapter(DisplayBookingsActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(DisplayBookingsActivity.this));
        recyclerView.setAdapter(adapter);
        customData = MyApplication.getWritableDatabase().getAllMyPosts();
        if(!customData.isEmpty()) {
            adapter.LoadBookings(customData);
            tv.setVisibility(View.GONE);
        }
    }
}
