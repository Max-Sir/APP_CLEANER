package com.security.applock.data;

public interface RxCallback<T> {
    void onSuccess(T data);

    default void onError(String errorMessage){}
}

