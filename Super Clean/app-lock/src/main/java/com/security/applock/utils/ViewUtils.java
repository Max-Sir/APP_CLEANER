package com.security.applock.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.security.applock.utils.Toolbox.hideSoftKeyboard;

public class ViewUtils {

    public static void setupUI(final View view, final Activity context) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                hideSoftKeyboard(context);
                return false;
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView, context);
            }
        }
    }

    public static void setupUI(final View view, final Activity context, View... viewException) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                hideSoftKeyboard(context);
                return false;
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                for (View aViewException : viewException) {
                    if (aViewException.getId() != innerView.getId()) {
                        setupUI(innerView, context);
                    }
                }
            }
        }
    }

    public static WindowManager.LayoutParams getWindowLayoutParams() {
        int layoutFlag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutFlag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutFlag = WindowManager.LayoutParams.TYPE_PHONE;
        }
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                layoutFlag,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        layoutParams.windowAnimations = android.R.style.Animation_Toast;
        return layoutParams;
    }

    public static WindowManager.LayoutParams setupLayoutParams() {
        WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                Build.VERSION.SDK_INT < Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_PHONE :
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mLayoutParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
        return mLayoutParams;
    }

    public static Bitmap drawableToBitmap(Drawable drawable, int width, int height) {
        int w = 20;
        int h = 20;
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        int[] pixex = new int[w * h];
        List<Integer> trIndexs = new ArrayList<Integer>();
        for (int i = 0; i < bitmap.getHeight(); i++) {
            for (int j = 0; j < bitmap.getWidth(); j++) {
                int color = bitmap.getPixel(j, i);
                int alpha = Color.alpha(color);
                if (alpha < 200) {
                    trIndexs.add(i * h + j);
                } else if (trIndexs.size() > 0) {
                    for (Integer tr : trIndexs) {
                        pixex[tr] = color;
                    }
                    trIndexs.clear();
                    pixex[i * h + j] = color;
                } else {
                    pixex[i * h + j] = color;
                }
            }
        }

        Bitmap bitmap2 = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bitmap2);
        RectF rectF = new RectF(0, 0,width,height);
        canvas2.drawBitmap(Bitmap.createBitmap(pixex, w, h, Bitmap.Config.ARGB_8888), null, rectF, null);
        return bitmap2;
    }

    public static void blur(Context mContent, @NonNull Bitmap bkg, View view, int width, int height) {
        //long startMs = System.currentTimeMillis();
        float radius = 50;
        float scaleFactor = 8;
        Bitmap overlay = Bitmap.createBitmap(
                (int) (width / scaleFactor),
                (int) (height/ scaleFactor),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);
        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        view.setBackgroundDrawable(new BitmapDrawable(mContent.getResources(), overlay));
    }

    public static Bitmap big(@NonNull Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(2.5f, 2.5f);
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, bitmap.getWidth() / 4,
                bitmap.getHeight() / 4, bitmap.getWidth() / 2 - 1,
                bitmap.getHeight() / 2 - 1, matrix, true);
        return handleImage(resizeBmp, 85);
    }

    public static Bitmap handleImage(Bitmap bm, int hue) {
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        ColorMatrix mHueMatrix = new ColorMatrix();
        ColorMatrix mAllMatrix = new ColorMatrix();
        float mHueValue = hue * 1.0F / 127;
        mHueMatrix.reset();
        mHueMatrix.setScale(mHueValue, mHueValue, mHueValue, 1);

        mAllMatrix.reset();
        mAllMatrix.postConcat(mHueMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));
        canvas.drawBitmap(bm, 0, 0, paint);
        return bmp;
    }

}
