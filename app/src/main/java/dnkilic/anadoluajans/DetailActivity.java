package dnkilic.anadoluajans;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DetailActivity extends AppCompatActivity {

    WebView wvNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);



        String title = getIntent().getStringExtra("NEWS_TITLE");


        String url = getIntent().getStringExtra("NEWS_URL");

        wvNews = (WebView) findViewById(R.id.wvNews);
        wvNews.setWebViewClient(new WebViewClient());
        WebSettings webSettings = wvNews.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wvNews.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (wvNews.canGoBack()) {
                        wvNews.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
