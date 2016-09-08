package com.bistelapp.bistel.adapter.rider;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bistelapp.bistel.AppConfig;
import com.bistelapp.bistel.R;
import com.bistelapp.bistel.callbacks.rider.OnClickListener;
import com.bistelapp.bistel.informations.driver.driver_info;
import com.bistelapp.bistel.internet.rider.GetDistanceDuration;
import com.bistelapp.bistel.network.VolleySingleton;
import com.bistelapp.bistel.utility.General;

import java.util.ArrayList;

/**
 * Created by tayo on 3/31/2016.
 */
public class RiderAdapter extends RecyclerView.Adapter<RiderAdapter.RiderHolder> {

    LayoutInflater inflater;
    Context context;
    ArrayList<driver_info> info = new ArrayList<>();
    General general;
    OnClickListener clickListener;
    VolleySingleton volleySingleton;
    ImageLoader imageLoader;
    String imageUrl = AppConfig.DRIVER_WEB_URL + "drivers_images/";
    GetDistanceDuration getDistanceDuration;
    String current_location;

    public RiderAdapter(Context context, OnClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
        inflater = LayoutInflater.from(context);
        general = new General(context);
        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getImageLoader();
        getDistanceDuration = new GetDistanceDuration(context);

    }

    public void setList(ArrayList<driver_info> information) {
        this.info = information;
        notifyDataSetChanged();
    }

    public void setCurrent_location(String current_location) {
        this.current_location = current_location;
    }

    @Override
    public RiderHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.online_driver_list, viewGroup, false);
        RiderHolder rh = new RiderHolder(view);
        return rh;
    }

    @Override
    public void onBindViewHolder(RiderHolder riderHolder, int i) {
        driver_info current = info.get(i);

        riderHolder.fn.setText(current.firstname);
        riderHolder.car_plate.setText(current.plate_number);
        riderHolder.location.setText(current.current_location);
        riderHolder.distance.setText(getDistanceDuration.getDistance(current_location, current.current_location));
        if(current.status.contentEquals("online")){
            riderHolder.status.setText("ONLINE");
            riderHolder.img_status.setImageResource(R.drawable.icon_online);
        }else if(current.status.contentEquals("offline")){
            riderHolder.status.setText("OFFLINE");
            riderHolder.img_status.setImageResource(R.drawable.icon_offline);
        }

    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    class RiderHolder extends RecyclerView.ViewHolder {

        TextView fn, car_plate, location, distance, status;
        Button book, request;
        ImageView img_status;

        public RiderHolder(View itemView) {
            super(itemView);

            fn = (TextView) itemView.findViewById(R.id.first);
            car_plate = (TextView) itemView.findViewById(R.id.plate);
            location = (TextView) itemView.findViewById(R.id.location);
            distance = (TextView) itemView.findViewById(R.id.kilometer);
            book = (Button) itemView.findViewById(R.id.book);
            request = (Button) itemView.findViewById(R.id.request);
            status = (TextView)itemView.findViewById(R.id.status);
            img_status = (ImageView)itemView.findViewById(R.id.image_status);

            book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.OnClick(v, getPosition());
                }
            });
            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.OnClick(v, getPosition());
                }
            });
             //driver_info current = info.get(getPosition());
            //general.displayAlertDialog("Confirm", "Are you sure you want to request for this driver now?", current.mobile);
        }
    }
}
