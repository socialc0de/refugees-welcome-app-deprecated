package de.pajowu.donate;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonPointStyle;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AuthorityMapFragment extends Fragment implements View.OnClickListener {

    private MapView mMapView;
    private GoogleMap mMap;
    private Bundle mBundle;
    private GeoJsonLayer mWifiLayer;
    private FloatingActionButton wifiButton;

    public AuthorityMapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("MainActivity", "onCreateView");
        /*SpannableString s = new SpannableString(getString(R.string.app_name));
        s.setSpan(new de.pajowu.donate.TypefaceSpan(getActivity().getApplicationContext(), "fabiolo.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((MaterialNavigationDrawer) this.getActivity()).getToolbar().setTitle(s);*/
        View inflatedView = inflater.inflate(R.layout.fragment_authority_map, container, false);
        wifiButton = (FloatingActionButton) inflatedView.findViewById(R.id.wifi);
        wifiButton.setOnClickListener(this);

        MapsInitializer.initialize(getActivity());
        mMapView = (MapView) inflatedView.findViewById(R.id.map);
        mMapView.onCreate(mBundle);
        setUpMapIfNeeded(inflatedView);

        return inflatedView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
    }

    private void setUpMapIfNeeded(View inflatedView) {
        mMap = ((MapView) inflatedView.findViewById(R.id.map)).getMap();
        if (mMap != null) {
            setUpMap();
        }
    }

    private void setUpMap() {
        Log.d("MainActivity", "setupMap");
        Location loc = getLocation();
        if (loc != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 12));
        }
        mMap.setMyLocationEnabled(true);
        ArrayList<Authority> auths = loadAuthoritiesFromAsset();
        Log.d("MainActivity", auths.toString());
        if (auths != null) {
            for (Authority auth : auths) {
                mMap.addMarker(new MarkerOptions().position(auth.location).snippet(auth.getDetailText()));
            }
        }


        String jsonWifi = null;
        JSONObject jsonWifiObject = null;
        InputStream is = null;
        try {
            is = getActivity().getAssets().open("wifihotspot_testing.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonWifi = new String(buffer, "UTF-8");
            jsonWifiObject = new JSONObject(jsonWifi.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mWifiLayer = new GeoJsonLayer(mMap, jsonWifiObject);

        GeoJsonPointStyle pointStyle = new GeoJsonPointStyle();
        pointStyle.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        pointStyle.setSnippet("Wifi");

        for (GeoJsonFeature gJF: mWifiLayer.getFeatures()){
            gJF.setPointStyle(pointStyle);


        }

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                View v = getActivity().getLayoutInflater().inflate(R.layout.marker, null);

                TextView info = (TextView) v.findViewById(R.id.info);

                info.setText(marker.getSnippet());

                return v;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    public ArrayList<Authority> loadAuthoritiesFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("authorities.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            //JSONObject obj = new JSONObject(json);
            JSONArray m_jArry = new JSONArray(json);
            ArrayList<Authority> formList = new ArrayList<Authority>();
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                Authority auth = new Authority();
                auth.fax = (String) jo_inside.get("fax");
                auth.open_times = (String) jo_inside.get("offnungszeiten");
                auth.website = (String) jo_inside.get("website");
                Double lat = (Double) ((JSONObject) jo_inside.get("location")).get("lat");
                Double lng = (Double) ((JSONObject) jo_inside.get("location")).get("lng");
                auth.location = new LatLng(lat, lng);
                auth.email = (String) jo_inside.get("email");
                auth.phone = (String) jo_inside.get("telefon");
                auth.address = (String) jo_inside.get("adresse");
                auth.source = "www.amt-de.com";
                formList.add(auth);
            }
            return formList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public Location getLocation() {
        // Get the location manager
        LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(getActivity().getApplicationContext().LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        return location;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.wifi){
            if (mWifiLayer.isLayerOnMap()){
                mWifiLayer.removeLayerFromMap();
            }
            else {
                mWifiLayer.addLayerToMap();
            }
        }
    }
}