package com.jew.coree.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContentResolverCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.corelibs.base.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;
import com.jew.coree.R;
import com.jew.coree.presenter.CollapsingToolbarPresenter;
import com.jew.coree.view.interfaces.CollapsingToolbarView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollapsingToolbarActivity extends BaseActivity<CollapsingToolbarView, CollapsingToolbarPresenter> implements CollapsingToolbarView {

    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collapsing_toolbar;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ImmersionBar.with(this)
                .statusBarColor(R.color.blue_dark)     //状态栏颜色，不写默认透明色
                .init();

        toolbar.setNavigationIcon(R.drawable.ic_arrow);
        toolbar.setNavigationOnClickListener(v->finish());
        collapsingToolbarLayout.setTitle("测试标题");
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(getViewContext(), R.color.blue_dark));
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(getViewContext(), R.color.white));
        iv.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    protected CollapsingToolbarPresenter createPresenter() {
        return new CollapsingToolbarPresenter();
    }


}