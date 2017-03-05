package com.example.jack.awesomebing.retroService;

import com.example.jack.awesomebing.beanForGson.PageInfo;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Jack on 2017/3/4.
 */
public interface BingService {

    @POST("v1")
    Call<PageInfo> getPage(@Query("p") int page,@Query("size") int size);
}
