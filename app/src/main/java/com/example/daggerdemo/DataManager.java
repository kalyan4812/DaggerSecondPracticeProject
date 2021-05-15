package com.example.daggerdemo;

import android.util.Log;

import com.example.daggerdemo.BaseClasses.BaseScheduler;
import com.example.daggerdemo.Utils.ApiResponse;
import com.example.daggerdemo.Utils.ResponseListener;
import com.example.daggerdemo.Utils.ResponseStatus;
import com.example.daggerdemo.data.Networkmodels.AlbumPhotos;
import com.example.daggerdemo.data.Networksource.ApiSource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class DataManager {
    final ApiSource apiSource;
    final BaseScheduler baseScheduler;

    @Inject
    public DataManager(ApiSource apiSource, BaseScheduler baseScheduler) {
        this.apiSource = apiSource;
        this.baseScheduler = baseScheduler;
    }

    private <T> void performRequest(Observable<List<T>> observable, ResponseListener<List<T>> responseListener) {
        observable.subscribeOn(baseScheduler.io()).observeOn(baseScheduler.ui())
                .subscribe(new Observer<List<T>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        responseListener.onStart();
                    }

                    @Override
                    public void onNext(@NonNull List<T> ts) {
                        responseListener.onResponse(new ApiResponse(ResponseStatus.SUCCESS, ts, null));
                        Log.i("check",ts.get(0).toString());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        responseListener.onResponse(new ApiResponse<>(ResponseStatus.ERROR, null, e));
                    }

                    @Override
                    public void onComplete() {
                        responseListener.onFinish();
                    }
                });

    }

    public void getAlbumPhotos(ResponseListener<List<AlbumPhotos>> responseListener) {
        performRequest(apiSource.getPhotos(2), responseListener);
    }
}
