package de.pajowu.donate;

import android.content.Context;

public class ContactRow {
    private String title;
    private int imgIcon;
    private String url;

    public ContactRow() {

    }

    public ContactRow(Context context, String tle, String img, String url) {
        this.title = tle;
        this.url = url;
        this.imgIcon = context.getResources().getIdentifier("ic_" + img + "_grey600", "drawable", context.getPackageName());
    }

    public ContactRow(Context context, String tle, String img) {
        this.title = tle;
        this.imgIcon = context.getResources().getIdentifier("ic_" + img + "_grey600", "drawable", context.getPackageName());
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