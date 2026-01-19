package com.fungsitama.dhsshopee.activity.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.EditText;

public class OTPReceiver extends BroadcastReceiver {

    private static EditText editText_otp;

    public void setEditText_otp(EditText editText){
        OTPReceiver.editText_otp = editText;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            for (SmsMessage smsMessage : smsMessages){
                String message_body = smsMessage.getMessageBody();

                /*String titikDua = ": ";
                String getTitikDua;

                if (titikDua.equals("")){
                    getTitikDua = "masukkan";
                }else{
                    getTitikDua = "enter";
                }
                String getOTP = message_body.split(getTitikDua)[1];*/

                /*String language = "en-US";
                String getPatokan;

                if (language.equals("en-US")){
                    getPatokan= "enter";
                }else{
                    getPatokan= "masukkan";
                }
                String getOTP = message_body.split(getPatokan)[1];*/

                String getOTP = message_body.split("masukkan ")[1];

                editText_otp.setText(getOTP);

                Log.d("TAG", "onReceive: " + message_body + " " + getOTP);

            }
        }catch (Exception e){
            e.getMessage();
        }
    }
}
