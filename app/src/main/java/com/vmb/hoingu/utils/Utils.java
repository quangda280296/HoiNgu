package com.vmb.hoingu.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.vmb.hoingu.activity.GameActivity;
import com.vmb.hoingu.config.Config;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by keban on 3/6/2018.
 */

public class Utils {
    public static void shortToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void longToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void shortSnackbar(Activity activity, String text) {
        Snackbar.make(activity.findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT).show();
    }

    public static void longSnackbar(Activity activity, String text) {
        Snackbar.make(activity.findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG).show();
    }

    public static void resetData() {
        Config.da_hoi.clear();

        Config.countTurn = 3;
        Config.score = 0;

        Config.countStar = 0;
        Config.requireStar = 2;

        Config.canRoll = false;
        Config.isShowAddRoll = false;
        Config.isflying = false;
    }

    public static int rand(int min, int max) {
        try {
            Random rn = new Random();
            int range = max - min + 1;
            int randomNum = min + rn.nextInt(range);
            return randomNum;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int getRand(int max) {
        boolean isDone = false;
        boolean isExist = false;
        int rand = 1;

        if (Config.da_hoi.size() == max) {
            Config.da_hoi = new ArrayList<>();
            rand = Utils.rand(0, max - 1);
            isDone = true;
        }

        while (!isDone) {
            rand = Utils.rand(0, max - 1);

            for (Integer d : Config.da_hoi)
                if (d.equals(rand))
                    isExist = true;

            if (!isExist)
                isDone = true;
        }

        Config.da_hoi.add(rand);
        return rand;
    }

    public static Bitmap takeScreenshot(Activity activity) {
        View rootView = activity.findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        Toast.makeText(activity, "Screenshot taken", Toast.LENGTH_SHORT).show();
        return rootView.getDrawingCache();
    }

    /*public static void saveBitmap(Bitmap bitmap, Context context) {
        String str = Environment.getExternalStorageDirectory() + "/screenshot.png";
        File imagePath = new File(str);
        FileOutputStream fos;

        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(str)));
            intent.setType("image/jpeg");
            context.startActivity(Intent.createChooser(intent, "send"));

        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }*/

    public static void showRewardedVideo(Context context) {
        if(!Config.isRequestLoadAd) {
            Log.d("ooooooo", "faileddddd");
            return;
        }

        if (!Config.adVideo.isLoaded()) {
            Utils.shortToast(context, "Hiện không có Video nhận thưởng nào !");
            Config.adVideo.loadAd(Config.video_ads, new AdRequest.Builder().build());
            Log.d("ooooooo", Config.video_ads);
        }
        else {
            Config.adVideo.show();
            Config.mediaPlayer.pause();
            Log.d("ooooooo", Config.video_ads);
            Log.d("ooooooo", "upppppppppp");
        }
    }

    public static void showAdPopup() {
        if(!Config.isRequestLoadAd)
            return;

        Calendar calendar = Calendar.getInstance();
        int end = ((calendar.get(Calendar.YEAR) * 365 + calendar.get(Calendar.DAY_OF_YEAR)) * 24
                + calendar.get(Calendar.HOUR_OF_DAY)) * 3600 + calendar.get(Calendar.MINUTE) * 60 + calendar.get(Calendar.SECOND);

        Log.d("tttttt", "time_start=" + (end - Config.time_start) + " / time_offset=" + (end - Config.time_end));

        if (end - Config.time_start >= Config.time_start_show_popup)
            if (end - Config.time_end >= Config.offset_time_show_popup) {
                calendar = Calendar.getInstance();
                Config.time_end = ((calendar.get(Calendar.YEAR) * 365 + calendar.get(Calendar.DAY_OF_YEAR)) * 24
                        + calendar.get(Calendar.HOUR_OF_DAY)) * 3600 + calendar.get(Calendar.MINUTE) * 60 + calendar.get(Calendar.SECOND);

                Config.iad.show();

                // Load ads into Interstitial Ads
                AdRequest adRequest = new AdRequest.Builder().build();
                Config.iad.loadAd(adRequest);
            }
    }

    public static void showAd(Context context, RelativeLayout layout, final FrameLayout layout_ads) {
        if(!Config.isRequestLoadAd)
            return;

        AdView banner = new AdView(context);
        banner.setAdSize(AdSize.SMART_BANNER);
        banner.setAdUnitId(Config.banner_ads);

        layout.addView(banner);

        // load advertisement
        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);

        banner.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                layout_ads.setVisibility(View.GONE);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                //banner.loadAd(adRequest);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                layout_ads.setVisibility(View.GONE);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                layout_ads.setVisibility(View.VISIBLE);
                Log.d("uuuuuuuuuuuuu", "Okkkkkkkkkk");
            }
        });
    }

    // handleInterstitialAd
    public static void handleInterstitialAd(Context context) {
        // load ad
        Config.iad = new InterstitialAd(context);
        // set the ad unit ID
        Config.iad.setAdUnitId(Config.popup_ads);
        AdRequest adRequest = new AdRequest.Builder().build();
        // Load ads into Interstitial Ads
        Config.iad.loadAd(adRequest);
        Config.iad.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                //Config.iad.loadAd(adRequest);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

        });
    }


    // handleRewardedVideoAd
    public static void handleRewardedVideoAd(Context context) {
        // load ad
        Config.adVideo = MobileAds.getRewardedVideoAdInstance(context);
        // Load ads into Rewarded Video Ad
        Config.adVideo.loadAd(Config.video_ads, new AdRequest.Builder().build());

        Config.adVideo.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                Config.mediaPlayer.start();
                Config.adVideo.loadAd(Config.video_ads, new AdRequest.Builder().build());
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                Config.countVideo--;
                Config.countTurn++;
                Config.mediaPlayer.start();
                Config.adVideo.loadAd(Config.video_ads, new AdRequest.Builder().build());
                Config.activity.startActivity(new Intent(Config.activity, GameActivity.class));
                Config.activity.finish();
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Log.d("errroru", "code=" + i);
                //Config.adVideo.loadAd(Config.video_ads, new AdRequest.Builder().build());
            }
        });
    }

    public static void flipit(View viewToFlip) {
        ObjectAnimator flip = ObjectAnimator.ofFloat(viewToFlip, "rotationY", 0f, 360f);
        flip.setRepeatMode(ValueAnimator.REVERSE);
        flip.setRepeatCount(ValueAnimator.INFINITE);
        flip.setDuration(3000);
        flip.start();
    }
}
