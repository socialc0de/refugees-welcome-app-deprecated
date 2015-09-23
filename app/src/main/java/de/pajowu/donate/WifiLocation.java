package de.pajowu.donate;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class WifiLocation implements ClusterItem{
    public String name;
    public LatLng location;

    public String getWifiInfo() {
        return name;
    }

    @Override
    public LatLng getPosition() {
        return location;
    }
}