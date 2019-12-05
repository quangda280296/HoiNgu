package com.vmb.hoingu.utils;

import android.content.Context;
import android.media.MediaPlayer;

import com.vmb.hoingu.R;

/**
 * Created by keban on 3/6/2018.
 */

public class PlayMusic {

    public static MediaPlayer mp;

    public static void playCorrect(Context context) {
        mp = MediaPlayer.create(context, R.raw.correct);
        mp.start();
    }

    public static void playPop(Context context) {
        mp = MediaPlayer.create(context, R.raw.pop);
        mp.start();
    }

    public static void playPunch(Context context) {
        mp = MediaPlayer.create(context, R.raw.punch);
        mp.start();
    }

    public static void playUhOh(Context context) {
        mp = MediaPlayer.create(context, R.raw.uh_oh);
        mp.start();
    }

    public static void playSad(Context context) {
        mp = MediaPlayer.create(context, R.raw.sad_trombone);
        mp.start();
    }
}
