package com.example.daggerdemo.BaseClasses;

import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;

public interface BaseScheduler {
    @NonNull
    Scheduler io();
    @NonNull
    Scheduler ui();
}
