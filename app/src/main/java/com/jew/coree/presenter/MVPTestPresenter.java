package com.jew.coree.presenter;

import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.corelibs.utils.ToastMgr;
import com.jew.coree.model.Response;
import com.jew.coree.model.api.LoginApi;
import com.jew.coree.model.api.LoginApiImpl;
import com.jew.coree.view.interfaces.MVPTestView;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MVPTestPresenter extends BasePresenter<MVPTestView> {
    private LoginApi api = new LoginApiImpl();

    @Override
    public void onStart() {

    }

    public void login(String phone,String yzm){
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "");
        api.cookie(body).compose(new ResponseTransformer<>())
        .compose(this.bindToLifeCycle())
        .subscribe(new ResponseSubscriber<Response>() {
            @Override
            public void success(Response response) {
                if (response != null){
                    ToastMgr.show("请求登录成功！");
                }
            }
        });
    }
}