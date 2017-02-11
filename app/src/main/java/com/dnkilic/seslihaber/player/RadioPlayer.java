package com.dnkilic.seslihaber.player;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

import com.dnkilic.seslihaber.MainActivity;
import com.dnkilic.seslihaber.R;

import java.io.IOException;

public class RadioPlayer extends Service implements MediaPlayer.OnPreparedListener{

    public static final String ACTION_PLAY = "play";
    public static final String BROADCAST_PLAYBACK_STOP = "stop";

    private MediaPlayer mediaPlayer;
    private String channelName;

    final BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();

            if (action.equals(BROADCAST_PLAYBACK_STOP))
            {
                stop();
                stopSelf();
            }
        }
    };

    public RadioPlayer() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_PLAYBACK_STOP);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent.getAction().equals(ACTION_PLAY))
        {
            channelName = intent.getStringExtra("CHANNEL_NAME");
            Uri uri = intent.getData();
            start(uri.toString());
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public boolean stop() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean start(String channelUrl) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(channelUrl);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private PendingIntent makePendingIntent(String broadcast)
    {
        Intent intent = new Intent(broadcast);
        return PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();

        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        Notification notification =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Radyo")
                        .setContentText(channelName)
                        .setColor(getResources().getColor(R.color.colorAboutPrimary))
                        .setContentIntent(resultPendingIntent)
                        .addAction(R.mipmap.ic_notification_player_stop, "Stop", makePendingIntent(BROADCAST_PLAYBACK_STOP))
                        .build();

        startForeground(1903, notification);
    }
}
