package com.bistelapp.bistel.riders;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bistelapp.bistel.R;
import com.bistelapp.bistel.database.rider.UserLocalStorage;
import com.bistelapp.bistel.informations.rider.rider_info;
import com.bistelapp.bistel.internet.rider.FetchVoucherCode;

public class VoucherActivity extends ActionBarActivity implements View.OnClickListener {

    EditText code;
    Button verify;
    FetchVoucherCode fetchVoucherCode;
    UserLocalStorage userLocalStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);

        userLocalStorage = new UserLocalStorage(VoucherActivity.this);

        code = (EditText)findViewById(R.id.voucher_code);
        verify = (Button)findViewById(R.id.verify);
        verify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.verify:
                verify_voucher_code();
                break;
        }
    }

    private void verify_voucher_code(){
        String getCode = code.getText().toString();
        rider_info ri = userLocalStorage.getRiderInfo();
        if(getCode.contentEquals("")){
            Toast.makeText(VoucherActivity.this,"please provide a voucher code",Toast.LENGTH_LONG).show();
        }else{
            fetchVoucherCode = new FetchVoucherCode(VoucherActivity.this,ri.email);
            fetchVoucherCode.FetchCode(getCode);
        }
    }
}
