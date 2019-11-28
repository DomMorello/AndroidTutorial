package com.example.mysmsreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SmsReceiver extends BroadcastReceiver {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    private static final String TAG = "SmsReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive() 호출됨");

        Bundle bundle = intent.getExtras();
        SmsMessage[] messages = parseSmsMessage(bundle);

        if(messages.length > 0){
            String sender = messages[0].getOriginatingAddress();
            Log.d(TAG,"sender: "+sender);

            String contents = messages[0].getMessageBody().toString();
            Log.d(TAG,"contents: "+contents);

            Date receivedDate = new Date(messages[0].getTimestampMillis());
            Log.d(TAG,"received date: "+receivedDate);

            sendToActivity(context, sender, contents, receivedDate);
        }

    }

    private void sendToActivity(Context context, String sender, String contents, Date receivedDate){
        Intent intent = new Intent(context, SmsActivity.class);   //getApplicationContext를 불러올 수 없다. 이건 onRecieve에서 쓸 수 있음.
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("sender",sender);
        intent.putExtra("contents",contents);
        intent.putExtra("receivedDate",format.format(receivedDate));
        context.startActivity(intent);
    }

    private SmsMessage[] parseSmsMessage(Bundle bundle){
        Object[] objs = (Object[]) bundle.get("pdus"); //pdus : sms데이터를 처리하는 국제표준 프로토콜이 smpp라고 하는데 그 안에 데이터가 이런 이름으로 돼있음.
        SmsMessage[] messages = new SmsMessage[objs.length];

        for(int i=0; i < objs.length; i++){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){ //마시멜로보다 높으면
                String format = bundle.getString("format");
                messages[i] = SmsMessage.createFromPdu((byte[])objs[i], format);
            }else{
                messages[i] = SmsMessage.createFromPdu((byte[])objs[i]);
            }
        }
        return messages;
    }
}
