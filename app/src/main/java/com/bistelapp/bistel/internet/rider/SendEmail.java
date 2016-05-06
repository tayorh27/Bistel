package com.bistelapp.bistel.internet.rider;

import java.util.Properties;

/**
 * Created by tayo on 5/6/2016.
 */
public class SendEmail {

    public void sendEmailToUser(String to, String body){
        String from = "support@bistelint.com.ng";
        String username = "gisanrinadetayo@gmail.com";
        String password = "nPbmGauCtMqKvn0rRsnPJA";
        String host = "smtp.mandrillapp.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");


    }
}
