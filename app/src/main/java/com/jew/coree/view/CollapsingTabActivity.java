package com.jew.coree.view;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.corelibs.base.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;
import com.jew.coree.R;
import com.jew.coree.adapter.AdapterViewPager;
import com.jew.coree.presenter.CollapsingTabPresenter;
import com.jew.coree.view.fragment.WebViewFragment;
import com.jew.coree.view.interfaces.CollapsingTabView;

import java.util.ArrayList;

import butterknife.BindView;

public class CollapsingTabActivity extends BaseActivity<CollapsingTabView, CollapsingTabPresenter> implements CollapsingTabView {

    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.wp_main_page)
    ViewPager viewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collapsing_tab;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ImmersionBar.with(this)
                .statusBarColor(R.color.blue_dark)     //状态栏颜色，不写默认透明色
                .init();

        toolbar.setNavigationIcon(R.drawable.ic_arrow);
        toolbar.setNavigationOnClickListener(v -> finish());
        collapsingToolbarLayout.setTitle("Collapsing + Tab");
        collapsingToolbarLayout.setStatusBarScrimColor(ContextCompat.getColor(getViewContext(), R.color.white));
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(getViewContext(), R.color.blue_dark));
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(getViewContext(), R.color.white));

        String[] titles = new String[2];
        titles[0] = "动态";
        titles[1] = "文章";
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new WebViewFragment());
        fragments.add(new WebViewFragment());
        viewPager.setAdapter(new AdapterViewPager(getSupportFragmentManager(), fragments, titles));
        viewPager.setOffscreenPageLimit(fragments.size());
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected CollapsingTabPresenter createPresenter() {
        return new CollapsingTabPresenter();
    }

}