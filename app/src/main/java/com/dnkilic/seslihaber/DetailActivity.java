package com.dnkilic.seslihaber;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class DetailActivity extends AppCompatActivity {

    WebView wvNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final ProgressBar progressBar  = (ProgressBar) findViewById(R.id.pbNewsDetail);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.MULTIPLY);


        final String title = getIntent().getStringExtra("NEWS_TITLE");

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

        wvNews.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                    wvNews.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    wvNews.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        /*AdView adView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);*/
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
