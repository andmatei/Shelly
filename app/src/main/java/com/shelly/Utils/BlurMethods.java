package com.shelly.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.widget.ImageView;

import com.shelly.Activities.MainActivity;
import com.shelly.R;

public class BlurMethods {

    public BlurMethods() {

    }

    public static Bitmap createBlurBitmap(View viewContainer, Context mContext) {
        Bitmap bitmap = captureView(viewContainer);
        if (bitmap != null) {
            blurBitmapWithRenderscript(
                    RenderScript.create(mContext),
                    bitmap);
        }
        return bitmap;
    }

    private static Bitmap captureView(View view) {
        view.measure(
                View.MeasureSpec.makeMeasureSpec(Constants.ACTIVITY_CARDVIEW_SIZE, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(Constants.ACTIVITY_CARDVIEW_SIZE, View.MeasureSpec.EXACTLY));
        view.layout(0, 0, Constants.ACTIVITY_CARDVIEW_SIZE, Constants.ACTIVITY_CARDVIEW_SIZE);
        Bitmap image = Bitmap.createBitmap(view.getMeasuredHeight(),
                view.getMeasuredHeight(),
                Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(image);
        view.draw(canvas);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        ColorFilter filter = new LightingColorFilter(0xFFCECECE, 0x000B0B0B);
        paint.setColorFilter(filter);
        canvas.drawBitmap(image, 0, 0, paint);
        return image;
    }

    private static void blurBitmapWithRenderscript(RenderScript rs, Bitmap bitmap2) {
        final Allocation input =
                Allocation.createFromBitmap(rs, bitmap2);
        final Allocation output = Allocation.createTyped(rs,
                input.getType());
        final ScriptIntrinsicBlur script =
                ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(25f);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap2);
    }


    public static void setBackgroundOnView(ImageView view, Bitmap bitmap, Context mContext, int mCorenrRadius) {
        Drawable d;
        if (bitmap != null) {
            d = RoundedBitmapDrawableFactory.create(mContext.getResources(), bitmap);
            ((RoundedBitmapDrawable) d).setCornerRadius(mCorenrRadius);
        } else {
            d = ContextCompat.getDrawable(mContext, R.drawable.bg_cardview_account_type);
        }
        view.setBackground(d);
    }
}
