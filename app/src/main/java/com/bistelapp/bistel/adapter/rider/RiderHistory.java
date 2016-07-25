package com.bistelapp.bistel.adapter.rider;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Control & Inst. LAB on 13-Jul-16.
 */
public class RiderHistory extends RecyclerView.Adapter<RiderHistory.HistoryInfo> {

    Context context;
    public RiderHistory(Context context){
        this.context = context;
    }

    public void SetHistoryInfo(){
        notifyDataSetChanged();
    }

    @Override
    public HistoryInfo onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(HistoryInfo holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class HistoryInfo extends RecyclerView.ViewHolder{

        public HistoryInfo(View itemView) {
            super(itemView);
        }
    }
}


