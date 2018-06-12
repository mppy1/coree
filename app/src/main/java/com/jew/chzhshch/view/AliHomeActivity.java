package com.jew.chzhshch.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.widget.RelativeLayout;

import com.corelibs.base.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;
import com.jew.chzhshch.R;
import com.jew.chzhshch.presenter.AliHomePresenter;
import com.jew.chzhshch.view.interfaces.AliHomeView;

import butterknife.BindView;


public class AliHomeActivity extends BaseActivity<AliHomeView, AliHomePresenter> implements AliHomeView, AppBarLayout.OnOffsetChangedListener {

    @BindView(R.id.v_title_big_mask)
    View vTitleBigMask;
    @BindView(R.id.v_toolbar_search_mask)
    View vToolbarSearchMask;
    @BindView(R.id.v_toolbar_small_mask)
    View vToolbarSmallMask;
    @BindView(R.id.abl_bar)
    AppBarLayout ablBar;
    @BindView(R.id.include_toolbar_search)
    View includeToolbarSearch;
    @BindView(R.id.include_toolbar_small)
    View includeToolbarSmall;

    private int mMaskColor;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alihome;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ImmersionBar.with(this)
                .statusBarColor(R.color.blue_dark)     //状态栏颜色，不写默认透明色
                .init();
        ablBar.addOnOffsetChangedListener(this);
        //背景颜色
        mMaskColor = getResources().getColor(R.color.blue_dark);
    }

    @Override
    protected AliHomePresenter createPresenter() {
        return new AliHomePresenter();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        //720*1080手机 verticalOffset取值范围[0-200]px
        int absVerticalOffset = Math.abs(verticalOffset);//AppBarLayout竖直方向偏移距离px
        int totalScrollRange = appBarLayout.getTotalScrollRange();//AppBarLayout总的距离px
        //背景颜色转化成RGB的渐变色
        int argb = Color.argb(absVerticalOffset, Color.red(mMaskColor), Color.green(mMaskColor), Color.blue(mMaskColor));
        int argbDouble = Color.argb(absVerticalOffset * 2, Color.red(mMaskColor), Color.green(mMaskColor), Color.blue(mMaskColor));
        //appBarLayout上滑一半距离后小图标应该由渐变到全透明
        int title_small_offset = (200 - absVerticalOffset) < 0 ? 0 : 200 - absVerticalOffset;
        int title_small_argb = Color.argb(title_small_offset * 2, Color.red(mMaskColor),
                Color.green(mMaskColor), Color.blue(mMaskColor));
        //appBarLayout上滑不到一半距离
        if (absVerticalOffset <= totalScrollRange / 2) {
            includeToolbarSearch.setVisibility(View.VISIBLE);
            includeToolbarSmall.setVisibility(View.GONE);
            //,乘以2倍为了和下面的大图标渐变区分渐变
            vToolbarSearchMask.setBackgroundColor(argbDouble);
        } else {
            includeToolbarSearch.setVisibility(View.GONE);
            includeToolbarSmall.setVisibility(View.VISIBLE);
            //appBarLayout上滑一半距离后小图标应该由渐变到全透明
            vToolbarSmallMask.setBackgroundColor(title_small_argb);

        }
        //上滑时遮罩由全透明到半透明
        vTitleBigMask.setBackgroundColor(argb);
    }
}