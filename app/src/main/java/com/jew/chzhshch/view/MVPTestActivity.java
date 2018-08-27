package com.jew.chzhshch.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import com.corelibs.base.BaseActivity;
import com.corelibs.utils.ToastMgr;
import com.gyf.barlibrary.ImmersionBar;
import com.jew.chzhshch.BannerActivity;
import com.jew.chzhshch.InterstitialADActivity;
import com.jew.chzhshch.R;
import com.jew.chzhshch.presenter.MVPTestPresenter;
import com.jew.chzhshch.view.interfaces.MVPTestView;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

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

        FileDownloader.getImpl().create("http://180.153.105.144/dd.myapp.com/16891/564E30CF4B6963B49FA8066A58A10FDE.apk?mkey=5b68dd47b4994f78&f=17c7&fsname=com.xiaoyu.baijiatoutiao_1.2.6_19.apk&csr=1bbd&from_tracker=y&cip=180.153.105.141&proto=http")
                .setPath(Environment.getExternalStorageDirectory() + "/baijiatoutiao_1.2.6_19.apk")
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        ToastMgr.show("下载成功");
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {

                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {

                    }
                }).start();

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