package com.example.daggerdemo.Utils;

import com.example.daggerdemo.Utils.ApiResponse;

public interface ResponseListener<T> {
    void onStart();

    void onFinish();

    void onResponse(ApiResponse<T> apiResponse);
}
