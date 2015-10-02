package de.pajowu.donate;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Authority implements ClusterItem{
    //public String fax;
    public String open_times;
    public String website;
    public LatLng location;
    public String email;
    public String phone;
    public String address;
    //public String source;
    public Authority() {

    }
    public String getDetailText() {
        String detail = "";
        //detail += address + "\n";
        //detail += "Fax: " + fax + "\n";
        detail += "Opening times: " + open_times + "\n";
        //detail += "Data from " + source;
        return detail;
    }

    public String getAddress() {
        return address;
    }

    public String getWebsite() {
        return website;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public LatLng getPosition() {
        return location;
    }
}