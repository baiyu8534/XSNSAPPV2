package com.example.lenovo.xsnsappv2.api;

import android.util.Log;

import com.example.lenovo.xsnsappv2.BuildConfig;
import com.example.lenovo.xsnsappv2.MyApplication;
import com.example.lenovo.xsnsappv2.utils.NetWorkUtil;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class ApiManager {
    static final HttpLoggingInterceptor httpLoggingInterceptor;

    static {
        httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Response response = chain.proceed(chain.request());
            if (BuildConfig.DEBUG) Log.d("ApiManager", response.toString());
            if (NetWorkUtil.isNetWorkAvailable(MyApplication.getContext())) {
                // FIXME: 2017/10/12 0012 现在只有在线缓存，离线缓存读不到或没有，需要看下,不是读不到而是要缓存返回的json数据，这样才有图片地址去调用缓存
                //在线缓存2分钟内可读取
                int maxAge = 120;
                return response.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // 离线时缓存保存4周
                return response.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };

    private static ApiManager apiManager;
    private static File httpCacheDirectory = new File(MyApplication.getContext().getExternalCacheDir(), "MyAppCacheDir");
    //缓存50M
    private static long cacheSize = 50 * 1024 * 1024;
    private static Cache cache = new Cache(httpCacheDirectory, cacheSize);

    /* private static OkHttpClient.Builder builder = new OkHttpClient.Builder()
             //超时
             .connectTimeout(1000, TimeUnit.SECONDS)
             .readTimeout(1000, TimeUnit.SECONDS)
             .writeTimeout(1000, TimeUnit.SECONDS)
             //错误重连
             .retryOnConnectionFailure(true)
             .connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS));
             //cookie设置
             //.cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(App.getInstance())));
 */
    private static OkHttpClient client = new OkHttpClient.Builder()
            //超时
            .connectTimeout(1000, TimeUnit.SECONDS)
            .readTimeout(1000, TimeUnit.SECONDS)
            .writeTimeout(1000, TimeUnit.SECONDS)
            //错误重连
            .retryOnConnectionFailure(true)
            .connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS))
            .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
            //日志
            .addInterceptor(httpLoggingInterceptor)
            .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
            // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里15个，和每个保持时间为10s
            .connectionPool(new ConnectionPool(15, 10, TimeUnit.SECONDS))
            .cache(cache)
            .build();
    private static OkHttpClient pixClient = null;

    //同步锁
    private Object lock = new Object();

    private XsnsApi mXsnsApi;

    public static ApiManager getInstance() {
        if (apiManager == null) {
            synchronized (ApiManager.class) {
                if (apiManager == null) {
                    apiManager = new ApiManager();
                }
            }
        }
        return apiManager;
    }

    public XsnsApi getXsnsApiService() {
        synchronized (lock) {
            if (mXsnsApi == null) {
                synchronized (lock) {
                    if (mXsnsApi == null) {
                        mXsnsApi = new Retrofit.Builder()
                                .baseUrl("http://106.52.221.177")
                                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                .client(client)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build().create(XsnsApi.class);
                    }
                }
            }
        }
        return mXsnsApi;
    }

}
