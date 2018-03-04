package com.wristcode.deliwala.extra;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.wristcode.deliwala.OTPActivity;

public class IncomingSMS extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        final Bundle bundle = intent.getExtras();
        try
        {
            if (bundle != null)
            {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++)
                {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    try
                    {
                        if (senderNum.contains("DLWALA"))
                        {
                            OTPActivity Sms = new OTPActivity();
                            message = message.replaceAll("[^0-9]", "");
                            Log.i("SMSReceiver", message);
                            Sms.receivedSms(message);
                        }
                    } catch (Exception e) {}
                }
            }
        } catch (Exception e) {}
    }
}
