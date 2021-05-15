package com.example.daggerdemo;

import androidx.lifecycle.MutableLiveData;

import com.example.daggerdemo.BaseClasses.BaseViewModel;
import com.example.daggerdemo.Utils.ApiResponse;
import com.example.daggerdemo.Utils.ResponseListener;
import com.example.daggerdemo.Utils.SingleLiveEvent;
import com.example.daggerdemo.data.Networkmodels.AlbumPhotos;

import java.util.List;

import javax.inject.Inject;

public class MainActivityViewModel extends BaseViewModel {
    final DataManager dataManager;
    public SingleLiveEvent<Boolean> loadingStatus = new SingleLiveEvent<>();
    public MutableLiveData<ApiResponse<List<AlbumPhotos>>> responseMutableLiveData = new MutableLiveData<>();

    @Inject
    public MainActivityViewModel(DataManager dataManager) {
        super(dataManager);
        this.dataManager = dataManager;
    }

    public void getAlbumPhotos() {
        dataManager.getAlbumPhotos(new ResponseListener<List<AlbumPhotos>>() {
            @Override
            public void onStart() {
                loadingStatus.setValue(true);
            }

            @Override
            public void onFinish() {
                loadingStatus.setValue(false);
            }

            @Override
            public void onResponse(ApiResponse<List<AlbumPhotos>> apiResponse) {

                loadingStatus.setValue(false);
                if (apiResponse != null && apiResponse.data != null) {
                    responseMutableLiveData.setValue(apiResponse);
                }
            }
        });

    }
}
