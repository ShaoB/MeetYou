package com.example.android.meetyou.networkUtils;

import com.example.android.meetyou.Bean.TokenModel;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shishaobin on 2019/8/26
 */
public interface ApiService {
    //获取融云Token
    @POST("user/getToken.json")
    @FormUrlEncoded
    Observable<TokenModel> getCloudToken(@FieldMap Map<String,String> map);
}
