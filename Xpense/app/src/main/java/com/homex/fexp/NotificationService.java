package com.homex.fexp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationService extends Service {
    public NotificationService() {
    }

    NotificationCompat.Builder builder;
    private static final int unique_ID = 45612;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        builder = new NotificationCompat.Builder(this);
        builder.setAutoCancel(true);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        final DBhandler db = new DBhandler(this);
        Log.i("tag", " Service CALLED!");
        Runnable r = new Runnable() {
            @Override
            public void run() {
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setTicker("Expense notification");
                builder.setContentTitle("XPENSE");
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                int rmon,ryear,cexp,per;
                String s;
                Calendar cal=Calendar.getInstance();
                rmon=cal.get(Calendar.MONTH)+1;
                ryear=cal.get(Calendar.YEAR);
                if(rmon<10)
                    s=ryear+"-0"+rmon;
                else
                    s=ryear+"-"+rmon;
                Mydata getData=new Mydata();
                SharedPreferences sharedpref=getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                getData=db.getContact(sharedpref.getString("email",""));
                cexp=db.getmonthexpense(sharedpref.getString("email",""),s );
                per=(100*cexp)/getData.budget;
                builder.setContentText("You have spent "+per+"% of your monthly budget");
                manager.notify(unique_ID, builder.build());

            }
        };
        Thread t = new Thread(r);
        t.start();
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("tag", "Destroyed!");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }}
