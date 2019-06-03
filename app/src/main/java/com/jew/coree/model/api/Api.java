package com.jew.coree.model.api;


import com.jew.coree.model.Response;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {

    @POST("device/add")
    Observable<Response> deviceAdd(@Body RequestBody body);

    @POST("users/cookie")
    Observable<Response> cookie(@Body RequestBody body);


}
