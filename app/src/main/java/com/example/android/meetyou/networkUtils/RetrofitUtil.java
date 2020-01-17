package com.example.android.meetyou.networkUtils;

import com.example.android.framework.utils.LogUtils;
import com.example.android.framework.utils.SHA1;
import com.example.android.meetyou.base.MyApp;
import com.example.android.framework.cloud.CloudManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by shishaobin on 2019/8/26
 */
public class RetrofitUtil {
    private static final int DEFAULT_TIME_OUT = 30;//链接超时时间
    private static final int DEFAULT_READ_TIME_OUT = 10;//读写 超时时间
    private final int RETRY_COUNT = 2;//请求失败重连次数
    private static Retrofit retrofit;
    private static OkHttpClient okHttpClient;
    private static Cache cache=new Cache(MyApp.getInstance().getCacheDir(),1024*1024*50);//缓存50mib


    private static OkHttpClient getOkHttpClient(){
        //参数
        final String Timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        final String Nonce = String.valueOf(Math.floor(Math.random() * 100000));
        final String Signature = SHA1.sha1(CloudManager.CLOUD_SECRET + Nonce + Timestamp);

        //开启Log日志
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.e(message);
            }
        });

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //请求头 拦截器  融云需要
        Interceptor headIntercptre = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Timestamp", Timestamp)
                        .addHeader("App-Key", CloudManager.CLOUD_KEY)
                        .addHeader("Nonce", Nonce)
                        .addHeader("Signature", Signature)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded");
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        if (okHttpClient==null){
            synchronized (RetrofitUtil.class){
                if (okHttpClient==null){

                    okHttpClient=new OkHttpClient.Builder()
                            .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)//连接超时时间
                            .readTimeout(DEFAULT_READ_TIME_OUT,TimeUnit.SECONDS)//读取超时时间
                            .writeTimeout(DEFAULT_READ_TIME_OUT,TimeUnit.SECONDS)//写入超时时间
                            .retryOnConnectionFailure(true)//错误重连
                            .addInterceptor(loggingInterceptor)//设置log模式
                            .addInterceptor(headIntercptre)
                            .cache(cache)
                            .build();
                }
            }
        }
        return okHttpClient;
    }

    private RetrofitUtil() {
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .client(getOkHttpClient())
                    .baseUrl(ApiConfig.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())//json转换成JavaBean
                    .build();
        }
    }

    public static RetrofitUtil getInstance(){
        return new RetrofitUtil();
    }

    /**
     * 获取对应的service
     * @param service
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service){
        return retrofit.create(service);
    }
    /**
     * 设置订阅 和 所在的线程环境
     */
    public <T> void toSubscribe(Observable<T> o, DisposableObserver<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry(RETRY_COUNT)
                .subscribe(s);
    }
}
