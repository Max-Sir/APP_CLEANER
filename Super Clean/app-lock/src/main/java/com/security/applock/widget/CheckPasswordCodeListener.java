package com.security.applock.widget;

public interface CheckPasswordCodeListener {
    void onCheck(State state, String passInput);

    enum State {
        SUCCESS, FAILED
    }
}
