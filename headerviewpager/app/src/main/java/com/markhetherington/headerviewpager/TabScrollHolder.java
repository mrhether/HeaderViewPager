package com.markhetherington.headerviewpager;

import android.widget.AbsListView;

public interface TabScrollHolder {
    void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition);
}
