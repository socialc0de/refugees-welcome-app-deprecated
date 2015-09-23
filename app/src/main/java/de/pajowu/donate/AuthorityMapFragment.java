package de.pajowu.donate;

import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AuthorityMapFragment extends Fragment implements View.OnClickListener {

    private MapView mMapView;
    private GoogleMap mMap;
    private Bundle mBundle;
    private GeoJsonLayer mWifiLayer;
    private FloatingActionButton wifiButton;
    private boolean wifi = false;
    private ClusterManager<WifiLocation> mClusterManager;
    private ClusterManager<Authority> mClusterManager2;


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
        setUpClusterer("authorities");
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

    public void loadAuthoritiesFromAsset() {
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
                mClusterManager2.addItem(auth);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void loadWifiLocationsFromAssets() {
        InputStream is = null;
        try {
            is = getActivity().getAssets().open("wifimap.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] RowData = line.split(",");
                Log.d("WIFILOC: ", "RowData[0] = " + RowData[0]);
                Log.d("WIFILOC: ", "RowData[1] = " + RowData[1]);
                Double lat = Double.parseDouble(RowData[0]);
                Double lng = Double.parseDouble(RowData[1]);
                WifiLocation wLoc = new WifiLocation();
                wLoc.location = new LatLng(lat, lng);
                mClusterManager.addItem(wLoc);
            }
        } catch (IOException ex) {
            // handle exception
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // handle exception
            }
        }
    }

    public Location getLocation() {
        // Get the location manager
        LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(getActivity().getApplicationContext().LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        return location;
    }

    private void setUpClusterer(String cluster) {
        if (cluster.equals("wifi")) {
            mClusterManager = new ClusterManager<WifiLocation>(getActivity(), mMap);
            mMap.setOnCameraChangeListener(mClusterManager);
            mMap.setOnMarkerClickListener(mClusterManager);

            // Add cluster items (markers) to the cluster manager.
            //loadWifiLocationsFromAssets();
            CSVLoader csvLoader = new CSVLoader();
            csvLoader.execute();
        }
        else {
            mClusterManager2 = new ClusterManager<Authority>(getActivity(), mMap);
            mMap.setOnCameraChangeListener(mClusterManager2);
            mMap.setOnMarkerClickListener(mClusterManager2);

            // Add cluster items (markers) to the cluster manager.
            loadAuthoritiesFromAsset();

        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.wifi) {
            if (!wifi) {
                mMap.clear();
                wifiButton.setImageResource(R.drawable.ic_info_outline_white);
                setUpClusterer("wifi");
                wifi = true;
            } else {
                mMap.clear();
                wifiButton.setImageResource(R.drawable.ic_network_wifi_white);
                setUpClusterer("authorities");
                wifi = false;
            }
        }
    }

    private class CSVLoader extends AsyncTask<Void, Void, ArrayList<WifiLocation>> {

        protected ArrayList<WifiLocation> doInBackground(Void... urls) {
            ArrayList<WifiLocation> wifiLocations = new ArrayList<>();
            InputStream is = null;
            try {
                is = getActivity().getAssets().open("wifimap.csv");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] RowData = line.split(",");
                    //Log.d("WIFILOC: ", "RowData[0] = " + RowData[0]);
                    //Log.d("WIFILOC: ", "RowData[1] = " + RowData[1]);
                    Double lat = Double.parseDouble(RowData[0]);
                    Double lng = Double.parseDouble(RowData[1]);
                    WifiLocation wLoc = new WifiLocation();
                    wLoc.location = new LatLng(lat, lng);
                    wifiLocations.add(wLoc);
                }
            } catch (IOException ex) {
                // handle exception
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    // handle exception
                }
            }
            return wifiLocations;
        }

        @Override
        protected void onPostExecute(ArrayList<WifiLocation> wifiLocations) {
            for (WifiLocation w : wifiLocations){
                mClusterManager.addItem(w);
            }
            super.onPostExecute(wifiLocations);
        }
    }
}

/*
*   ASYNCTASK
 */



