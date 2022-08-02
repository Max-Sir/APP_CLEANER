package com.testapp.duplicatefileremover.Model;

public class TypeFile {
    public final static int IMAGE=0;
    public final static int AUDIO=1;
    public final static int VIDEO=2;
    public final static int DOCUMENT=3;
    public final static int APK=4;
    public final static int VCF=5;
    public final static int ZIP=6;
    public final static int PDF=7;
    public final static int UNKNOWN=8;
    public static int getType(String fileName) {
        if (fileName.endsWith(".apk")) {

            return APK;
        } else if (fileName.endsWith(".zip")) {
            return ZIP;
        } else if (fileName.endsWith(".vcf")) {
            return VCF;
        }
        else if (fileName.endsWith(".mp3")
                ||fileName.endsWith(".aac")
                ||fileName.endsWith(".amr")
                ||fileName.endsWith(".m4a")
                ||fileName.endsWith(".ogg")
                ||fileName.endsWith(".wav")
                ||fileName.endsWith(".flac")) {
            return  AUDIO;
       } else if (fileName.endsWith(".3gp")
                ||fileName.endsWith(".mp4")
                ||fileName.endsWith(".mkv")
                ||fileName.endsWith(".webm")) {

            return  VIDEO;
        }else if (fileName.endsWith(".doc")
                ||fileName.endsWith(".docx")
                ||fileName.endsWith(".html")
                ||fileName.endsWith(".txt")
                ||fileName.endsWith(".xml")
                ||fileName.endsWith(".xlsx")) {

            return  DOCUMENT;
        }else if (fileName.endsWith(".jpg")
                ||fileName.endsWith(".jpeg")
                ||fileName.endsWith(".png")
                ||fileName.endsWith(".bmp")
                ||fileName.endsWith(".gif")) {

            return  IMAGE;
        }else if (fileName.endsWith(".pdf")) {

        return  PDF;
        }
        return UNKNOWN;
    }

}
