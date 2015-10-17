package de.pajowu.donate.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Authority implements ClusterItem {
    //public String fax;
    private String open_times;
    private String website;
    private LatLng location;
    private String email;
    private String phone;
    private String address;

    //public String source;
    public Authority() {

    }

    public String getOpen_times() {
        return open_times;
    }

    public void setOpen_times(String open_times) {
        this.open_times = open_times;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public LatLng getPosition() {
        return location;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}