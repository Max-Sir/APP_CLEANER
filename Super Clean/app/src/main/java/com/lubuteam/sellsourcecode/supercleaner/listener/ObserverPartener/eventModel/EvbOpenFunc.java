package com.lubuteam.sellsourcecode.supercleaner.listener.ObserverPartener.eventModel;

import com.lubuteam.sellsourcecode.supercleaner.utils.Config;

public class EvbOpenFunc extends ObserverAction {
    public Config.FUNCTION mFunction;

    public EvbOpenFunc(Config.FUNCTION mFunction) {
        this.mFunction = mFunction;
    }
}
