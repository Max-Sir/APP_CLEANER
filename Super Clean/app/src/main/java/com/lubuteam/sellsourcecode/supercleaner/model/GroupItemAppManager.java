package com.lubuteam.sellsourcecode.supercleaner.model;

import android.content.pm.ApplicationInfo;

import java.util.ArrayList;
import java.util.List;

public class GroupItemAppManager {

    public static final int TYPE_USER_APPS = 0;
    public static final int TYPE_SYSTEM_APPS = 1;

    private String title;
    private int total;
    private int type;
    private List<ApplicationInfo> items = new ArrayList<>();

    public GroupItemAppManager() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ApplicationInfo> getItems() {
        return items;
    }

    public void setItems(List<ApplicationInfo> items) {
        this.items = items;
    }
}
