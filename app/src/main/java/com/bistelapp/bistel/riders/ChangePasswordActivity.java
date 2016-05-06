package com.bistelapp.bistel.riders;

import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bistelapp.bistel.R;
import com.bistelapp.bistel.internet.rider.UpdatePassword;
import com.bistelapp.bistel.utility.General;

public class ChangePasswordActivity extends ActionBarActivity implements View.OnClickListener {

    General general;
    EditText et1,et2,et3;
    Button change_password;

    UpdatePassword updatePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        general = new General(ChangePasswordActivity.this);
        change_password = (Button)findViewById(R.id.change_);

        et1 = (EditText)findViewById(R.id.cPassword);
        et2 = (EditText)findViewById(R.id.nPassword);
        et3 = (EditText)findViewById(R.id.tv_ccPassword);

        change_password.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_change_password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_:
                if(general.rider_check_all(true,"current password","new password","confirm password",et1,et2,et3)){
                    String cP = et1.getText().toString();
                    String cnP = et3.getText().toString();

                    updatePassword = new UpdatePassword(ChangePasswordActivity.this,cnP,cP);
                    updatePassword.updatePassword();
                }
                break;
        }
    }
}
