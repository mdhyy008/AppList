package com.example.gray_dog3.applist;

import android.graphics.drawable.Drawable;



public class MyAppInfo {
    private Drawable image;
    private String appName;
	private String appNameText;
	
    public MyAppInfo(Drawable image, String appName,String appNameText) {
        this.image = image;
        this.appName = appName;
		this.appNameText = appNameText;
    }
    public MyAppInfo() {

    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
	
	
	public void setAppNameText(String appNameText) {
        this.appNameText = appNameText;
    }
	
}
