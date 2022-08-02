package com.testapp.duplicatefileremover.Model;

import java.io.File;

import static com.testapp.duplicatefileremover.Model.TypeFile.UNKNOWN;

public class Duplicate {
    private File file;
    private boolean isChecked = true;
    private int typeFile=UNKNOWN;


    public int getTypeFile(){
        return  this.typeFile;
    }
    public int setTypeFile(int type){
        return  this.typeFile=type;
    }
    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }
}
