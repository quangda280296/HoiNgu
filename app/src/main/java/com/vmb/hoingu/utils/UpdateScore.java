package com.vmb.hoingu.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.vmb.hoingu.config.Config;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by keban on 3/17/2018.
 */

public class UpdateScore extends AsyncTask {

    Context context;
    String fb_id;
    String score;
    String name;

    String json;

    public UpdateScore(Context context, String fb_id, String score, String name) {
        this.context = context;
        this.fb_id = fb_id;
        this.score = score;
        this.name = name;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        // Tạo danh sách tham số gửi đến máy chủ
        List<NameValuePair> args = new ArrayList<NameValuePair>();
        args.add(new BasicNameValuePair("fb_id", fb_id));
        args.add(new BasicNameValuePair("score", score));

        String utf = convertStringToUTF8(name);
        args.add(new BasicNameValuePair("name", utf));

        HttpHandler httpHandler = new HttpHandler();
        json = httpHandler.callService(Config.post_score_API_URL, HttpHandler.POST, args);

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (json == null)
            Utils.shortToast(context, "Gửi điểm thất bại, vui lòng thử lại sau");
        else
            Utils.shortToast(context, "Điểm của bạn đã được gửi lên thành công");
    }

    // convert internal Java String format to UTF-8
    public static String convertStringToUTF8(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("UTF-8"), "ISO-8859-1");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }
}
