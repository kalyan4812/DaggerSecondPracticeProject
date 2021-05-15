package com.example.daggerdemo.BaseClasses;

import androidx.lifecycle.ViewModel;

import com.example.daggerdemo.DataManager;

public abstract class BaseViewModel extends ViewModel {
    private final DataManager dataManager;

    public BaseViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
