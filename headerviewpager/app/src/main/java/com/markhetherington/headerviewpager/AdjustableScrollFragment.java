package com.markhetherington.headerviewpager;

import android.support.v4.app.Fragment;

public abstract class AdjustableScrollFragment extends Fragment implements ScrollAdjustable {

	protected TabScrollHolder mTabScrollHolder;
	public void setScrollTabHolder(TabScrollHolder tabScrollHolder) {
		mTabScrollHolder = tabScrollHolder;
	}

}