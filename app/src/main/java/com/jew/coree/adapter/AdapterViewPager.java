package com.jew.coree.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 滑动的控件Adapter
 * Created by pengzhu on 2017/11/2.
 */

public class AdapterViewPager extends FragmentStatePagerAdapter {
    private List<Fragment> mList;
    private CharSequence[] mTitles;

    public AdapterViewPager(FragmentManager fragmentManager, List<Fragment> list) {
        super(fragmentManager);
        this.mList = list;
        notifyDataSetChanged();
    }


    public AdapterViewPager(FragmentManager fragmentManager, List<Fragment> list, CharSequence[] titles) {
        super(fragmentManager);
        this.mList = list;
        this.mTitles = titles;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles != null) {
            return mTitles[position];
        }
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment f = (Fragment) super.instantiateItem(container, position);
        View view = f.getView();
        if (view != null)
            container.addView(view);
        return f;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position >= mList.size()) {
            return;
        }
        View view = mList.get(position).getView();
        if (view != null)
            container.removeView(view);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
