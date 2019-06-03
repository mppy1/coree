package com.jew.coree.model.api;

import com.jew.coree.model.Response;

import io.reactivex.Observable;
import okhttp3.RequestBody;


/**
 * Created by pengzhu on 2019/6/3/003.
 */
public class LoginApiImpl implements LoginApi {
    @Override
    public Observable<Response> cookie(RequestBody body) {
        return Observable.fromCallable(()->{
            Response response = new Response();
            response.setCode(200);
            response.setMessage("登录接口成功！");
            return response;
        });

    }
}
