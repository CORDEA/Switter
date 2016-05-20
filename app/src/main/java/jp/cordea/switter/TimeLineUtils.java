package jp.cordea.switter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

/**
 * Created by Yoshihiro Tanaka on 16/05/20.
 */
public class TimeLineUtils {

    private static String getFormattedStringOfName(Context context, String userName, String screenName) {
        return userName + " "
                + String.format(context.getResources().getString(R.string.mention_format_text), screenName);
    }

    /**
     * UI に表示するために調整された SpannableString を返す
     * @param context Context
     * @param userName tweet.user.name
     * @param screenName tweet.user.screenName
     * @return SpannableString
     */
    public static SpannableString getSpannableStringOfName(Context context, String userName, String screenName) {
        float size = context.getResources().getDimension(R.dimen.user_name_text_size);
        float secSize = context.getResources().getDimension(R.dimen.user_id_text_size);

        int color = ContextCompat.getColor(context, R.color.colorPrimaryText);
        int secColor = ContextCompat.getColor(context, R.color.colorSecondaryText);

        String formattedString = getFormattedStringOfName(context, userName, screenName);
        SpannableString spannableString = new SpannableString(formattedString);
        int length = userName.length();

        spannableString.setSpan(new AbsoluteSizeSpan((int) size), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan((int) secSize), length + 1, formattedString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(color), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(secColor), length + 1, formattedString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

}
