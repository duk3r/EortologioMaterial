package com.duk3r.eortologio2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyStartupIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //Intent serviceIntent = new Intent();
        //serviceIntent.setAction("com.duk3r.eortologio2.MyService");
        //context.startService(serviceIntent);


        Intent serviceIntent = new Intent(context,MyService.class);
        context.startService(serviceIntent);


    }

}