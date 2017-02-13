package com.dnkilic.seslihaber.speaker;

import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

import com.dnkilic.seslihaber.MainActivity;
import com.dnkilic.seslihaber.R;

public class Speaker {

    private TextToSpeech mTextToSpeech;
    private boolean mIsTTSEnabled = true;

    public boolean isSpeaking(){
        return mTextToSpeech.isSpeaking();
    }

    public Speaker(final MainActivity act){
        mTextToSpeech = new TextToSpeech(act, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i == TextToSpeech.SUCCESS){
                    mTextToSpeech.setLanguage(new Locale("TR-tr"));
                    mTextToSpeech.addEarcon("[intro]", "com.dnkilic.seslihaber", R.raw.intro);
                    mTextToSpeech.addEarcon("[beep]", "com.dnkilic.seslihaber", R.raw.beep);

                    switch (mTextToSpeech.isLanguageAvailable(new Locale("TR-tr")))
                    {
                        case TextToSpeech.LANG_MISSING_DATA:
                        case TextToSpeech.LANG_NOT_SUPPORTED:

                            Toast.makeText(act, "Cihazda Türkçe konuşma sentezi desteklenmemektedir.", Toast.LENGTH_SHORT).show();

                            Intent checkTTSIntent = new Intent();
                            checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
                            act.startActivityForResult(checkTTSIntent, 1000);
                        default:
                            break;
                    }
                }
                else
                {
                    mIsTTSEnabled = false;
                }
            }
        });
    }

    public int speak(String announce){
        if(mIsTTSEnabled){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return mTextToSpeech.speak(announce, TextToSpeech.QUEUE_ADD, null, "");
            }
            else
            {
                return mTextToSpeech.speak(announce, TextToSpeech.QUEUE_ADD, null);
            }
        }

        return -1;
    }

    public void play(String earcon){
        if(mIsTTSEnabled){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mTextToSpeech.playEarcon(earcon, TextToSpeech.QUEUE_ADD, null, "");
            }
            else
            {
                mTextToSpeech.playEarcon(earcon, TextToSpeech.QUEUE_ADD, null);
            }
        }
    }

    public void shutdown(){
        if(mTextToSpeech != null)
        {
            mTextToSpeech.shutdown();
        }
    }

    public void stop(){
        if(mTextToSpeech != null)
        {
            mTextToSpeech.stop();
        }
    }
}
