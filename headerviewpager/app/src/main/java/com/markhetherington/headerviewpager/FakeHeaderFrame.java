package com.markhetherington.headerviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by markhetherington on 2014-09-20.
 */
public class FakeHeaderFrame extends FrameLayout {

    public FakeHeaderFrame(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomHeaderFrame.giveTouchToHeader(motionEvent);
                return true;
            }
        });

    }

}
