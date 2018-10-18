package com.lujayn.latiwala.common;

/**
 * Created by Belal on 03/11/16.
 */

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;

import com.lujayn.latiwala.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Ravi on 31/03/15.
 */

public class MyNotificationManager {

    public static final int ID_BIG_NOTIFICATION = 234;
    public static final int ID_SMALL_NOTIFICATION = 235;
    NotificationManager notificationManager;
    private Context mCtx;
    String CHANNEL_ID="RajuChannelId";
    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    public MyNotificationManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    //the method will show a big notification with an image
    //parameters are title for message title, message for message text, url of the big image and an intent that will open
    //when you will tap on the notification
    public void showBigNotification(String title, String message, String url, Intent intent) {
       // new DownloadImageTask(title,message,url,intent).execute();

        notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "RajuChanel";
            String description = "Notification description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
       // Bitmap remote_picture = null;
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mCtx,
                        ID_BIG_NOTIFICATION,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
        Bitmap bitmap = getBitmapFromURL("http://api.androidhive.info/images/minion.jpg");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx);

      /*  try {
            remote_picture = BitmapFactory.decodeStream(
                    (InputStream) new URL("http://api.androidhive.info/images/minion.jpg").getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        Log.e("TAG", "Json bitmap: " +bitmap);
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);

        Notification notification;
        notification = mBuilder.setSmallIcon(R.drawable.logo).setTicker(title).setWhen(0)
                .setAutoCancel(false)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setStyle(bigPictureStyle)
                .setChannelId(CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.logo))
                .setContentText(message)
                .setSound(soundUri)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(ID_BIG_NOTIFICATION, notification);

    }

    //the method will show a small notification
    //parameters are title for message title, message for message text and an intent that will open
    //when you will tap on the notification
    public void showSmallNotification(String title, String message, Intent intent) {
        notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "RajuChanel";
            String description = "Notification description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
        }
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mCtx,
                        ID_SMALL_NOTIFICATION,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx);
        Notification notification;
        notification = mBuilder.setSmallIcon(R.drawable.logo).setTicker(title).setWhen(0)
                .setAutoCancel(false)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setChannelId(CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.logo))
                .setContentText(message)
                .setSound(soundUri)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(ID_SMALL_NOTIFICATION, notification);
    }

    //The method will return Bitmap from an image URL
    private Bitmap getBitmapFromURL(String strURL) {
        Bitmap myBitmap;
        try {
            URL url = new URL(strURL);
            //Log.e("TAG", "Json bitmap: " +url);
            HttpURLConnection connection =null;
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream input = connection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(input);

            //Log.e("TAG", "Json bitmap: " +myBitmap);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        //private ImageView imageView;
        private Bitmap image;
        private Context mContext;
        private String title, message, imageUrl;
        private Intent intent;
        public DownloadImageTask(String title, String message, String url, Intent intent) {
            this.title = title;
            this.message = message;
            this.imageUrl = url;
            this.intent = intent;
        }

        protected Bitmap doInBackground(String... params) {
            InputStream in;
            try {
                URL url = new URL(this.imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressLint("NewApi")
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            Bitmap remote_picture = null;
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            mCtx,
                            ID_BIG_NOTIFICATION,
                            intent,
                            PendingIntent.FLAG_CANCEL_CURRENT
                    );
            //Bitmap bitmap = getBitmapFromURL("http://api.androidhive.info/images/minion.jpg");
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx);


            try {
                remote_picture = BitmapFactory.decodeStream(
                        (InputStream) new URL("http://api.androidhive.info/images/minion.jpg").getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.e("TAG", "Json bitmap: " +result);
            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
            bigPictureStyle.setBigContentTitle(title);
            bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
            bigPictureStyle.bigPicture(remote_picture);

            Notification notification;
            notification = mBuilder.setSmallIcon(R.drawable.logo).setTicker(title).setWhen(0)
                    .setAutoCancel(false)
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle(title)
                    .setStyle(bigPictureStyle)
                    .setSmallIcon(R.drawable.logo)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.logo))
                    .setContentText(message)
                    .setSound(soundUri)
                    .build();

            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(ID_BIG_NOTIFICATION, notification);
        }
    }

}