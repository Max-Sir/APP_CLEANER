package com.security.applock.model;

public class IconApp {
    private int id;
    private String className;
    private int iconPreview;
    private String nameDisplay;

    public IconApp(int id, String className, int iconPreview, String nameDisplay) {
        this.id = id;
        this.className = className;
        this.iconPreview = iconPreview;
        this.nameDisplay = nameDisplay;
    }

    public String getNameDisplay() {
        return nameDisplay;
    }

    public void setNameDisplay(String nameDisplay) {
        this.nameDisplay = nameDisplay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getIconPreview() {
        return iconPreview;
    }

    public void setIconPreview(int iconPreview) {
        this.iconPreview = iconPreview;
    }
}
