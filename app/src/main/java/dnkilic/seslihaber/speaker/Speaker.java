package dnkilic.seslihaber.speaker;

import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

import dnkilic.seslihaber.MainActivity;

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

                    switch (mTextToSpeech.isLanguageAvailable(new Locale("TR-tr")))
                    {
                        case TextToSpeech.LANG_MISSING_DATA:
                        case TextToSpeech.LANG_NOT_SUPPORTED:
                            Toast.makeText(act, "Cihazda Türkçe konuşma sentezi desteklenmemektedir.", Toast.LENGTH_SHORT).show();
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

    public void speak(String announce){
        if(mIsTTSEnabled){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mTextToSpeech.speak(announce, TextToSpeech.QUEUE_ADD, null, "");
            }
            else
            {
                mTextToSpeech.speak(announce, TextToSpeech.QUEUE_FLUSH, null);
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
