package com.vmb.hoingu;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.vmb.hoingu.config.Config;
import com.vmb.hoingu.database.HoiNgu;
import com.vmb.hoingu.service.ShowAdService;

import java.util.Calendar;

/**
 * Created by keban on 3/6/2018.
 */

public class MainApplication extends Application {

    private Typeface fontSuperBold;
    private Typeface fontBold;
    private Typeface fontMedium;

    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        //MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713");

        // Handle font
        fontSuperBold = Typeface.createFromAsset(getAssets(), "iciel Cadena.ttf");
        fontBold = Typeface.createFromAsset(getAssets(), "SanFranciscoDisplay-Bold.otf");
        fontMedium = Typeface.createFromAsset(getAssets(), "SanFranciscoDisplay-Regular.otf");

        // Handle adService
        Calendar calendar = Calendar.getInstance();
        Config.time_start = ((calendar.get(Calendar.YEAR) * 365 + calendar.get(Calendar.DAY_OF_YEAR)) * 24
                + calendar.get(Calendar.HOUR_OF_DAY)) * 3600 + calendar.get(Calendar.MINUTE) * 60 + calendar.get(Calendar.SECOND);
        Config.time_end = Config.time_start;

        Config.showAdService = new Intent(this, ShowAdService.class);
        startService(Config.showAdService);

        // load database
        Config.hoi_ngu = new HoiNgu(getApplicationContext(), Config.HOINGU_DATABASE);
        Config.cac_thanh = new HoiNgu(getApplicationContext(), Config.CACTHANH_DATABASE);

        SharedPreferences mySharedPreferences = getSharedPreferences(Config.MYPREFS, Config.mode);
        if (mySharedPreferences == null)
            return;

        if (mySharedPreferences.contains("highscore"))
            Config.highScore = mySharedPreferences.getInt("highscore", 0);

        if (mySharedPreferences.contains("isUpdated"))
            Config.isUpdated = mySharedPreferences.getBoolean("isUpdated", true);

        if (mySharedPreferences.contains("isFirstTime"))
            Config.isFirstTime = mySharedPreferences.getBoolean("isFirstTime", true);
    }

    public Typeface getFontSuperBold() {
        return fontSuperBold;
    }

    public Typeface getFontBold() {
        return fontBold;
    }

    public Typeface getFontMedium() {
        return fontMedium;
    }
}
