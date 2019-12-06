package com.vmb.hoingu.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.roger.catloadinglibrary.CatLoadingView;
import com.vmb.hoingu.R;
import com.vmb.hoingu.config.Config;
import com.vmb.hoingu.utils.Utils;

public class AddQuestionActivity extends AppCompatActivity {

    private boolean isStartNew = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        /*FrameLayout layout_ads = findViewById(R.id.layout_ads);
        RelativeLayout adView = findViewById(R.id.adView);
        Utils.showAd(getApplicationContext(), adView, layout_ads);*/

        CatLoadingView load = new CatLoadingView();
        load.show(getSupportFragmentManager(), "");

        WebView webView = findViewById(R.id.webview);
        webView.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSfZOyq-NYPuhZnT8R_bmVzXy4_0A_PpV20HUbGSkGHMKme11A/viewform");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                load.dismiss();
                super.onPageFinished(view, url);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (!isStartNew)
            Config.mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!Config.mediaPlayer.isPlaying())
            if (Config.isPlayBackgroundMusic)
                Config.mediaPlayer.start();
    }

    @Override
    public void onBackPressed() {
        isStartNew = true;
        super.onBackPressed();
    }
}
