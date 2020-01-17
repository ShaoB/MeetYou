package com.example.android.meetyou.networkUtils;

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

    public static ApiService getApiService(){
        return  mApiService;
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
