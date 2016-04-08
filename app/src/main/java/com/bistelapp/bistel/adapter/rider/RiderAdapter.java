package com.bistelapp.bistel.adapter.rider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bistelapp.bistel.R;
import com.bistelapp.bistel.callbacks.rider.OnClickListener;
import com.bistelapp.bistel.informations.rider.online_driver;
import com.bistelapp.bistel.utility.General;

import java.util.ArrayList;

/**
 * Created by tayo on 3/31/2016.
 */
public class RiderAdapter extends RecyclerView.Adapter<RiderAdapter.RiderHolder> {

    LayoutInflater inflater;
    Context context;
    ArrayList<online_driver> info = new ArrayList<>();
    General general;
    OnClickListener clickListener;

    public RiderAdapter(Context context, OnClickListener clickListener){
        this.context = context;
        this.clickListener = clickListener;
        inflater = LayoutInflater.from(context);
        general = new General(context);
    }

    public void setList(ArrayList<online_driver> info){
        this.info = info;
        notifyDataSetChanged();
    }

    @Override
    public RiderHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.online_driver_list,viewGroup,false);
        RiderHolder rh = new RiderHolder(view);
        return rh;
    }

    @Override
    public void onBindViewHolder(RiderHolder riderHolder, int i) {
        online_driver current = info.get(i);
        riderHolder.fn.setText(current.first_name);
        riderHolder.car_plate.setText(current.car_plate_number);
        riderHolder.location.setText(current.location);

    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    class RiderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView fn, car_plate,location;
        Button book,request;

        public RiderHolder(View itemView) {
            super(itemView);

            fn = (TextView)itemView.findViewById(R.id.first);
            car_plate = (TextView)itemView.findViewById(R.id.plate);
            location = (TextView)itemView.findViewById(R.id.location);
            book = (Button)itemView.findViewById(R.id.book);
            request = (Button)itemView.findViewById(R.id.request);

            book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.OnClick(v,getPosition());
                }
            });
            request.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.request:
                    online_driver current = info.get(getPosition());
                    general.displayAlertDialog("Confirm", "Are you sure you want to request for this driver now?", current.mobile);
                    break;
            }
        }
    }
}
