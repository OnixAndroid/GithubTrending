package com.app.githubtrending.domain.common;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AppSchedulers {
    public Scheduler main() {
        return AndroidSchedulers.mainThread();
    }
    public Scheduler io() {
        return Schedulers.io();
    }
}
