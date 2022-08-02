package com.lubuteam.sellsourcecode.supercleaner.model;

import java.io.Serializable;
import java.util.List;

public class LstPkgNameTaskInfo implements Serializable {
    private List<String> lst;

    public List<String> getLst() {
        return lst;
    }

    public void setLst(List<String> lst) {
        this.lst = lst;
    }

    public LstPkgNameTaskInfo(List<String> lst) {
        this.lst = lst;
    }
}
