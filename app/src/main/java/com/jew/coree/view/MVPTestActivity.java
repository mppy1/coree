package com.jew.coree.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.corelibs.base.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;
import com.jew.coree.BannerActivity;
import com.jew.coree.InterstitialADActivity;
import com.jew.coree.R;
import com.jew.coree.presenter.MVPTestPresenter;
import com.jew.coree.view.interfaces.MVPTestView;

import butterknife.BindView;

public class MVPTestActivity extends BaseActivity<MVPTestView, MVPTestPresenter> implements MVPTestView {

    @BindView(R.id.tv_banner)
    TextView tvBanner;
    @BindView(R.id.tv_interstitial)
    TextView tvInterstitial;
    @BindView(R.id.tv_ali_home)
    TextView tvAliHome;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_mvptest;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
                .init();


        tvBanner.setOnClickListener(v ->
                startActivity(new Intent(this, BannerActivity.class))
        );
        tvInterstitial.setOnClickListener(v ->
                startActivity(new Intent(this, InterstitialADActivity.class))
        );
        tvAliHome.setOnClickListener(v ->
                startActivity(new Intent(this, AliHomeActivity.class))
        );
    }

    @Override
    protected MVPTestPresenter createPresenter() {
        return new MVPTestPresenter();
    }
}