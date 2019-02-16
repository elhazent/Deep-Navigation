package com.elhazent.picodiploma.mydeepnavigation;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnOpenDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnOpenDetail = findViewById(R.id.btn_open_detail);
        btnOpenDetail.setOnClickListener(this);
        DelayAsync delayAsync = new DelayAsync();
        delayAsync.execute();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_open_detail){
            Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
            detailIntent.putExtra(DetailActivity.EXTRA_TITLE,"Hallo, Good News");
            detailIntent.putExtra(DetailActivity.EXTRA_MESSAGE,"Now you can learn android in elhazent.com");
            startActivity(detailIntent);
        }
    }
    private class DelayAsync extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
            showNotification(MainActivity.this,"Hi, how are you?", "Do you hace any plan this weekend? Let's hangout", 110);
        }

        private void showNotification(Context context, String title, String message, int notifId) {
            String CHANNEL_ID = "Channel_1";
            String CHANNEL_NAME = "Navigation channel";
            Intent notifDetailIntent = new Intent(MainActivity.this, DetailActivity.class);
            notifDetailIntent.putExtra(DetailActivity.EXTRA_TITLE, title);
            notifDetailIntent.putExtra(DetailActivity.EXTRA_MESSAGE,message);
            PendingIntent pendingIntent = TaskStackBuilder.create(MainActivity.this).addParentStack(DetailActivity.class).addNextIntent(notifDetailIntent).getPendingIntent(110, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManager notificationManagerCompat = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID).setContentTitle(title).setSmallIcon(R.drawable.ic_email_black_24dp).setContentText(message).setColor(ContextCompat.getColor(context, android.R.color.black)).setVibrate(new long[]{1000,1000,1000,1000,1000}).setSound(alarmSound).setContentIntent(pendingIntent).setAutoCancel(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableVibration(true);
                channel.setVibrationPattern(new  long[]{1000,1000,1000,1000,1000});
                builder.setChannelId(CHANNEL_ID);
                if (notificationManagerCompat != null){
                    notificationManagerCompat.createNotificationChannel(channel);
                }
            }
            Notification notification = builder.build();
            if (notificationManagerCompat != null){
                notificationManagerCompat.notify(notifId,notification);
            }
        }
    }
}
