package com.testapp.duplicatefileremover.utilts;

import android.os.Environment;

import java.io.File;

public class Config {
    public static final int DATA = 1000;
    public static final int REPAIR = 2000;
    public static final int UPDATE = 3000;
    public static final String IMAGE_RECOVER_DIRECTORY;

    static {
        StringBuilder sbDirectory = new StringBuilder();
        sbDirectory.append(Environment.getExternalStorageDirectory());
        sbDirectory.append(File.separator);
        sbDirectory.append("RestoredPhotos");
        IMAGE_RECOVER_DIRECTORY = sbDirectory.toString();
    }
}
