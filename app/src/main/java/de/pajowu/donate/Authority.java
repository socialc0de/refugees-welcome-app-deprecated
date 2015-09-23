package de.pajowu.donate;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Authority implements ClusterItem{
    public String fax;
    public String open_times;
    public String website;
    public LatLng location;
    public String email;
    public String phone;
    public String address;
    public String source;
    public Authority() {

    }
    public String getDetailText() {
        String detail = "";
        detail += address + "\n";
        detail += "Fax: " + fax + "\n";
        detail += "Phone: " + phone + "\n";
        detail += "eMail: " + email + "\n";
        detail += "Opening times: " + open_times + "\n";
        detail += "Website: " + website + "\n";
        detail += "Data from " + source;
        return detail;
    }

    @Override
    public LatLng getPosition() {
        return location;
    }
}