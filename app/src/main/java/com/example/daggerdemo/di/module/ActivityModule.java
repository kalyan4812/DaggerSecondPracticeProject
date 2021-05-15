package com.example.daggerdemo.di.module;

import com.example.daggerdemo.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public  abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract MainActivity contributesMainActivity();
}
