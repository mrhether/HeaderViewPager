package com.markhetherington.headerviewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;

public class MainActivity extends FragmentActivity implements TabScrollHolder {

    private static AccelerateDecelerateInterpolator sSmoothInterpolator = new AccelerateDecelerateInterpolator();

    private ImageView mHeaderPicture;
    private View mHeader;
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;

    private PagerAdapter mPagerAdapter;

    private int mMinHeaderHeight;
    private int mHeaderHeight;
    private int mMinHeaderTranslation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMinHeaderHeight = getResources().getDimensionPixelSize(R.dimen.min_header_height);
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mMinHeaderTranslation = -mMinHeaderHeight + Utils.getActionBarHeight(this);

        setContentView(R.layout.activity_main);

        mHeaderPicture = (ImageView) findViewById(R.id.header_picture);
        mHeaderPicture.setImageResource(R.drawable.pic3);
        mHeader = findViewById(R.id.header);

        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.requestDisallowInterceptTouchEvent(true);

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPagerAdapter.setTabHolderScrollingContent(this);
        mViewPager.setAdapter(mPagerAdapter);

        mPagerSlidingTabStrip.setShouldExpand(true);
        mPagerSlidingTabStrip.setTextColorResource(android.R.color.white);
        mPagerSlidingTabStrip.setDividerColorResource(android.R.color.white);
        mPagerSlidingTabStrip.setViewPager(mViewPager);
        mPagerSlidingTabStrip.setOnPageChangeListener(new ActivityOnPageChangeListener());
        mPagerSlidingTabStrip.setTabPaddingLeftRight(getResources().getDimensionPixelOffset(R.dimen.tab_padding));
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition) {
        if (mViewPager.getCurrentItem() == pagePosition) {
            int scrollY = getScrollY(view);
            mHeader.setTranslationY(Math.max(-scrollY, mMinHeaderTranslation));
        }
    }

    // Reminder: that this function is custom to your list views maybe
    public int getScrollY(AbsListView view) {
        View c = view.getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = view.getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = mHeaderHeight;
        }

        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    /////
    ///// ON PAGE CHANGE LISTENER
    /////
    class ActivityOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        int mCurrent = 0;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int posToReset = position;
            if (mCurrent == position) posToReset = position + 1;
            adjustScrollOfView(posToReset);

        }

        @Override
        public void onPageSelected(int position) {
            mCurrent = position;
            adjustScrollOfView(position);
        }

        private void adjustScrollOfView(int position) {
            SparseArrayCompat<ScrollAdjustable> scrollTabHolders = mPagerAdapter.getScrollTabHolders();
            ScrollAdjustable currentHolder = scrollTabHolders.valueAt(position);
            if (currentHolder != null) {
                currentHolder.adjustScrollHeight((int) (mHeader.getHeight() + mHeader.getTranslationY()));
            }
        }
    }


    //////
    ////// PAGER ADAPTER
    //////
    public class PagerAdapter extends FragmentPagerAdapter {

        private SparseArrayCompat<ScrollAdjustable> mScrollTabHolders;
        private final String[] TITLES = {"The Left", "The Centre", "The Right"};
        private TabScrollHolder mListener;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            mScrollTabHolders = new SparseArrayCompat<ScrollAdjustable>();
        }

        public void setTabHolderScrollingContent(TabScrollHolder listener) {
            mListener = listener;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            AdjustableScrollFragment fragment = (AdjustableScrollFragment) SampleListFragmentScroll.newInstance(position);

            mScrollTabHolders.put(position, fragment);
            if (mListener != null) {
                fragment.setScrollTabHolder(mListener);
            }

            return fragment;
        }

        public SparseArrayCompat<ScrollAdjustable> getScrollTabHolders() {
            return mScrollTabHolders;
        }

    }
}
