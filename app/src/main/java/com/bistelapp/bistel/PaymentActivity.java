package com.bistelapp.bistel;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class PaymentActivity extends ActionBarActivity {

    TextView pay;
    String text="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        pay = (TextView)findViewById(R.id.tv_payment);

        text += "Make a deposit or transfer to \n"+
                "Back name: Diamond bank \n"+
                "Account number: 0068478994 \n"+
                "Account name: Bistel International Enterprises. \n"+
                "Account type: Current Account \n"+
                "And send us your proof of payment \n" +
                "before your booking to \n" +
                "customerservice@bistelint.com.ng \n"+"" +
                "or text message with your transaction ID \n"+"" +
                "to 08189884270";

        pay.setText(text);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
