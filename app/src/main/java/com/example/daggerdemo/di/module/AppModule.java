package com.example.daggerdemo.di.module;

import android.app.Application;
import android.content.Context;

import com.example.daggerdemo.BaseClasses.BaseScheduler;
import com.example.daggerdemo.DataManager;
import com.example.daggerdemo.SchedulerProvider;
import com.example.daggerdemo.data.Networksource.ApiSource;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module(includes = {ViewModelModule.class})
public class AppModule {

    //provide context
    @Singleton
    @Provides
    Context getContext(Application application) {
        return application;
    }

    //provide schedular
    @Singleton
    @Provides
    BaseScheduler getSchedular() {
        return new SchedulerProvider();
    }

    //provide ApiSource

    @Singleton
    @Provides
    ApiSource getApiSource(Retrofit retrofit) {
        return retrofit.create(ApiSource.class);
    }

    //provide datamanager
    @Singleton
    @Provides
    DataManager getDataManager(ApiSource apiSource, BaseScheduler baseScheduler) {
        return new DataManager(apiSource, baseScheduler);
    }

    @Singleton
    @Provides
    Picasso picasso(Context app, OkHttp3Downloader okHttp3Downloader) {
        return new Picasso.Builder(app.getApplicationContext())
                .downloader(okHttp3Downloader)
                .loggingEnabled(true)
                .build();
    }
}
