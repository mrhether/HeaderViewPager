package com.markhetherington.headerviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by markhetherington on 2014-09-19.
 */
public class CustomHeaderFrame extends FrameLayout {

    // TODO: change this as its bad.
    public static CustomHeaderFrame mostRecentCustomFrame;

    public boolean safeZoneTouch = false;

    private static final String TAG = CustomHeaderFrame.class.getSimpleName();
    private GestureDetector mGestureDetector;

    private float xDistance;
    private float yDistance;
    private float lastX;
    private float lastY;
    private int mActionBarHeight;

    public CustomHeaderFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
        mostRecentCustomFrame= this;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        // If we are horizontally scrolling then its our touch event
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                lastX = ev.getX();
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                xDistance += Math.abs(curX - lastX);
                yDistance += Math.abs(curY - lastY);
                lastX = curX;
                lastY = curY;
                if(xDistance > yDistance) return false;
        }

        // If someone has set this touch to be safe it is our touch event.
        if (safeZoneTouch || mGestureDetector.onTouchEvent(ev)) {
            safeZoneTouch = false;
            return false;
        }

        // This is so the tabs get clicks when they are above the scroll view.
        int measuredHeight = (int) -getTranslationY();
        int dimensionPixelOffset =
                getContext().getResources().getDimensionPixelOffset(R.dimen.min_header_height)
                - Utils.getActionBarHeight(getContext());
        if (measuredHeight >= dimensionPixelOffset){
            return super.onInterceptTouchEvent(ev);
        }

        // Let the viewpager handle this touch.
        return true;
    }

    // Return false if we're scrolling in the y direction
    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            boolean horizontalScroll = Math.abs(distanceY) < Math.abs(distanceX);
            return horizontalScroll;
        }

    }

    public static void giveTouchToHeader(MotionEvent motionEvent) {
        CustomHeaderFrame.mostRecentCustomFrame.safeZoneTouch = true;
        CustomHeaderFrame.mostRecentCustomFrame.dispatchTouchEvent(motionEvent);
    }


}
