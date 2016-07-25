package com.bistelapp.bistel.async.rider;

import android.content.Context;
import android.os.AsyncTask;

import com.bistelapp.bistel.internet.rider.UpdateVoucherCode;

/**
 * Created by Control & Inst. LAB on 10-Jun-16.
 */
public class TaskUpdateVouchers extends AsyncTask<Void, Void, Void> {
    Context context;
    UpdateVoucherCode updateVoucherCode;

    public TaskUpdateVouchers(Context context) {
        this.context = context;
        updateVoucherCode = new UpdateVoucherCode(context);
    }

    @Override
    protected Void doInBackground(Void... params) {
        updateVoucherCode.updateCode();
        return null;
    }
}
