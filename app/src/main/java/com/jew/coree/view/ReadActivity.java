package com.jew.coree.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.corelibs.base.BaseActivity;
import com.corelibs.utils.LogUtils;
import com.jew.coree.R;
import com.jew.coree.presenter.ReadPresenter;
import com.jew.coree.treader.Config;
import com.jew.coree.treader.db.BookList;
import com.jew.coree.treader.util.PageFactory;
import com.jew.coree.treader.view.PageWidget;
import com.jew.coree.treader.xiaoyu.popup.TxtViewMenu;
import com.jew.coree.view.interfaces.ReadView;

import java.io.IOException;

import butterknife.BindView;

public class ReadActivity extends BaseActivity<ReadView, ReadPresenter> implements ReadView {
    private final static String EXTRA_BOOK = "bookList";

    @BindView(R.id.bookpage)
    PageWidget bookpage;
    @BindView(R.id.book_back_btn)
    ImageView bookBackBtn;
    @BindView(R.id.textview_title_textview)
    TextView textviewTitleTextview;
    @BindView(R.id.book_voice_btn)
    ImageView bookVoiceBtn;
    @BindView(R.id.book_download_btn)
    ImageView bookDownloadBtn;
    @BindView(R.id.book_more_btn)
    ImageView bookMoreBtn;
    @BindView(R.id.topMenuLL)
    RelativeLayout topMenuLL;

    private Config config;
    private PageFactory pageFactory;
    private BookList bookList;
    // popwindow是否显示
    private Boolean isShow = false;
    private TxtViewMenu mMenu;

    @Override
    protected int getLayoutId() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_read;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 19) {
            bookpage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        hideSystemUI();
        mMenu = new TxtViewMenu(this);
        //获取intent中的携带的信息
        Intent intent = getIntent();
        bookList = (BookList) intent.getSerializableExtra(EXTRA_BOOK);
        config = Config.getInstance();
        pageFactory = PageFactory.getInstance();
        bookpage.setPageMode(config.getPageMode());
        pageFactory.setPageWidget(bookpage);
        bookpage.setTouchListener(new PageWidget.TouchListener() {
            @Override
            public void center() {
                if (isShow) {
                    hideReadSetting();
                } else {
                    showReadSetting();
                }
            }

            @Override
            public Boolean prePage() {
                if (isShow) {
                    return false;
                }

                pageFactory.prePage();
                if (pageFactory.isfirstPage()) {
                    return false;
                }

                return true;
            }

            @Override
            public Boolean nextPage() {
                LogUtils.e("setTouchListener" + "nextPage");
                if (isShow) {
                    return false;
                }

                pageFactory.nextPage();
                if (pageFactory.islastPage()) {
                    return false;
                }
                return true;
            }

            @Override
            public void cancel() {
                pageFactory.cancelPage();
            }
        });

        try {
            pageFactory.openBook(bookList);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "打开电子书失败", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected ReadPresenter createPresenter() {
        return new ReadPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pageFactory.clear();
        bookpage = null;
    }

    /**
     * 隐藏菜单。沉浸式阅读
     */
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        //  | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    private void showReadSetting() {
        isShow = !isShow;
        topMenuLL.setVisibility(View.VISIBLE);
        View parent = ReadActivity.this.getWindow().getDecorView();
        mMenu.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        showSystemUI();
    }

    private void hideReadSetting() {
        isShow = !isShow;
        topMenuLL.setVisibility(View.GONE);
        mMenu.dismiss();
        hideSystemUI();
    }

    public static boolean openBook(final BookList bookList, Context context) {
        if (bookList == null) {
            throw new NullPointerException("BookList can not be null");
        }

        Intent intent = new Intent(context, ReadActivity.class);
        intent.putExtra(EXTRA_BOOK, bookList);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        return true;
    }

}