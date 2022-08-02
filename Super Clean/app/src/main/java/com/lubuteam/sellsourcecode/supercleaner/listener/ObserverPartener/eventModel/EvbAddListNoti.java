package com.lubuteam.sellsourcecode.supercleaner.listener.ObserverPartener.eventModel;

import com.lubuteam.sellsourcecode.supercleaner.model.NotifiModel;

import java.util.List;

public class EvbAddListNoti extends ObserverAction {
    public List<NotifiModel> lst;

    public EvbAddListNoti(List<NotifiModel> lst) {
        this.lst = lst;
    }

}
