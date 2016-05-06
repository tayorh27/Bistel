package com.bistelapp.bistel.adapter.rider;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bistelapp.bistel.R;
import com.bistelapp.bistel.callbacks.rider.OnClickListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Created by tayo on 4/13/2016.
 */
public class AT_Adapter extends RecyclerView.Adapter<AT_Adapter.PredictionHolder> implements Filterable {

    ArrayList<AT_Place> myResultList;
    GoogleApiClient googleApiClient;
    LatLngBounds bounds;
    AutocompleteFilter filter;
    Context context;
    int layout;
    OnClickListener clickListener;


    LayoutInflater inflater;

    public AT_Adapter(Context context, int resources, GoogleApiClient googleApiClient, LatLngBounds bounds, AutocompleteFilter filter, OnClickListener clickListener) {
        this.context = context;
        this.googleApiClient = googleApiClient;
        this.bounds = bounds;
        this.layout = resources;
        this.filter = filter;
        inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
    }

    public void setBounds(LatLngBounds bounds) {
        this.bounds = bounds;
    }


    @Override
    public PredictionHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(layout, viewGroup, false);
        PredictionHolder predictionHolder = new PredictionHolder(view);
        return predictionHolder;
    }

    @Override
    public void onBindViewHolder(PredictionHolder predictionHolder, int i) {
        predictionHolder.myPrediction.setText(myResultList.get(i).description);
    }

    @Override
    public int getItemCount() {
        if (myResultList != null)
            return myResultList.size();
        else
            return 0;
    }

    public AT_Place getItem(int position) {
        return myResultList.get(position);
    }

    @Override
    public Filter getFilter() {
        Filter filter1 = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    myResultList = getAutocomplete(constraint);
                    if (myResultList != null) {
                        filterResults.values = myResultList;
                        filterResults.count = myResultList.size();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {

                }
            }
        };
        return filter1;
    }

    private ArrayList<AT_Place> getAutocomplete(CharSequence constraint) {
        if (googleApiClient.isConnected()) {
            PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi
                            .getAutocompletePredictions(googleApiClient, constraint.toString(), bounds, filter);

            AutocompletePredictionBuffer autocompletePredictions = results.await(60, TimeUnit.SECONDS);

            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                Toast.makeText(context, "Error contacting API: " + status.toString(), Toast.LENGTH_LONG).show();
                autocompletePredictions.release();
                return null;
            }

            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
            ArrayList resultList = new ArrayList(autocompletePredictions.getCount());
            while (iterator.hasNext()) {
                AutocompletePrediction prediction = iterator.next();
                resultList.add(new AT_Place(prediction.getPlaceId(), prediction.getDescription()));
            }
            autocompletePredictions.release();

            return resultList;

        }
        return null;
    }

    public class PredictionHolder extends RecyclerView.ViewHolder {

        TextView myPrediction;
        RelativeLayout myRow;

        public PredictionHolder(View itemView) {
            super(itemView);
            myPrediction = (TextView)itemView.findViewById(R.id.address);
            myRow = (RelativeLayout)itemView.findViewById(R.id.autocomplete_row);

            myRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickListener != null){
                        clickListener.OnClick(v,getPosition());
                    }
                }
            });

        }
    }

    public class AT_Place {
        public CharSequence placeId;
        public CharSequence description;

        AT_Place(CharSequence placeId, CharSequence description) {
            this.placeId = placeId;
            this.description = description;
        }

        @Override
        public String toString() {
            return description.toString();
        }
    }
}
