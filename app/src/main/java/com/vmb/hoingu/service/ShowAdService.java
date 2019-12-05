package com.vmb.hoingu.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.vmb.hoingu.Interface.ITimeReturn;
import com.vmb.hoingu.config.Config;
import com.vmb.hoingu.utils.GetTimePopup;
import com.vmb.hoingu.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Manh Dang on 01/08/2018.
 */

public class ShowAdService extends Service implements ITimeReturn {

    private String idDevice;
    private String installTime;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("lllllllllll", "OK");

        idDevice = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        PackageManager pm = getApplicationContext().getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Log.d("uuuuuuuuuuuuuuu", getPackageName());

            installTime = dateFormat.format(new Date(packageInfo.firstInstallTime));
            installTime = installTime.replaceAll("\\s", "");
            new GetTimePopup(Config.codeAccessAPI, idDevice, installTime, ShowAdService.this).execute();

        } catch (PackageManager.NameNotFoundException e) {
            Log.d("rrrrrrrrrrrrrrr", "Error service");
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTimeReturn(String key_banner_ads, String key_popup_ads, String key_video_ads, int time_start_show_popup, int offset_time_show_popup, int show_ads) {
        Log.d("adsssssssssssss", show_ads + "");

        if (show_ads != 0) {
            Config.isRequestLoadAd = true;
            Utils.handleInterstitialAd(getApplicationContext());
            Utils.handleRewardedVideoAd(getApplicationContext());
        } else {
            Config.isRequestLoadAd = false;
            stopService(new Intent(Config.showAdService));
            return;
        }

        if (!key_banner_ads.equals(null))
            Config.banner_ads = key_banner_ads;

        if (!key_popup_ads.equals(null)) {
            Config.popup_ads = key_popup_ads;

            Config.iad = new InterstitialAd(getApplicationContext());
            // set the ad unit ID
            Config.iad.setAdUnitId(Config.popup_ads);
            AdRequest adRequest = new AdRequest.Builder().build();
            // Load ads into Interstitial Ads
            Config.iad.loadAd(adRequest);
        }

        if (!key_video_ads.equals(null)) {
            Config.video_ads = key_video_ads;

            // Load ads into Rewarded Video Ad
            Config.adVideo.loadAd(Config.video_ads, new AdRequest.Builder().build());
        }

        Config.time_start_show_popup = time_start_show_popup;
        Config.offset_time_show_popup = offset_time_show_popup;

        Log.d("ssssssssss", "start=" + Config.time_start_show_popup + " offset=" + Config.offset_time_show_popup);
        Log.d("ssssssssss", "banner=" + Config.banner_ads + " popup=" + Config.popup_ads);

        stopService(new Intent(Config.showAdService));
    }
}
