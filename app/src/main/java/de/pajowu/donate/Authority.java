package de.pajowu.donate;
import com.google.android.gms.maps.model.LatLng;

public class Authority {
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
}