package com.example.daggerdemo.di.module;

import com.example.daggerdemo.MainActivityViewModel;
import com.example.daggerdemo.di.Scopes.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel.class)
    abstract MainActivityViewModel bindMainActivityViewModel(MainActivityViewModel mainActivityViewModel);
}
