package com.vmb.hoingu.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import com.vmb.hoingu.MainApplication;
import com.vmb.hoingu.R;
import com.vmb.hoingu.config.Config;
import com.vmb.hoingu.listener.OnTouchClickListener;
import com.vmb.hoingu.utils.Check;
import com.vmb.hoingu.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private int countBack = 0;
    private boolean canTouchBottom = true;
    private Handler handler = new Handler();

    private boolean isStartNew = false;
    private ImageView img_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainApplication mainApplication = (MainApplication) getApplication();
        Config.mainActivity = MainActivity.this;

        findViewById(R.id.choose_package_view).setVisibility(View.GONE);
        findViewById(R.id.ranking).setVisibility(View.GONE);

        FrameLayout layout_ads = findViewById(R.id.layout_ads);
        RelativeLayout adView = findViewById(R.id.adView);
        Utils.showAd(getApplicationContext(), adView, layout_ads);

        // set fullscreen
        FrameLayout root = findViewById(R.id.root);
        root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        Button btn_title_choose_package = findViewById(R.id.btn_title_choose_package);
        btn_title_choose_package.setTypeface(mainApplication.getFontBold());

        Button btn_title_ranking = findViewById(R.id.btn_title_ranking);
        btn_title_ranking.setTypeface(mainApplication.getFontBold());

        handleSound();
        handleClickMenu();
        handleClickRanking();
        handleClickButton();
    } //onCreate


    public void handleSound() {
        Config.mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.trollmusic);
        Config.mediaPlayer.setLooping(true);
        Config.mediaPlayer.start();

        img_sound = findViewById(R.id.img_sound);
        if (Config.isPlayBackgroundMusic)
            img_sound.setImageResource(R.mipmap.ic_sound_on);
        else
            img_sound.setImageResource(R.mipmap.ic_sound_off);

        findViewById(R.id.img_sound).setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canTouchBottom)
                    return;

                if (Config.isPlayBackgroundMusic) {
                    Config.mediaPlayer.pause();
                    Config.isPlayBackgroundMusic = false;
                    img_sound.setImageResource(R.mipmap.ic_sound_off);
                } else {
                    Config.mediaPlayer.start();
                    Config.isPlayBackgroundMusic = true;
                    img_sound.setImageResource(R.mipmap.ic_sound_on);
                }
                //perform onClick
            }
        }, 20, getApplicationContext()));
    } //handleSound


    public void handleClickMenu() {
        findViewById(R.id.img_menu).setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canTouchBottom)
                    return;

                canTouchBottom = false;
                YoYo.with(Techniques.BounceInDown).playOn(findViewById(R.id.choose_package_view));
                findViewById(R.id.choose_package_view).setVisibility(View.VISIBLE);
                //perform onClick
            }
        }, 20, getApplicationContext()));

        findViewById(R.id.btn_hoi_ngu).setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.category = "hoi_ngu";
                Utils.resetData();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isStartNew = true;
                        startActivity(new Intent(MainActivity.this, GameActivity.class));

                        if (findViewById(R.id.choose_package_view).getVisibility() == View.VISIBLE) {
                            canTouchBottom = true;
                            findViewById(R.id.choose_package_view).setVisibility(View.GONE);
                        }

                        if (findViewById(R.id.ranking).getVisibility() == View.VISIBLE) {
                            canTouchBottom = true;
                            findViewById(R.id.ranking).setVisibility(View.GONE);
                        }

                        countBack = 0;
                    }
                }, 100);
                //perform onClick
            }
        }, 20, getApplicationContext()));

        findViewById(R.id.btn_cac_thanh).setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.category = "cac_thanh";
                Utils.resetData();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isStartNew = true;
                        startActivity(new Intent(MainActivity.this, GameActivity.class));

                        if (findViewById(R.id.choose_package_view).getVisibility() == View.VISIBLE) {
                            canTouchBottom = true;
                            findViewById(R.id.choose_package_view).setVisibility(View.GONE);
                        }

                        if (findViewById(R.id.ranking).getVisibility() == View.VISIBLE) {
                            canTouchBottom = true;
                            findViewById(R.id.ranking).setVisibility(View.GONE);
                        }

                        countBack = 0;
                    }
                }, 100);
                //perform onClick
            }
        }, 20, getApplicationContext()));
    } //HandleClickMenu


    public void handleClickRanking() {
        findViewById(R.id.btn_ranking).setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canTouchBottom)
                    return;

                canTouchBottom = false;
                YoYo.with(Techniques.BounceInDown).playOn(findViewById(R.id.ranking));
                findViewById(R.id.ranking).setVisibility(View.VISIBLE);
                //perform onClick
            }
        }, 20, getApplicationContext()));

        findViewById(R.id.btn_join).setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Hiện không có sự kiện đua top nào !")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();

                dialog.show();
                //perform onClick
            }
        }, 20, getApplicationContext()));
    } //HandleClickRanking


    public void handleClickButton() {
        findViewById(R.id.btn_play).setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canTouchBottom)
                    return;

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isStartNew = true;
                        startActivity(new Intent(MainActivity.this, GameActivity.class));
                    }
                }, 100);
                //perform onClick
            }
        }   , 20, getApplicationContext()));

        findViewById(R.id.btn_leaderboard).setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canTouchBottom)
                    return;

                isStartNew = true;
                Config.index = 1;
                startActivity(new Intent(MainActivity.this, LeaderboardActivity.class));
                //perform onClick
            }
        }, 20, getApplicationContext()));

        findViewById(R.id.btn_add).setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canTouchBottom)
                    return;

                if (!Check.checkInternetConnection(getApplicationContext()))
                    return;

                isStartNew = true;
                startActivity(new Intent(MainActivity.this, AddQuestionActivity.class));
                //perform onClick
            }
        }, 20, getApplicationContext()));
    } //handleClickButton


    @Override
    protected void onResume() {
        super.onResume();
        // set fullscreen
        FrameLayout root = findViewById(R.id.root);
        root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        if (!Config.mediaPlayer.isPlaying())
            if (Config.isPlayBackgroundMusic)
                Config.mediaPlayer.start();
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.choose_package_view).getVisibility() == View.VISIBLE) {
            this.canTouchBottom = true;
            findViewById(R.id.choose_package_view).setVisibility(View.GONE);
            return;
        }

        if (findViewById(R.id.ranking).getVisibility() == View.VISIBLE) {
            this.canTouchBottom = true;
            findViewById(R.id.ranking).setVisibility(View.GONE);
            return;
        }

        this.countBack++;
        if (this.countBack == 2)
            finish();
        else
            Utils.longToast(getApplicationContext(), getString(R.string.press_back_again));
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

    public void setStartNew(boolean startNew) {
        isStartNew = startNew;
    }

    public void setSound(boolean isPlaying) {
        if (isPlaying)
            img_sound.setImageResource(R.mipmap.ic_sound_on);
        else
            img_sound.setImageResource(R.mipmap.ic_sound_off);
    }
}
