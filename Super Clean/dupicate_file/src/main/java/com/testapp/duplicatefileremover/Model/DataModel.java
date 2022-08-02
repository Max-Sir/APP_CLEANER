package com.testapp.duplicatefileremover.Model;

import java.util.ArrayList;

public class DataModel {
    String titleGroup;
    ArrayList<Duplicate> listDuplicate;


    public String getTitleGroup() {
        return titleGroup;
    }

    public void setTitleGroup(String str_folder) {
        this.titleGroup = str_folder;
    }

    public ArrayList<Duplicate> getListDuplicate() {
        return listDuplicate;
    }

    public void setListDuplicate(ArrayList<Duplicate> mListDuplicate) {
        this.listDuplicate = mListDuplicate;
    }
}
