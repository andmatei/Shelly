package com.shelly.Utils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.nfc.Tag;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class TextTransformationUtils {

    public final static int CODE_PUT_STRIKETHROUGH = 1;
    public final static int CODE_ERASE_STRIKETHROUGH = 0;
    private final static int STRIKETHROUGH_DURATION = 300;

    private Context mContext;

    public TextTransformationUtils(Context mContext) {
        this.mContext = mContext;
    }

    public static void animateStrikeThrough(final TextView tv, int Code) {
        final Spannable span = new SpannableString(tv.getText());
        final StrikethroughSpan strikethroughSpan = new StrikethroughSpan();

        if(Code == CODE_PUT_STRIKETHROUGH) {
            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, tv.getText().length());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    span.setSpan(strikethroughSpan, 0, (int) valueAnimator.getAnimatedValue(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(span);
                }
            });
            valueAnimator.setDuration(STRIKETHROUGH_DURATION);
            valueAnimator.start();
        } else {
            ValueAnimator valueAnimator = ValueAnimator.ofInt(tv.getText().length(), 0);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    StrikethroughSpan[] strikethroughSpans = span.getSpans(0, (int) valueAnimator.getAnimatedValue(), StrikethroughSpan.class);
                    if(strikethroughSpans.length > 0) {
                        span.removeSpan(strikethroughSpans[0]);
                        span.setSpan(strikethroughSpan, 0, (int) valueAnimator.getAnimatedValue(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    tv.setText(span);
                }
            });
            valueAnimator.setDuration(STRIKETHROUGH_DURATION);
            valueAnimator.start();
        }
    }

    public static int findNewStartingPosition(StringBuilder stringBuilder, int position, List<String> Tags) {
        int i = stringBuilder.indexOf("<"), offset = 0;
        Log.e("<", "" + i + " " + (position + offset));
        while(i <= position + offset && i != -1) {
            for(String tag : Tags) {
                if(stringBuilder.indexOf(tag, i) == i) {
                    offset += tag.length();
                }
            }
            i = stringBuilder.indexOf("<", i + 1);
            Log.e("<", "" + i + " " + (position + offset));
        }
        return position + offset;
    }
}
