package com.example.daggerdemo.di.component;

import android.app.Application;

import com.example.daggerdemo.MyApplication;
import com.example.daggerdemo.di.module.ActivityModule;
import com.example.daggerdemo.di.module.AppModule;
import com.example.daggerdemo.di.module.NetworkModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {AndroidInjectionModule.class, AppModule.class, ActivityModule.class, NetworkModule.class})
public interface AppComponent {

    void inject(MyApplication myApplication);
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
