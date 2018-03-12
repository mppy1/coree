package com.jew.chzhshch.view;

import android.os.Bundle;

import com.corelibs.base.BaseActivity;
import com.jew.chzhshch.R;
import com.jew.chzhshch.presenter.MVPTestPresenter;
import com.jew.chzhshch.view.interfaces.MVPTestView;

public class MVPTestActivity extends BaseActivity<MVPTestView, MVPTestPresenter> implements MVPTestView {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mvptest;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected MVPTestPresenter createPresenter() {
        return new MVPTestPresenter();
    }
}