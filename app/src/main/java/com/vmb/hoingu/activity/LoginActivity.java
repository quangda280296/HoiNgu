package com.vmb.hoingu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.vmb.hoingu.R;
import com.vmb.hoingu.config.Config;
import com.vmb.hoingu.utils.UpdateScore;
import com.vmb.hoingu.utils.Utils;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private boolean isStartNew = false;

    private CallbackManager callbackManager = CallbackManager.Factory.create();
    private AccessToken accessToken;
    private Profile profile;

    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // set fullscreen
        LinearLayout root = findViewById(R.id.root);
        root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        index = getIntent().getExtras().getInt("index", -1);

        // Check login
        accessToken = AccessToken.getCurrentAccessToken();
        profile = Profile.getCurrentProfile();

        if (accessToken != null && profile != null) {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
            sendHighScore();
        } else
            handle();
    }

    // handle Login Facebook
    public void handle() {
        LoginButton loginButton = (LoginButton) findViewById(R.id.btn_login);

        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        profile = Profile.getCurrentProfile();
                        if (profile == null) {
                            Utils.shortToast(getApplicationContext(), "Đã xảy ra lỗi không mong muốn, vui lòng thử lại sau");
                            return;
                        }

                        sendHighScore();
                    }
                }, 500);
            }

            @Override
            public void onCancel() {
                //App code
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void sendHighScore() {
        if (Config.isUpdated) {
            Utils.shortToast(getApplicationContext(), "Bạn chưa có kỉ lục điểm cao mới để gửi lên");
            finish();
            return;
        }

        Log.d("arrrrrrrrarara", profile.getProfilePictureUri(500, 500).toString());
        Log.d("arrrrrrrrarara", profile.getLinkUri().toString());
        Log.d("arrrrrrrrarara", profile.getId());
        Log.d("arrrrrrrrarara", profile.getFirstName());
        Log.d("arrrrrrrrarara", profile.getMiddleName());
        Log.d("arrrrrrrrarara", profile.getLastName());
        Log.d("arrrrrrrrarara", profile.getName());

        new UpdateScore(getApplicationContext(), profile.getId(), Config.highScore + "", profile.getName()).execute();

        SharedPreferences mySharedPreferences = getSharedPreferences(Config.MYPREFS, Config.mode);
        SharedPreferences.Editor myEditor = mySharedPreferences.edit();
        myEditor.putBoolean("isUpdated", true);
        myEditor.commit();
        Config.isUpdated = true;

        Intent intent = new Intent(LoginActivity.this, LeaderboardActivity.class);
        if (index != -1) {
            Bundle bundle = new Bundle();
            bundle.putInt("index", index);
            intent.putExtras(bundle);
        }

        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // set fullscreen
        LinearLayout root = findViewById(R.id.root);
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
}
