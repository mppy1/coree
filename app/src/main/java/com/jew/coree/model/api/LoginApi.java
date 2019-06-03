package com.jew.coree.model.api;

import com.jew.coree.model.Response;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by pengzhu on 2019/6/3/003.
 */
public interface LoginApi {
    @POST("users/cookie")
    Observable<Response> cookie(@Body RequestBody body);
}
