package com.bistelapp.bistel.riders;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bistelapp.bistel.R;

public class AboutActivity extends ActionBarActivity {

    TextView about;
    String text="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        about = (TextView)findViewById(R.id.tv_about);

        text += "As a responsible company, Bistel is committed to protect and respect the privacy of its users.\n" +
                "Our Privacy Policy captures and processes the personal information that is provided to us voluntarily \n"+
                "by the user or is captured by us when you visit our website. The information we capture helps us in \n"+
                "identifying you and understanding your requirements carefully. The personal information captured \n"+
                "by us includes username, IP address, phone number, location, email address and so on. Our database \n"+
                "maintain the records of all these captured personal information.\n\n";

        text += "• VISION STATEMENT: Creating an avenue for our client to make money using their assets.\n\n" +
                "• MISSION STATEMENT: To offer customers another stream of income by using their cars to generate income and also a solid retirement plan\n\n" +
                "• DISCLAIMER: Bistel is not responsible for any kind of attack or threat to the user systems by any mails send by the third party websites. The user is the solely authorized to protect their systems against all such threats and attacks\n\n" +
                "• SLOGAN: “We convert liabilities into asset by using your car to make money for YOU”";


        about.setText(text);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
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
