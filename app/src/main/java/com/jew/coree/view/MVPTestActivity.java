package com.jew.coree.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.corelibs.base.BaseActivity;
import com.corelibs.utils.ToastMgr;
import com.gyf.barlibrary.ImmersionBar;
import com.jew.coree.BannerActivity;
import com.jew.coree.InterstitialADActivity;
import com.jew.coree.R;
import com.jew.coree.adapter.MVPTestAdapter;
import com.jew.coree.presenter.MVPTestPresenter;
import com.jew.coree.view.interfaces.MVPTestView;

import java.lang.ref.WeakReference;

import butterknife.BindView;

public class MVPTestActivity extends BaseActivity<MVPTestView, MVPTestPresenter> implements MVPTestView {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private String[] data = {"阿里首页", "Banner广告", "插屏广告", "collapsingToolbar", "BottomBar", "CollapsingTab", "模拟数据请求"};
    private MVPTestAdapter adapter;
    private CommonHandler commonHandler;

    public static class CommonHandler extends Handler {
        private final WeakReference<MVPTestActivity> mtarget;

        public CommonHandler(MVPTestActivity activity) {
            mtarget = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MVPTestActivity target = mtarget.get();
            if (target != null) {
                switch (msg.what) {
                    case 0:
                        target.itemEvent(msg.arg1);
                        break;

                }
            }
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_mvptest;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
                .init();
        commonHandler = new CommonHandler(this);

        adapter = new MVPTestAdapter(getViewContext(), data);
        adapter.setHandler(commonHandler);
        LinearLayoutManager manager = new LinearLayoutManager(getViewContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected MVPTestPresenter createPresenter() {
        return new MVPTestPresenter();
    }

    public void itemEvent(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, AliHomeActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, BannerActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, InterstitialADActivity.class));
                break;
            case 3:
                startActivity(new Intent(this, CollapsingToolbarActivity.class));
                break;
            case 4:
                startActivity(new Intent(this, BottomBarActivity.class));
                break;
            case 5:
                startActivity(new Intent(this, CollapsingTabActivity.class));
                break;
            case 6:
                presenter.login("123456789", "3344");
                break;
            default:
                ToastMgr.show("没有事件");
                break;
        }

    }
}