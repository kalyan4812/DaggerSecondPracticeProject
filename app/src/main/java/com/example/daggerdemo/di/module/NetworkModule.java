package com.example.daggerdemo.di.module;

import android.content.Context;
import android.net.NetworkInfo;
import android.util.Log;

import com.ncornette.cache.OkCacheControl;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.util.concurrent.TimeUnit.MINUTES;

@Module
public class NetworkModule {
    private static final String PRAGMA_HEADER = "Pragma";
    private static final String CACHE_CONTROL_HEADER = "Cache-Control";
    private static final long CONNECTION_TIMEOUT = 30000;
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";

    //providing file for cache storage
    @Singleton
    @Provides
    File getFile(Context context) {
        File file = new File(context.getCacheDir(), "my_network_cache");
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    //providing cache of 10MB size
    @Singleton
    @Provides
    Cache getCache(File file) {
        return new Cache(file, 10 * 1024 * 1024);
    }

    //providing httplogginginterceptor
    @Singleton
    @Provides
    HttpLoggingInterceptor getHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    //providing Interceptor
    @Singleton
    @Provides
    Interceptor getInterceptor(Context context) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                CacheControl cacheControl;

                if (isInternetAvailable()) {
                    cacheControl = new CacheControl.Builder()
                            .maxAge(300, TimeUnit.SECONDS)
                            .build();
                    Log.i("check", cacheControl.toString() + "\n" + "internt avaialabe");
                } else {
                    cacheControl = new CacheControl.Builder()
                            .maxAge(7, TimeUnit.DAYS)
                            .build();
                    Log.d("check", cacheControl.toString() + "\n" + " NO internt avaialabe");
                }


                return response.newBuilder()
                        .removeHeader(PRAGMA_HEADER)
                        .removeHeader(CACHE_CONTROL_HEADER)
                        .addHeader(CACHE_CONTROL_HEADER, cacheControl.toString())
                        .build();
            }
        };

    }


    //checking connection
    private boolean isConnected(Context context) {
        try {
            android.net.ConnectivityManager e = (android.net.ConnectivityManager) context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = e.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        } catch (Exception e) {
            Log.w("", e.toString());
        }

        return false;
    }

    public static boolean isInternetAvailable() {
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("www.google.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(2000, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
        }
        return inetAddress != null && !inetAddress.equals("");
    }

    //providing okhttpclient
    @Singleton
    @Provides
    OkHttpClient getokhttpclient(Cache cache, HttpLoggingInterceptor httpLoggingInterceptor, Interceptor interceptor) {
        return new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).addInterceptor(interceptor).cache(cache)
                .readTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS).
                        connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS).
                        writeTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();


    }

    //provide retrofit instance

    @Singleton
    @Provides
    Retrofit getRetrofitInstance(OkHttpClient okHttpClient) {
        return new Retrofit.Builder().client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL).build();
    }

}
