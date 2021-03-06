package ui;

import android.graphics.Color;

/**
 * Created by Matthias on 10.02.2018.
 */

public class AppColors {

    public static final int COLOR_PRIMARY = 0x004d40;
    public static final int COLOR_PRIMARY_TRANSPARENT = Color.argb(30, Color.red(COLOR_PRIMARY), Color.green(COLOR_PRIMARY), Color.blue(COLOR_PRIMARY));
    public static final int COLOR_NEUTRAL = Color.GRAY;
    public static final int COLOR_POSITIVE = Color.argb(255, 0, 153, 0);
    public static final int COLOR_NEGATIVE =  Color.argb(255, 204, 0, 0);

    public static final int[] PLAYER_COLORS = {
            Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31)};

    public static final int[] STATISTICS_COLORS = {
            Color.rgb(0,77,64), Color.rgb(69,125,116), Color.rgb(0,57,47),
            Color.rgb(139,174,168), Color.rgb(0,35,30)};

    public static int getScoreTextColor(double score, boolean grayout, boolean solo, boolean totalScore) {
        int alpha = 255;

        //if(grayout) alpha = 200;

        if(solo && !totalScore && score > 0) return Color.argb(alpha,218,165,32);

        if(score > 0) return Color.argb(alpha,0,128,0);
        if(score < 0) return Color.argb(alpha,255,0,0);

        return Color.argb(alpha,0,0,0);
    }
}
