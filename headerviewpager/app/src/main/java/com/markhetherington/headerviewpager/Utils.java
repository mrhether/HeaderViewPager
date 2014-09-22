package com.markhetherington.headerviewpager;

import android.content.Context;
import android.graphics.RectF;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by markhetherington on 2014-09-21.
 */
public class Utils {

    private static int mActionBarHeight;
    public static int getActionBarHeight(Context context) {

        TypedValue mTypedValue = new TypedValue();
        if (mActionBarHeight != 0) {
            return mActionBarHeight;
        }
        context.getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue, true);
        mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data, context.getResources().getDisplayMetrics());
        return mActionBarHeight;
    }

    public static float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, min), max);
    }

    // TODO: trace path meeting point of views and use all translations from there
    public static void interpolate(View view1, View view2, float interpolation, final View view1Parent) {
        RectF rect1 = new RectF();
        RectF rect2 = new RectF();
        getOnScreenRect(rect1, view1);
        getOnScreenRect(rect2, view2);

        float scaleX = 1.0F + interpolation * (rect2.width() / rect1.width() - 1.0F);
        float scaleY = 1.0F + interpolation * (rect2.height() / rect1.height() - 1.0F);
        float translationX = 0.5F * (interpolation * (rect2.left + rect2.right - rect1.left - rect1.right));
        float translationY = 0.5F * (interpolation * (rect2.top + rect2.bottom - rect1.top - rect1.bottom));

        view1.setTranslationX(translationX);
        view1.setTranslationY(translationY - view1Parent.getTranslationY());
        view1.setScaleX(scaleX);
        view1.setScaleY(scaleY);
    }

    private static RectF getOnScreenRect(RectF rect, View view) {
        rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        return rect;
    }

}
