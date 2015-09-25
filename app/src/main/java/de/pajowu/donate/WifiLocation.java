package de.pajowu.donate;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class WifiLocation implements ClusterItem{
    public String detail = "wifi";
    public LatLng location;

    public String getDetailText() {
        return detail;
    }

    @Override
    public LatLng getPosition() {
        return location;
    }
}