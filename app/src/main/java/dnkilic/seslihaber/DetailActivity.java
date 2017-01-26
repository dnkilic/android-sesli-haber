package dnkilic.seslihaber;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class DetailActivity extends AppCompatActivity {

    WebView wvNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String title = getIntent().getStringExtra("NEWS_TITLE");

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle(title);
        }

        String url = getIntent().getStringExtra("NEWS_URL");

        wvNews = (WebView) findViewById(R.id.wvNews);
        wvNews.setWebViewClient(new WebViewClient());
        WebSettings webSettings = wvNews.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wvNews.loadUrl(url);

        AdView adView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
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
