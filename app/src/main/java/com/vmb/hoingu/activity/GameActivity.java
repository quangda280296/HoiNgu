package com.vmb.hoingu.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.vmb.hoingu.MainApplication;
import com.vmb.hoingu.R;
import com.vmb.hoingu.config.Config;
import com.vmb.hoingu.listener.OnTouchClickListener;
import com.vmb.hoingu.utils.Check;
import com.vmb.hoingu.utils.PlayMusic;
import com.vmb.hoingu.utils.Utils;

public class GameActivity extends AppCompatActivity {

    private MainApplication mainApplication;

    private boolean isStartNew = false;
    private boolean canTouchBottom = true;
    private boolean isRollEnd = true;

    private String dap_an;
    private String giai_thich;

    private int rand;
    private int random = -1;
    private int countBack = 0;

    private TextView lbl_count;
    private TextView lbl_author;
    private TextView lbl_left;
    private TextView lbl_number;

    private ImageView img_star;
    private ImageView img_back;
    private ImageView img_sound;
    private ImageView img_leaderboard;
    private ImageView img_share;
    private ImageView img_flying;

    private ImageView btn_share_fb;
    private ImageView btn_cancel;

    private Button btn_question;
    private Button btn_a;
    private Button btn_b;
    private Button btn_c;
    private Button btn_d;

    private Handler handler;
    private Bitmap bitmap;

    private Animation rotate;
    private Animation zoom_and_move;
    private Animation blink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        /*// set fullscreen
        FrameLayout root = findViewById(R.id.root);
        root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);*/

        handler = new Handler();

        /*FrameLayout layout_ads = findViewById(R.id.layout_ads);
        RelativeLayout adView = findViewById(R.id.adView);
        Utils.showAd(getApplicationContext(), adView, layout_ads);*/

        mainApplication = (MainApplication) getApplication();

        lbl_author = findViewById(R.id.lbl_author);
        lbl_author.setTypeface(mainApplication.getFontSuperBold());

        lbl_number = findViewById(R.id.lbl_number);
        lbl_number.setTypeface(mainApplication.getFontSuperBold());

        btn_question = findViewById(R.id.btn_question);

        // Handle sound
        PlayMusic.playPop(getApplicationContext());
        img_sound = findViewById(R.id.img_sound);
        if (Config.isPlayBackgroundMusic)
            img_sound.setImageResource(R.mipmap.ic_sound_on);
        else
            img_sound.setImageResource(R.mipmap.ic_sound_off);

        // Handle text and emotion
        blink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        if (Config.isShowAddRoll) {
            findViewById(R.id.lbl_roll_round_turn).setVisibility(View.VISIBLE);
            findViewById(R.id.lbl_roll_round_turn).startAnimation(blink);
        }

        lbl_left = findViewById(R.id.lbl_left);
        lbl_left.setTypeface(mainApplication.getFontSuperBold());
        lbl_left.setText("Ngu: " + Config.countTurn);

        lbl_count = findViewById(R.id.lbl_count);
        lbl_count.setTypeface(mainApplication.getFontSuperBold());
        lbl_count.setText(Config.countStar + "/" + Config.requireStar);

        img_flying = findViewById(R.id.img_flying);
        img_star = findViewById(R.id.img_star);

        /*ImageView img = findViewById(R.id.img);
        img.setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic.playPunch(getApplicationContext());
                //perform onClick
            }
        }, 5, getApplicationContext()));*/

        btn_a = findViewById(R.id.btn_a);
        btn_b = findViewById(R.id.btn_b);
        btn_c = findViewById(R.id.btn_c);
        btn_d = findViewById(R.id.btn_d);

        // Handle data
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            random = bundle.getInt("index", -1);

        if (Config.category.equals("hoi_ngu")) {
            Cursor cursor = Config.hoi_ngu.getCursor();

            if (random == -1)
                random = Utils.getRand(cursor.getCount());

            cursor.moveToPosition(random);
            getDataHoingu(cursor);

        } else {
            Cursor cursor = Config.cac_thanh.getCursor();
            if (random == -1)
                random = Utils.getRand(cursor.getCount());

            cursor.moveToPosition(random);
            getDataCacthanh(cursor);
        }

        handleAnswer();
        handleClickButton();
        handleClickShare();
        handleMenu();
        handleRoll();
    } // onCreate

    @Override
    protected void onStart() {
        super.onStart();

        if (!Config.canRoll)
            return;

        img_star.setImageResource(R.mipmap.ic_star);
        Utils.flipit(img_star);

        if (Config.isflying)
            return;

        zoom_and_move = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_and_move);
        zoom_and_move.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                findViewById(R.id.flying_star_view).setVisibility(View.GONE);
                canTouchBottom = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        canTouchBottom = false;
        findViewById(R.id.star).startAnimation(zoom_and_move);
        findViewById(R.id.flying_star_view).setVisibility(View.VISIBLE);
        Config.isflying = true;
    } // onStart

    public void handleRoll() {
        // set Roll
        findViewById(R.id.layout_star).setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                countBack = 0;

                if (!canTouchBottom)
                    return;

                if (!Config.canRoll) {
                    Utils.shortSnackbar(GameActivity.this, "Bạn cần vượt qua thêm nhiều câu hỏi hơn để có thể sử dụng Vòng Quay May Mắn");
                    return;
                }

                canTouchBottom = false;
                YoYo.with(Techniques.BounceInDown).playOn(findViewById(R.id.roll_round_view));
                findViewById(R.id.roll_round_view).setVisibility(View.VISIBLE);
                //perform onClick
            }
        }, 20, getApplicationContext()));

        findViewById(R.id.btn_roll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Config.canRoll)
                    return;

                if (!isRollEnd)
                    return;

                isRollEnd = false;
                firstAnimation();
            }
        });

        findViewById(R.id.btn_watch_rewardList).setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                //perform onClick
            }
        }, 20, getApplicationContext()));
    } //handleRoll


    public void firstAnimation() {
        rand = Utils.rand(1, 8);

        switch (rand) {
            case 1:
                rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_1);
                img_flying.setImageResource(R.mipmap.txt_xit_roi_1);
                break;
            case 2:
                rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_2);
                img_flying.setImageResource(R.mipmap.txt_mat_luot_1);
                break;
            case 3:
                rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_3);
                img_flying.setImageResource(R.mipmap.txt_them_2_luot);
                break;
            case 4:
                rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_4);
                img_flying.setImageResource(R.mipmap.txt_xit_roi_2);
                break;
            case 5:
                rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_5);
                img_flying.setImageResource(R.mipmap.txt_mat_luot_2);
                break;
            case 6:
                rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_6);
                img_flying.setImageResource(R.mipmap.txt_dac_biet);
                break;
            case 7:
                rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_7);
                img_flying.setImageResource(R.mipmap.txt_them_1_luot);
                break;
            case 8:
                rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_8);
                img_flying.setImageResource(R.mipmap.txt_truot);
                break;
        }

        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                secondAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        findViewById(R.id.img_wheel).startAnimation(rotate);
    } // firstAnimation


    public void secondAnimation() {
        img_flying.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.ZoomOutUp)
                .duration(3000)
                .playOn(findViewById(R.id.img_flying));

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                img_flying.setVisibility(View.GONE);
                handleResultRollRound();
            }
        }, 1000);
    } // secondAnimation


    public void handleResultRollRound() {
        img_star.setImageResource(R.mipmap.ic_white_star);
        img_star.clearAnimation();

        boolean isOK = true;

        Config.countStar = 0;
        if (Config.requireStar == 2)
            Config.requireStar = 4;
        lbl_count.setText(Config.countStar + "/" + Config.requireStar);

        switch (rand) {
            case 1:
                break;

            case 2:
                Config.countTurn--;
                if (Config.countTurn == 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString("flag", "lost");

                    Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
                    intent.putExtras(bundle);

                    isStartNew = true;

                    startActivity(intent);
                    finish();
                }

                break;

            case 3:
                Config.countTurn += 2;
                lbl_left.setText("Ngu: " + Config.countTurn);
                break;

            case 4:
                break;

            case 5:
                Config.countTurn--;
                if (Config.countTurn == 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString("flag", "lost");

                    Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
                    intent.putExtras(bundle);

                    isStartNew = true;

                    startActivity(intent);
                    finish();
                }

                break;

            case 6:
                isOK = false;
                findViewById(R.id.child).setVisibility(View.VISIBLE);
                findViewById(R.id.img_light).setVisibility(View.VISIBLE);
                YoYo.with(Techniques.ZoomIn).playOn(findViewById(R.id.img_light));

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.img_gift_box).setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.ZoomIn).playOn(findViewById(R.id.img_gift_box));
                    }
                }, 1000);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.img_light).setVisibility(View.GONE);
                        findViewById(R.id.img_gift_box).setVisibility(View.GONE);

                        findViewById(R.id.img_luot_quay).setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.Wobble)
                                .repeat(ValueAnimator.INFINITE)
                                .repeatMode(ValueAnimator.REVERSE)
                                .playOn(findViewById(R.id.img_luot_quay));
                    }
                }, 2000);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.child).setVisibility(View.GONE);
                        findViewById(R.id.img_luot_quay).setVisibility(View.GONE);

                        findViewById(R.id.lbl_roll_round_turn).setVisibility(View.VISIBLE);
                        findViewById(R.id.lbl_roll_round_turn).startAnimation(blink);

                        Config.isShowAddRoll = true;
                        Config.canRoll = true;
                        isRollEnd = true;
                    }
                }, 4000);

                break;

            case 7:
                Config.countTurn++;
                lbl_left.setText("Ngu: " + Config.countTurn);
                break;

            case 8:
                isOK = false;
                isRollEnd = false;
                firstAnimation();
                break;
        }

        if (!isOK)
            return;

        if (rand != 6) {
            Config.isShowAddRoll = false;
            findViewById(R.id.lbl_roll_round_turn).setVisibility(View.GONE);
            findViewById(R.id.lbl_roll_round_turn).clearAnimation();
        }

        isRollEnd = true;
        canTouchBottom = true;
        findViewById(R.id.roll_round_view).setVisibility(View.GONE);
        Config.canRoll = false;
    } // handleResultRollRound


    public void handleMenu() {
        findViewById(R.id.img_menu).setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canTouchBottom)
                    return;

                countBack = 0;

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
                isStartNew = true;

                finish();
                startActivity(new Intent(GameActivity.this, GameActivity.class));
                //perform onClick
            }
        }, 20, getApplicationContext()));

        findViewById(R.id.btn_cac_thanh).setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.category = "cac_thanh";
                Utils.resetData();
                isStartNew = true;

                finish();
                startActivity(new Intent(GameActivity.this, GameActivity.class));
                //perform onClick
            }
        }, 20, getApplicationContext()));
    } // handleMenu


    public void handleClickShare() {
        // set Share
        img_share = findViewById(R.id.img_share);
        img_share.setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                countBack = 0;

                if (!canTouchBottom)
                    return;

                if (!Check.checkInternetConnection(getApplicationContext()))
                    return;

                FrameLayout layout_ads = findViewById(R.id.layout_ads);
                layout_ads.setVisibility(View.INVISIBLE);

                bitmap = Utils.takeScreenshot(GameActivity.this);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        canTouchBottom = false;
                        layout_ads.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.BounceInDown).playOn(findViewById(R.id.share_fb_view));
                        findViewById(R.id.share_fb_view).setVisibility(View.VISIBLE);
                    }
                }, 500);
            }
        }, 20, getApplicationContext()));

        btn_share_fb = findViewById(R.id.btn_share_fb);
        btn_share_fb.setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(bitmap)
                        .setCaption("Giúp mình câu này với mọi người ơi !!!")
                        .build();

                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();

                // this part is optional
                CallbackManager callbackManager = CallbackManager.Factory.create();

                ShareDialog shareDialog = new ShareDialog(GameActivity.this);
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Utils.shortToast(getApplicationContext(), getString(R.string.share_successfull));
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Utils.shortToast(getApplicationContext(), "Cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Utils.shortToast(getApplicationContext(), getString(R.string.please_try_again));
                    }
                });// registerCallback

                shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
                //perform onClick
            }
        }, 20, getApplicationContext()));

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                canTouchBottom = true;
                findViewById(R.id.share_fb_view).setVisibility(View.GONE);
            }
        }, 20, getApplicationContext()));
    } //HandleClickShare


    public void handleClickButton() {
        // set Back
        img_back = findViewById(R.id.img_back);
        img_back.setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canTouchBottom)
                    return;

                Utils.resetData();
                isStartNew = true;
                Config.mainActivity.setStartNew(false);
                Config.mainActivity.setSound(Config.isPlayBackgroundMusic);
                finish();
                //perform onClick
            }
        }, 20, getApplicationContext()));

        // set Sound
        img_sound = findViewById(R.id.img_sound);
        img_sound.setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                countBack = 0;

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

        // set Leaderboard
        img_leaderboard = findViewById(R.id.img_leaderboard);
        img_leaderboard.setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                countBack = 0;

                if (!canTouchBottom)
                    return;

                isStartNew = true;
                Config.index = 2;

                Intent intent = new Intent(GameActivity.this, LeaderboardActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("index", random);
                intent.putExtras(bundle);

                startActivity(intent);
                finish();
                //perform onClick
            }
        }, 20, getApplicationContext()));
    } //handleClickButton


    public void handleAnswer() {
        btn_a.setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                countBack = 0;

                if (!canTouchBottom)
                    return;

                if (!dap_an.equals("a"))
                    lose();
                else
                    win();
                //perform onClick
            }
        }, 20, getApplicationContext()));

        btn_b.setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                countBack = 0;

                if (!canTouchBottom)
                    return;

                if (!dap_an.equals("b"))
                    lose();
                else
                    win();
                //perform onClick
            }
        }, 20, getApplicationContext()));

        btn_c.setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                countBack = 0;

                if (!canTouchBottom)
                    return;

                if (!dap_an.equals("c"))
                    lose();
                else
                    win();
                //perform onClick
            }
        }, 20, getApplicationContext()));

        btn_d.setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                countBack = 0;

                if (!canTouchBottom)
                    return;

                if (!dap_an.equals("d"))
                    lose();
                else
                    win();
                //perform onClick
            }
        }, 20, getApplicationContext()));
    } //handleAnswer


    public void lose() {
        Config.countTurn--;

        if (Config.countTurn == 0) {
            Bundle bundle = new Bundle();
            bundle.putString("flag", "lose");

            switch (dap_an) {
                case "a":
                    bundle.putString("dap_an", btn_a.getText().toString());
                    break;
                case "b":
                    bundle.putString("dap_an", btn_b.getText().toString());
                    break;
                case "c":
                    bundle.putString("dap_an", btn_c.getText().toString());
                    break;
                case "d":
                    bundle.putString("dap_an", btn_d.getText().toString());
                    break;
            }

            Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
            intent.putExtras(bundle);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isStartNew = true;

                    startActivity(intent);
                    finish();
                }
            }, 100);

        } else {
            PlayMusic.playUhOh(getApplicationContext());
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom);
            lbl_left.setText("Ngu: " + Config.countTurn);
            lbl_left.startAnimation(anim);
        }
    }

    public void win() {
        if (Config.countStar < Config.requireStar) {
            Config.countStar++;

            if (Config.countStar == Config.requireStar) {
                Config.canRoll = true;
                Config.isflying = false;
            }
        }

        Config.score++;
        if (Config.highScore < Config.score) {
            Config.highScore = Config.score;
            SharedPreferences mySharedPreferences = getSharedPreferences(Config.MYPREFS, Config.mode);
            SharedPreferences.Editor myEditor = mySharedPreferences.edit();
            myEditor.putString("highScore", Config.highScore + "");
            myEditor.putBoolean("isUpdated", false);
            myEditor.commit();
            Config.isUpdated = false;
        }

        PlayMusic.playCorrect(getApplicationContext());

        Bundle bundle = new Bundle();
        bundle.putString("flag", "win");
        bundle.putString("giai_thich", giai_thich);

        Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
        intent.putExtras(bundle);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isStartNew = true;
                startActivity(intent);
                finish();
            }
        }, 100);
    }

    public void getDataHoingu(Cursor cursor) {
        lbl_number.setText("Câu: " + (Config.score + 1) + "/" + cursor.getCount());
        btn_question.setText(Config.hoi_ngu.getCauhoi(cursor));

        if (Config.hoi_ngu.getNick(cursor) == null)
            lbl_author.setText("Của: Assmin");
        else if (Config.hoi_ngu.getNick(cursor).equals(""))
            lbl_author.setText("Của: Assmin");
        else
            lbl_author.setText("Của: " + Config.hoi_ngu.getNick(cursor));

        dap_an = Config.hoi_ngu.getDapan(cursor);
        giai_thich = Config.hoi_ngu.getGiaithich(cursor);

        //Utils.shortToast(getApplicationContext(), "Đáp án: " + dap_an);

        btn_a.setText(Config.hoi_ngu.getA(cursor));
        btn_b.setText(Config.hoi_ngu.getB(cursor));
        btn_c.setText(Config.hoi_ngu.getC(cursor));
        btn_d.setText(Config.hoi_ngu.getD(cursor));
    }

    public void getDataCacthanh(Cursor cursor) {
        lbl_number.setText("Câu: " + (Config.score + 1) + "/" + cursor.getCount());
        btn_question.setText(Config.cac_thanh.getCauhoi(cursor));

        if (Config.cac_thanh.getNick(cursor) == null)
            lbl_author.setText("Của: Assmin");
        else if (Config.hoi_ngu.getNick(cursor).equals(""))
            lbl_author.setText("Của: Assmin");
        else
            lbl_author.setText("Của: " + Config.cac_thanh.getNick(cursor));

        dap_an = Config.cac_thanh.getDapan(cursor);
        giai_thich = Config.cac_thanh.getGiaithich(cursor);

        //Utils.shortToast(getApplicationContext(), "Đáp án: " + dap_an);

        btn_a.setText(Config.cac_thanh.getA(cursor));
        btn_b.setText(Config.cac_thanh.getB(cursor));
        btn_c.setText(Config.cac_thanh.getC(cursor));
        btn_d.setText(Config.cac_thanh.getD(cursor));
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.choose_package_view).getVisibility() == View.VISIBLE) {
            this.canTouchBottom = true;
            findViewById(R.id.choose_package_view).setVisibility(View.GONE);
            return;
        }

        if (findViewById(R.id.share_fb_view).getVisibility() == View.VISIBLE) {
            this.canTouchBottom = true;
            findViewById(R.id.share_fb_view).setVisibility(View.GONE);
            return;
        }

        if (findViewById(R.id.roll_round_view).getVisibility() == View.VISIBLE) {
            if (!isRollEnd)
                return;

            this.canTouchBottom = true;
            findViewById(R.id.roll_round_view).setVisibility(View.GONE);
            return;
        }

        this.countBack++;
        if (this.countBack == 2) {
            Utils.resetData();
            isStartNew = true;
            Config.mainActivity.setStartNew(false);
            Config.mainActivity.setSound(Config.isPlayBackgroundMusic);
            finish();
        } else
            Utils.longToast(getApplicationContext(), getString(R.string.press_back_again));
    }

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
    protected void onPause() {
        super.onPause();

        if (!isStartNew)
            Config.mediaPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
