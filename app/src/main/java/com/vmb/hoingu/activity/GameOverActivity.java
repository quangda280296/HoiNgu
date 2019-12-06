package com.vmb.hoingu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vmb.hoingu.R;
import com.vmb.hoingu.config.Config;
import com.vmb.hoingu.listener.OnTouchClickListener;
import com.vmb.hoingu.utils.PlayMusic;
import com.vmb.hoingu.utils.Utils;

public class GameOverActivity extends AppCompatActivity {

    private boolean isStartNew = false;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Config.activity = GameOverActivity.this;

        // set fullscreen
        LinearLayout root_view = findViewById(R.id.root_view);
        root_view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        TextView lbl_noti = findViewById(R.id.lbl_noti);
        lbl_noti.setText("Xem hết video để nhận thêm lượt chơi nữa, bạn nhé!\nBạn còn " + Config.countVideo + " lượt xem");

        if (!Config.isRequestLoadAd) {
            findViewById(R.id.btn_watch).setVisibility(View.GONE);
            findViewById(R.id.lbl_noti).setVisibility(View.GONE);
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("flag").equals("win"))
            win(bundle.getString("giai_thich", ""));
        else if (bundle.getString("flag").equals("lose"))
            lose(bundle.getString("dap_an", ""));
        else
            lost();

        //Utils.showAdPopup();
    }

    // lose
    public void lost() {
        /*FrameLayout layout_ads = findViewById(R.id.layout_ads_1);
        RelativeLayout adView = findViewById(R.id.adView_1);
        Utils.showAd(getApplicationContext(), adView, layout_ads);*/

        LinearLayout win = findViewById(R.id.win);
        LinearLayout lose = findViewById(R.id.lose);

        win.setVisibility(View.GONE);
        lose.setVisibility(View.VISIBLE);

        //showSpotlight(findViewById(R.id.img_upload_1));
        findViewById(R.id.btn_upload).setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartNew = true;
                Config.index = 3;
                startActivity(new Intent(GameOverActivity.this, LoginActivity.class));
                //perform onClick
            }
        }, 20, GameOverActivity.this));

        findViewById(R.id.btn_replay).setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.resetData();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isStartNew = true;
                        startActivity(new Intent(GameOverActivity.this, GameActivity.class));
                        finish();
                    }
                }, 100);
                //perform onClick
            }
        }, 20, GameOverActivity.this));

        findViewById(R.id.btn_menu).setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.resetData();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isStartNew = true;
                        startActivity(new Intent(GameOverActivity.this, MainActivity.class));
                        finish();
                    }
                }, 100);
                //perform onClick
            }
        }, 20, GameOverActivity.this));

        findViewById(R.id.btn_watch).setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (Config.countVideo > 0)
                    Utils.showRewardedVideo(getApplicationContext());
                else*/
                    Utils.shortSnackbar(GameOverActivity.this, "Bạn đã hết lượt xem video !!!");
                //perform onClick
            }
        }, 20, GameOverActivity.this));

        TextView lbl_dapan = findViewById(R.id.lbl_dapan);
        lbl_dapan.setText("Rất tiếc, bạn quá đen !!! ;)))))");

        TextView lbl_score = findViewById(R.id.lbl_score);
        lbl_score.setText("Điểm: " + Config.score);

        TextView lbl_highscore = findViewById(R.id.lbl_highscore);
        lbl_highscore.setText("Điểm cao nhất: " + Config.highScore);

        PlayMusic.playSad(getApplicationContext());
    }

    // lose
    public void lose(String dapan) {
        /*FrameLayout layout_ads = findViewById(R.id.layout_ads_1);
        RelativeLayout adView = findViewById(R.id.adView_1);
        Utils.showAd(getApplicationContext(), adView, layout_ads);*/

        LinearLayout win = findViewById(R.id.win);
        LinearLayout lose = findViewById(R.id.lose);

        win.setVisibility(View.GONE);
        lose.setVisibility(View.VISIBLE);

        //showSpotlight(findViewById(R.id.img_upload_1));
        //showSpotlight(findViewById(R.id.img_upload_1));
        findViewById(R.id.btn_upload).setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartNew = true;
                Config.index = 3;
                startActivity(new Intent(GameOverActivity.this, LoginActivity.class));
                //perform onClick
            }
        }, 20, GameOverActivity.this));

        findViewById(R.id.btn_replay).setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.resetData();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isStartNew = true;
                        startActivity(new Intent(GameOverActivity.this, GameActivity.class));
                        finish();
                    }
                }, 100);
                //perform onClick
            }
        }, 20, GameOverActivity.this));

        findViewById(R.id.btn_menu).setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.resetData();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isStartNew = true;
                        startActivity(new Intent(GameOverActivity.this, MainActivity.class));
                        finish();
                    }
                }, 100);
                //perform onClick
            }
        }, 20, GameOverActivity.this));

        findViewById(R.id.btn_watch).setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (Config.countVideo > 0)
                    Utils.showRewardedVideo(getApplicationContext());
                else*/
                    Utils.shortSnackbar(GameOverActivity.this, "Bạn đã hết lượt xem video !!!");
                //perform onClick
            }
        }, 20, GameOverActivity.this));

        TextView lbl_dapan = findViewById(R.id.lbl_dapan);
        lbl_dapan.setText("Đáp án: " + dapan);

        TextView lbl_score = findViewById(R.id.lbl_score);
        lbl_score.setText("Điểm: " + Config.score);

        TextView lbl_highscore = findViewById(R.id.lbl_highscore);
        lbl_highscore.setText("Điểm cao nhất: " + Config.highScore);

        PlayMusic.playSad(getApplicationContext());
    }

    // win
    public void win(String giaithich) {
        /*FrameLayout layout_ads = findViewById(R.id.layout_ads_2);
        RelativeLayout adView = findViewById(R.id.adView_2);
        Utils.showAd(getApplicationContext(), adView, layout_ads);*/

        LinearLayout win = findViewById(R.id.win);
        LinearLayout lose = findViewById(R.id.lose);

        win.setVisibility(View.VISIBLE);
        lose.setVisibility(View.GONE);

        /*showSpotlight(findViewById(R.id.img_upload_2));
        findViewById(R.id.img_upload_2).setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartNew = true;
                Config.index = 3;
                startActivity(new Intent(GameOverActivity.this, LoginActivity.class));
                //perform onClick
            }
        }, 20, GameOverActivity.this));*/

        if (giaithich.equals("    "))
            giaithich = "Con cái nhà ai mà giỏi thế !!! :))))";

        Button explain = findViewById(R.id.explain);
        explain.setText(giaithich);

        findViewById(R.id.btn_continue).setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isStartNew = true;
                        startActivity(new Intent(GameOverActivity.this, GameActivity.class));
                        finish();
                    }
                }, 100);
                //perform onClick
            }
        }, 20, GameOverActivity.this));
    }

    /*public void showSpotlight(View view) {
        if (!Config.isFirstTime)
            return;

        Config.isFirstTime = false;
        SharedPreferences mySharedPreferences = getSharedPreferences(Config.MYPREFS, Config.mode);
        SharedPreferences.Editor myEditor = mySharedPreferences.edit();
        myEditor.putBoolean("isFirstTime", false);
        myEditor.commit();

        TapTargetView.showFor(GameOverActivity.this, TapTarget.forView(view, "Đồng bộ dữ liệu", "Ấn vào đây để gửi điểm cao của bạn lên đám mây")
                        // All options below are optional
                        .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                        .titleTextSize(20)                  // Specify the size (in sp) of the title text
                        .titleTextColor(R.color.white)      // Specify the color of the title text
                        .descriptionTextSize(15)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(R.color.white)  // Specify the color of the description text
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(true)                  // Whether tapping outside the outer circle dismisses the view
                        .transparentTarget(true),           // Specify whether the target is transparent (displays the content underneath)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        isStartNew = true;
                        startActivity(new Intent(GameOverActivity.this, LoginActivity.class));
                    }
                });
    }*/

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (!isStartNew)
            Config.mediaPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // set fullscreen
        LinearLayout root_view = findViewById(R.id.root_view);
        root_view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        if (!Config.mediaPlayer.isPlaying())
            if (Config.isPlayBackgroundMusic)
                Config.mediaPlayer.start();
    }
}
