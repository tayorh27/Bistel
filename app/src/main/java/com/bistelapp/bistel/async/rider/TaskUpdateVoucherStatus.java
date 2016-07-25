package com.bistelapp.bistel.async.rider;

import android.content.Context;
import android.os.AsyncTask;

import com.bistelapp.bistel.internet.rider.UpdateVoucherStatus;

/**
 * Created by Control & Inst. LAB on 10-Jun-16.
 */
public class TaskUpdateVoucherStatus extends AsyncTask<Void,Void,Void> {
    Context context;
    UpdateVoucherStatus updateVoucherStatus;
    public TaskUpdateVoucherStatus(Context context){
        this.context = context;
        updateVoucherStatus = new UpdateVoucherStatus(context);
    }
    @Override
    protected Void doInBackground(Void... params) {
        updateVoucherStatus.updateStatus();
        return null;
    }
}
