package com.fyusuf.tvguide;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //Toast.makeText(context, "Notification!", Toast.LENGTH_LONG).show();

        /*Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null)
        {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();*/
        SharedPreferences sharedPreferences = context.getSharedPreferences("SelectFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = sharedPreferences.getString("program", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> arrayList = gson.fromJson(json, type);
        String str = intent.getStringExtra("programChannel")+intent.getStringExtra("programTime")+intent.getStringExtra("programName");
        if(arrayList !=null) {
           arrayList.remove(str);
        }
        json = gson.toJson(arrayList);
        editor.putString("program", json);
        editor.commit();
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(android.R.drawable.ic_menu_info_details)
                        .setContentTitle(intent.getStringExtra("programName"))
                        .setLargeIcon((Bitmap)intent.getParcelableExtra("programImg"))
                        .setVibrate(new long[]{100,200,100,200})
                        .setLights(Color.GREEN, 3000, 3000)
                        .setVisibility(1)
                        .setContentText("Program Başladı...");

        Intent notificationIntent = new Intent(context, DetailActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(intent.getStringExtra("programName").hashCode(), builder.build());
    }
}