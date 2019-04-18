package com.corelibs.views;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有可以左右滑动界面的Adapter，封装下方点跟随界面滑动改变以及监听页面可见状态改变的功能
 */
public class PageViewAdapter extends PagerAdapter {

    private ArrayList<ImageView> viewArrayList;
    private List<String> tabTitles;

    public PageViewAdapter(ArrayList<ImageView> viewList) {
        setList(viewList);
    }

    public PageViewAdapter(ArrayList<ImageView> viewList, List<String> titles) {
        setList(viewList);
        setTitles(titles);
    }

    public void setList(ArrayList<ImageView> viewList) {
        this.viewArrayList = viewList;
    }

    public void setTitles(List<String> titles) {
        this.tabTitles = titles;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        return viewArrayList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        final View view = viewArrayList.get(position);
        if (view instanceof OnViewPageListener) {
            ((OnViewPageListener) view).onStop();
        }
        container.removeView(view);

    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (tabTitles != null) {
            return tabTitles.get(position);
        }
        return super.getPageTitle(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View view = viewArrayList.get(position);
        container.addView(view);
        if (view instanceof OnViewPageListener) {
            ((OnViewPageListener) view).onStart();
        }
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public interface OnViewPageListener {
        void onStart();

        void onStop();

        void onVisibleChange(boolean isVisible);
    }
}