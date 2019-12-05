package com.vmb.hoingu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.roger.catloadinglibrary.CatLoadingView;
import com.vmb.hoingu.R;
import com.vmb.hoingu.config.Config;
import com.vmb.hoingu.listener.OnTouchClickListener;
import com.vmb.hoingu.utils.Utils;

public class LeaderboardActivity extends AppCompatActivity {

    private boolean isStartNew = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        FrameLayout layout_ads = findViewById(R.id.layout_ads);
        RelativeLayout adView = findViewById(R.id.adView);
        Utils.showAd(getApplicationContext(), adView, layout_ads);

        CatLoadingView load = new CatLoadingView();
        load.show(getSupportFragmentManager(), "");

        WebView webView = findViewById(R.id.webview);
        webView.loadUrl("http://gamemobileglobal.com/hoingu/rank.php");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                load.dismiss();
                super.onPageFinished(view, url);
            }
        });

        /*findViewById(R.id.img_back).setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.mainActivity.setStartNew(false);
                isStartNew = true;

                switch (Config.index) {
                    case 1:
                        startActivity(new Intent(LeaderboardActivity.this, MainActivity.class));
                        break;

                    case 2:
                        Bundle bundle = new Bundle();
                        int index = getIntent().getExtras().getInt("index");
                        bundle.putInt("index", index);
                        Intent intent = new Intent(new Intent(LeaderboardActivity.this, GameActivity.class));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;

                    case 3:
                        startActivity(new Intent(LeaderboardActivity.this, GameOverActivity.class));
                        break;
                }
                finish();
            }
        }, 10, getApplicationContext()));*/
    }

    @Override
    public void onBackPressed() {
        Config.mainActivity.setStartNew(false);
        isStartNew = true;

        switch (Config.index) {
            case 1:
                startActivity(new Intent(LeaderboardActivity.this, MainActivity.class));
                break;

            case 2:
                Bundle bundle = new Bundle();
                int index = getIntent().getExtras().getInt("index");
                bundle.putInt("index", index);
                Intent intent = new Intent(new Intent(LeaderboardActivity.this, GameActivity.class));
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case 3:
                startActivity(new Intent(LeaderboardActivity.this, GameOverActivity.class));
                break;
        }
        finish();
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
}
