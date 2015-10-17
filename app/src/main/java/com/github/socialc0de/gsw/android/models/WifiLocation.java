package com.github.socialc0de.gsw.android.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class WifiLocation implements ClusterItem {
    private String detail = "wifi";
    private LatLng location;

    public String getDetailText() {
        return detail;
    }

    @Override
    public LatLng getPosition() {
        return location;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}