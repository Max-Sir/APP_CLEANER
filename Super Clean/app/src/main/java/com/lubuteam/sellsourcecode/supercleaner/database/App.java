package com.lubuteam.sellsourcecode.supercleaner.database;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class App implements Parcelable {

	private String title;
	private String packageName;
	private String versionName;
	private int versionCode;
	private String sourcePath;
	// aloha
	private Drawable icon;
	
	public App(){
		
	}
			
	public App(Parcel in){
		
		title = in.readString();
		packageName = in.readString();
		versionName = in.readString();
		versionCode = in.readInt();
		sourcePath = in.readString();
		icon = new BitmapDrawable((Bitmap) in.readParcelable(Bitmap.class.getClassLoader()));
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getsourcePath() {
		return sourcePath;
	}

	public void setsourcePath(String path) {
		this.sourcePath = path;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
		dest.writeString(title);
		dest.writeString(packageName);
		dest.writeString(versionName);
		dest.writeInt(versionCode);
		dest.writeString(sourcePath);
		dest.writeParcelable(((BitmapDrawable) icon).getBitmap(), flags);
	}
	
	public static final Creator<App> CREATOR = new Creator<App>()
    {
        public App createFromParcel(Parcel in)
        {
            return new App(in);
        }
        public App[] newArray(int size)
        {
            return new App[size];
        }
    };
}
