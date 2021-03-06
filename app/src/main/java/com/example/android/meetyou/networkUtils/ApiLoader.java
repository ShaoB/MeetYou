package com.example.android.meetyou.networkUtils;

import com.example.android.meetyou.Bean.TokenModel;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shishaobin on 2019/8/26
 */
public class ApiLoader extends ObjectLoader{
    private static ApiService mApiService;
    public ApiLoader(){
        mApiService = RetrofitUtil.getInstance().create(ApiService.class);
    }

    //获取融云Token
    public Observable<TokenModel> getCloudToken(Map<String,String> map){
        return observe(mApiService.getCloudToken(map));
    }
}

/**
 * 将一些重复的操作提出来，放到父类以免Loader 里每个接口都有重复代码
 */
class ObjectLoader {
    /**
     * @param observable
     * @param <T>
     * @return
     */
    protected <T> Observable<T> observe(Observable<T> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
