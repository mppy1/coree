package com.jew.coree.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.corelibs.base.BaseFragment;
import com.jew.coree.App;
import com.jew.coree.R;
import com.jew.coree.presenter.WebViewPresenter;
import com.jew.coree.utils.Utils;
import com.jew.coree.view.interfaces.WebViewView;

import butterknife.BindView;

public class WebViewFragment extends BaseFragment<WebViewView, WebViewPresenter> implements WebViewView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        toolbar.setTitle("网页显示");
        Utils.setConfigCallback((WindowManager) App.getApp()
                .getSystemService(Context.WINDOW_SERVICE));
        Utils.setWebViewConfig(webview);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 判断是否点击电话，短信
                if (url != null && "tel".equals(url.substring(0, 3))) {
                    // 点击手机号码，提示打电话
                    final String phoneNumber = url.substring(4);
                } else if (url != null && "sms".equals(url.substring(0, 3))) {
                    // 跳转到发送短信页面
                    String phoneNumber = url.substring(4);
                    Uri smsToUri = Uri.parse("smsto:" + phoneNumber);
                    Intent mIntent = new Intent(
                            Intent.ACTION_SENDTO, smsToUri);
                    startActivity(mIntent);
                } else {
                    webview.loadUrl(url);
                }
                return true;
            }
        });
        webview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 100) {
                }
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog,
                                          boolean isUserGesture, Message resultMsg) {
                WebView newWebView = new WebView(getViewContext());
                view.addView(newWebView);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();
                newWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view,
                                                            String url) {
                        webview.loadUrl(url);
                        return true;
                    }
                });
                return true;
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
            }

            @Override
            public void onHideCustomView() {
            }
        });
        webview.loadUrl("https://www.baidu.com/");
    }

    @Override
    protected WebViewPresenter createPresenter() {
        return new WebViewPresenter();
    }


}