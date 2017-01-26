package dnkilic.seslihaber;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

public class AboutActivity extends Activity {

    @BindViews({ R.id.textView2, R.id.textView20, R.id.textView21,
            R.id.textView22, R.id.textView23, R.id.textView24,
            R.id.textView25, R.id.textView26, R.id.textView27,
            R.id.textView29, R.id.textView30, R.id.textView32,
            R.id.textView33, R.id.textView34})
    List<TextView> nameViews;

    private static Typeface typeface;

    static final ButterKnife.Action<View> TYPEFACE = new ButterKnife.Action<View>() {
        @Override public void apply(View view, int index) {
            TextView tv = (TextView) view;
            tv.setTypeface(typeface);
            tv.setText(tv.getText().toString().toLowerCase());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        typeface = Typeface.createFromAsset(getAssets(),"appleberry.ttf");

        ButterKnife.bind(this);

        ButterKnife.apply(nameViews, TYPEFACE);
    }
}
