package dnkilic.seslihaber.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import java.io.IOException;

public class RadioPlayer {

    private Context context;
    private MediaPlayer mediaPlayer;

    public RadioPlayer(Context context) {
        this.context = context;
    }

    public boolean isPlaying() {
        try {
            if(mediaPlayer != null)
            {
                if(mediaPlayer.isPlaying())
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        catch (IllegalStateException e)
        {
            e.printStackTrace();
            return true;
        }
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
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
