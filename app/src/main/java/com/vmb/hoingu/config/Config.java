package com.vmb.hoingu.config;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.vmb.hoingu.activity.GameOverActivity;
import com.vmb.hoingu.activity.MainActivity;
import com.vmb.hoingu.database.HoiNgu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by keban on 3/6/2018.
 */

public class Config {
    public static boolean isRequestLoadAd = false;

    public static int countVideo = 4;
    public static int countTurn = 3;

    public static int highScore = 0;
    public static int score = 0;

    public static int countStar = 0;
    public static int requireStar = 2;

    public static boolean isFirstTime = true;
    public static boolean isUpdated = true;
    public static boolean isflying = false;

    public static boolean isShowAddRoll = false;
    public static boolean canRoll = false;

    public static Intent showAdService;
    public static InterstitialAd iad;
    public static RewardedVideoAd adVideo;

    public static String post_score_API_URL = "http://gamemobileglobal.com/hoingu/score.php";

    public static String popup_API_URL = "http://gamemobileglobal.com/api/control-app.php?";
    public static String codeAccessAPI = "53545";

    public static int time_start = 0;
    public static int time_end = 0;
    public static int time_start_show_popup = 0;
    public static int offset_time_show_popup = 0;

    public static String banner_ads = "ca-app-pub-4776693922147142/6293398421";
    public static String popup_ads = "ca-app-pub-4776693922147142/2463207899";
    public static String video_ads = "ca-app-pub-4776693922147142/2413863164";

    public static GameOverActivity activity;
    public static MainActivity mainActivity;
    public static int index = 1;

    public static HoiNgu hoi_ngu;
    public static HoiNgu cac_thanh;

    public static List<Integer> da_hoi = new ArrayList<>();
    public static String category = "hoi_ngu";
    public static MediaPlayer mediaPlayer;

    public static boolean isPlayBackgroundMusic = true;

    public static final int mode = Activity.MODE_PRIVATE;
    public static final String MYPREFS = "MyPreferences";
    public static final int SCHEMA_VERSION = 1;

    public static final String HOINGU_DATABASE = "hoi_ngu.sqlite";
    public static final String CACTHANH_DATABASE = "cac_thanh.sqlite";

    public static final String HOINGU_TABLE = "question";
    public static final String HOINGU_ID = "_id";
    public static final String HOINGU_CAUHOI = "cauhoi";
    public static final String HOINGU_A = "a";
    public static final String HOINGU_B = "b";
    public static final String HOINGU_C = "c";
    public static final String HOINGU_D = "d";
    public static final String HOINGU_DAPAN = "dapan";
    public static final String HOINGU_NICK = "nick";
    public static final String HOINGU_GIAITHICH = "giai_thich";

    public static final String[] trick = {"Mẹo 1: Xem Bảng Xếp Hạng toàn bộ người chơi bằng cách nhấn vào mục \"tất cả\"",
            "Mẹo 2: Bạn có thể thêm câu hỏi cho bộ \"Các thánh\".",
            "Mẹo 3: \"Hỏi Ngu\" là những câu hỏi thử thách kiến thức của bạn",
            "Mẹo 4: Bạn có thể oánh thằng ra đề",
            "Mẹo 5: Nhấn vào quảng cáo có thể khiến Assmin đẹp trai hơn :v",
            "Mẹo 6: Like fanpage Facebook để cập nhật thông tin mới nhất về game",
            "Mẹo 7: Nên thêm những câu đố mới lạ, vui vẻ do bạn nghĩ ra\nKhông nên thêm những câu đã có trong game",
            "Mẹo 8: Chỉ cần thay đổi vài từ ngữ là đã có một câu đố mới, với một đáp án mới",
            "Mẹo 9: Nếu thấy game hay thì hãy Rate game 5 sao và giới thiệu game với bạn bè nhé!",
            "Mẹo 10: Nếu bạn có thắc mắc, góp ý hay ý tưởng gì về game thì có thể inbox với assmin qua fanpage Facebook"};
}
