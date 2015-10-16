package de.pajowu.donate.models;

import android.content.Context;
public class ProfileRow {
    private String title;
    private int imgIcon;
    private String url;

    public ProfileRow() {

    }
    public ProfileRow(Context context, String tle, String img, String url) {
        this.title = tle;
        this.url = url;
       // Context context = getContext();
        this.imgIcon = context.getResources().getIdentifier("ic_"+img+"_grey600", "drawable", context.getPackageName());
    }
    public ProfileRow(Context context, String tle, String img) {
        this.title = tle;
       // Context context = getContext();
        this.imgIcon = context.getResources().getIdentifier("ic_"+img+"_grey600", "drawable", context.getPackageName());
    }
    public String getTitle() {
        return title;
    }
 
    public void setTitle(String title) {
        this.title = title;
    }
 
    public int getImgIcon() {
        return imgIcon;
    }
 
    public void setImgIcon(int imgIcon) {
        this.imgIcon = imgIcon;
    }
    public String getUrl() {
        return url;
    }
}