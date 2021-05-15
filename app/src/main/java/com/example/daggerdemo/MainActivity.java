package com.example.daggerdemo;

import androidx.lifecycle.Observer;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.daggerdemo.BaseClasses.BaseActivity;
import com.example.daggerdemo.Utils.ApiResponse;
import com.example.daggerdemo.data.Networkmodels.AlbumPhotos;
import com.example.daggerdemo.databinding.ActivityMainBinding;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainActivityViewModel> {

    private ActivityMainBinding activityMainBinding;
    private ProgressDialog progressDialog;
    @Inject
    MainActivityViewModel mainActivityViewModel;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainActivityViewModel getViewModel() {
        return mainActivityViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = getViewDataBinding();
        progressDialog = new ProgressDialog(this);
        mainActivityViewModel.getAlbumPhotos();
        mainActivityViewModel.loadingStatus.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    showProgressBar();
                } else {
                    progressDialog.dismiss();
                }
            }
        });
        mainActivityViewModel.responseMutableLiveData.observe(this, new Observer<ApiResponse<List<AlbumPhotos>>>() {
            @Override
            public void onChanged(ApiResponse<List<AlbumPhotos>> albumPhotosApiResponse) {
                if (albumPhotosApiResponse != null && albumPhotosApiResponse.data != null) {
                    List<AlbumPhotos> albumPhotos = albumPhotosApiResponse.data;
                    Log.i("check",albumPhotosApiResponse.data.get(0).getTitle());
                    Toast.makeText(getApplicationContext(),albumPhotosApiResponse.data.get(0).getTitle(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showProgressBar() {
        progressDialog.show();
    }
}
