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
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.dnkilic.seslihaber.MainActivity;
import com.dnkilic.seslihaber.R;
import com.google.firebase.crash.FirebaseCrash;

import java.io.IOException;

public class RadioPlayer extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    public static final String ACTION_PLAY = "play";
    public static final String BROADCAST_PLAYBACK_STOP = "stop";

    private MediaPlayer mediaPlayer;
    private String channelName;
    private Messenger messageHandler;

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
        sendMessage();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent != null)
        {
            if(intent.getAction() != null && intent.getAction().equals(ACTION_PLAY))
            {
                messageHandler = (Messenger) intent.getExtras().get("MESSENGER");
                channelName = intent.getStringExtra("CHANNEL_NAME");
                Uri uri = intent.getData();
                start(uri.toString());
            }
        }

        return START_STICKY;
    }


    public void sendMessage() {
        Message message = Message.obtain();

        message.arg1 = 1;

        try {
            messageHandler.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
            FirebaseCrash.logcat(Log.ERROR, "Sesli Haber", "radio player");
            FirebaseCrash.report(e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean start(String channelUrl) {

        try {

            if(mediaPlayer != null)
            {
                stop();
            }

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(channelUrl);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepare();
        } catch (IOException e) {
            FirebaseCrash.logcat(Log.ERROR, "Sesli Haber", "radio player");
            FirebaseCrash.report(e);
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
        sendMessage();

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
                        .setContentTitle("Sesli Haber")
                        .setContentText(channelName)
                        .setColor(getResources().getColor(R.color.colorAboutPrimary))
                        .setContentIntent(resultPendingIntent)
                        .addAction(R.mipmap.ic_notification_player_stop, "Durdur", makePendingIntent(BROADCAST_PLAYBACK_STOP))
                        .build();

        startForeground(1903, notification);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        sendMessage();
        return false;
    }
}
