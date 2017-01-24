package dnkilic.anadoluajans;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by burcu on 19.1.2017.
 */
public class AboutContact extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_contact);

        TextView textView2,textView20,textView21,textView22,textView23,textView24,textView25,textView26;
        TextView textView27,textView28,textView29,textView30,textView31,textView32,textView33,textView34;
        textView2=(TextView) findViewById(R.id.textView2);
        textView20= (TextView) findViewById(R.id.textView20);
        textView21= (TextView) findViewById(R.id.textView21);
        textView22= (TextView) findViewById(R.id.textView22);
        textView23= (TextView) findViewById(R.id.textView23);
        textView24= (TextView) findViewById(R.id.textView24);
        textView25= (TextView) findViewById(R.id.textView25);
        textView26= (TextView) findViewById(R.id.textView26);
        textView27= (TextView) findViewById(R.id.textView27);
        textView28= (TextView) findViewById(R.id.textView28);
        textView29= (TextView) findViewById(R.id.textView29);
        textView30= (TextView) findViewById(R.id.textView30);
        textView31= (TextView) findViewById(R.id.textView31);
        textView32= (TextView) findViewById(R.id.textView32);
        textView33= (TextView) findViewById(R.id.textView33);
        textView34= (TextView) findViewById(R.id.textView34);
        Typeface face=Typeface.createFromAsset(getAssets(),"Fonts/Asimov.ttf");
        textView2.setTypeface(face);
        textView20.setTypeface(face);
        textView21.setTypeface(face);
        textView22.setTypeface(face);
        textView23.setTypeface(face);
        textView24.setTypeface(face);
        textView25.setTypeface(face);
        textView26.setTypeface(face);
        textView27.setTypeface(face);
        textView28.setTypeface(face);
        textView29.setTypeface(face);
        textView30.setTypeface(face);
        textView31.setTypeface(face);
        textView32.setTypeface(face);
        textView33.setTypeface(face);
        textView34.setTypeface(face);
    }
}
