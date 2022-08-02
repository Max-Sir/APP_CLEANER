package com.lubuteam.sellsourcecode.supercleaner.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BatteryInfo implements Parcelable {
    public static final String BATTERY_INFO_KEY = "battery_info_key";
    public int level = -1;
    public int scale = -1;
    public int temperature = -1;
    public int voltage =-1;
    public String technology = "";
    public int plugged = 0;
    public int status = -1;
    public int health = -1;
    public int hourleft = 0;
    public int minleft = 0;
    @SuppressWarnings("rawtypes")
    public static final Creator CREATOR = new Creator() {
        public BatteryInfo createFromParcel(Parcel in) {
            return new BatteryInfo(in);
        }

        public BatteryInfo[] newArray(int size) {
            return new BatteryInfo[size];
        }
    };
    public BatteryInfo() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeInt(level);
        dest.writeInt(scale);
        dest.writeInt(temperature);
        dest.writeInt(voltage);
        dest.writeInt(status);
        dest.writeInt(health);
        dest.writeInt(hourleft);
        dest.writeInt(minleft);
        dest.writeInt(plugged);
        dest.writeString(technology);
    }
    public BatteryInfo(Parcel in) {
        // TODO Auto-generated constructor stub
        level = in.readInt();
        scale = in.readInt();
        temperature = in.readInt();
        voltage = in.readInt();
        status = in.readInt();
        health = in.readInt();
        hourleft = in.readInt();
        minleft = in.readInt();
        plugged = in.readInt();
        technology = in.readString();
    }
    public String toString(){
        String string;
        string = "level"+ level  +" temperature"+temperature+" voltage"+voltage;
        return string;
    }
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

}
