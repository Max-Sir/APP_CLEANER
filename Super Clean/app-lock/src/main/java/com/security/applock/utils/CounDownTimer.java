package com.security.applock.utils;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public abstract class CounDownTimer {
    private TimeUnit timeUnit;
    private int startValue;
    private int period;
    private Disposable disposable;

    public CounDownTimer(int startValue,int period, TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
        this.period = period;
        this.startValue = startValue + 1;
    }

    public abstract void onTick(int tickValue);

    public abstract void onFinish();

    public CounDownTimer start() {
        Observable.zip(
                Observable.range(1, startValue), Observable.interval(period, timeUnit), (integer, aLong) -> {
                    int l = startValue - integer;
                    return l;
                }
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        onTick(integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        onFinish();
                    }
                });
        return this;
    }

    public Disposable getDisposable() {
        return disposable;
    }
}
