package com.bistelapp.bistel.adapter.rider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bistelapp.bistel.R;
import com.bistelapp.bistel.callbacks.rider.BookingsDelete;
import com.bistelapp.bistel.informations.rider.Bookings;

import java.util.ArrayList;

/**
 * Created by Control & Inst. LAB on 20-Sep-16.
 */
public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingPage> {

    ArrayList<Bookings> info = new ArrayList<>();
    Context context;
    LayoutInflater inflater;
    BookingsDelete bd;

    public BookingsAdapter(Context context, BookingsDelete bd) {
        this.context = context;
        this.bd = bd;
        inflater = LayoutInflater.from(context);
    }

    public void LoadBookings(ArrayList<Bookings> info) {
        this.info = info;
        notifyDataSetChanged();
    }

    @Override
    public BookingPage onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_display_bookings, parent, false);
        BookingPage bp = new BookingPage(view);
        return bp;
    }

    @Override
    public void onBindViewHolder(BookingPage holder, int position) {
        Bookings current = info.get(position);
        String any = current.driver_name;
        if (!any.contentEquals("none")) {
            holder.driver_name.setText(current.driver_name);
            holder.plate.setText(current.plateNumber);
            holder.booked_date.setText(current.booked_date);
            holder.amount_pay_type.setText(current.amount + " | " + current.payment_type);
            holder.pickup_location.setText(current.pickUp);
            holder.dest_location.setText(current.destination);
            holder.dist_time.setText(current.distance + " | " + current.time);
        }
    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    class BookingPage extends RecyclerView.ViewHolder {

        ImageView iv;
        TextView driver_name, plate, booked_date, amount_pay_type, pickup_location, dest_location, dist_time;
        RelativeLayout relativeLayout;

        public BookingPage(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.custom_comment_dp);
            driver_name = (TextView) itemView.findViewById(R.id.first);
            plate = (TextView) itemView.findViewById(R.id.plate);
            booked_date = (TextView) itemView.findViewById(R.id.status);
            amount_pay_type = (TextView) itemView.findViewById(R.id.location);
            pickup_location = (TextView) itemView.findViewById(R.id.pickup_location);
            dest_location = (TextView) itemView.findViewById(R.id.dest_location);
            dist_time = (TextView) itemView.findViewById(R.id.kilometer);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.bookings);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog();
//call,notify driver,cancel
                }
            });
            relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (bd != null) {
                        bd.onBookingsDelete(view, getPosition());
                    }
                    return true;
                }
            });
        }

        public void showDialog() {
            final AlertDialog dialog = new AlertDialog.Builder(context).create();
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View custom_view = inflater1.inflate(R.layout.custom_other_options, (ViewGroup) itemView.findViewById(R.id.root), false);
            ListView listView = (ListView) custom_view.findViewById(R.id.list);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Bookings current = info.get(getPosition());
                    switch (i) {
                        case 0:
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel: " + current.driver_number));
//                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                                // TODO: Consider calling
//                                //    ActivityCompat#requestPermissions
//                                // here to request the missing permissions, and then overriding
//                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                //                                          int[] grantResults)
//                                // to handle the case where the user grants the permission. See the documentation
//                                // for ActivityCompat#requestPermissions for more details.
//                                return;
//                            }
                            context.startActivity(intent);
                            dialog.dismiss();
                            break;
                        case 1:
                            break;
                        case 2:
                            break;
                    }
                }
            });
            dialog.setView(custom_view);
            dialog.show();
        }
    }
}
