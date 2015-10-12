package de.pajowu.donate;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
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

    private Cluster<Authority> clickedCluster;
    private Authority clickedClusterItem;

    private Cluster<WifiLocation> clickedCluster2;
    private WifiLocation clickedClusterItem2;


    public AuthorityMapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("MainActivity", "onCreateView");
        View inflatedView = inflater.inflate(R.layout.fragment_authority_map, container, false);
        wifiButton = (FloatingActionButton) inflatedView.findViewById(R.id.wifi);
        wifiButton.setColorNormalResId(R.color.accentColor);
        wifiButton.setColorPressedResId(R.color.accentColor2);
        wifiButton.setColorRippleResId(R.color.accentColor);
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
        setUpClusterer("authorities");
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
                auth.open_times = (String) jo_inside.get("offnungszeiten");
                auth.website = (String) jo_inside.get("website");
                Double lat = (Double) ((JSONObject) jo_inside.get("location")).get("lat");
                Double lng = (Double) ((JSONObject) jo_inside.get("location")).get("lng");
                auth.location = new LatLng(lat, lng);
                auth.email = (String) jo_inside.get("email");
                auth.phone = (String) jo_inside.get("telefon");
                auth.address = (String) jo_inside.get("adresse");
                //auth.source = "www.amt-de.com";
                // Source will be mentioned in "About"
                mClusterManager2.addItem(auth);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
            if (mClusterManager == null) {
                mClusterManager = new ClusterManager<WifiLocation>(getActivity(), mMap);
                mClusterManager.setRenderer(new DefaultClusterRenderer<WifiLocation>(getActivity(), mMap, mClusterManager));
                mClusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(new ClusterInfoWindow());
                mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MarkerInfoWindowAdapter(true));
                mClusterManager
                        .setOnClusterClickListener(new ClusterManager.OnClusterClickListener<WifiLocation>() {
                            @Override
                            public boolean onClusterClick(Cluster<WifiLocation> cluster) {
                                clickedCluster2 = cluster;
                                return false;
                            }
                        });

                mClusterManager
                        .setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<WifiLocation>() {
                            @Override
                            public boolean onClusterItemClick(WifiLocation item) {
                                clickedClusterItem2 = item;
                                return false;
                            }
                        });


                mClusterManager.cluster();
                CSVLoader csvLoader = new CSVLoader();
                csvLoader.execute();
            }
            mMap.setOnCameraChangeListener(mClusterManager);
            mMap.setOnInfoWindowClickListener(mClusterManager);
            mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
            mMap.setOnMarkerClickListener(mClusterManager);

            
        } else {
            if (mClusterManager2 == null) {
                mClusterManager2 = new ClusterManager<Authority>(getActivity(), mMap);
                mClusterManager2.setRenderer(new MyClusterRenderer(getActivity(), mMap, mClusterManager2));
                mClusterManager2.getClusterMarkerCollection().setOnInfoWindowAdapter(new ClusterInfoWindow());
                mClusterManager2.getMarkerCollection().setOnInfoWindowAdapter(new MarkerInfoWindowAdapter(false));
                
                mClusterManager2
                        .setOnClusterClickListener(new ClusterManager.OnClusterClickListener<Authority>() {
                            @Override
                            public boolean onClusterClick(Cluster<Authority> cluster) {
                                clickedCluster = cluster;
                                return false;
                            }
                        });

                mClusterManager2
                        .setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<Authority>() {
                            @Override
                            public boolean onClusterItemClick(Authority item) {
                                clickedClusterItem = item;
                                return false;
                            }
                        });


                mClusterManager2.cluster();

                loadAuthoritiesFromAsset();
            }
            mMap.setOnCameraChangeListener(mClusterManager2);
            mMap.setOnInfoWindowClickListener(mClusterManager2);
            mMap.setInfoWindowAdapter(mClusterManager2.getMarkerManager());
            mMap.setOnMarkerClickListener(mClusterManager2);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.wifi) {
            if (!wifi) {
                mMap.clear();
                wifiButton.setImageResource(R.drawable.ic_info_outline_white);

                // Sending little Message to screen
                Toast.makeText(getActivity(), "Loading Wifi Hotspot Locations",
                        Toast.LENGTH_LONG).show();

                setUpClusterer("wifi");
                wifi = true;
            } else {
                mMap.clear();
                wifiButton.setImageResource(R.drawable.ic_network_wifi_white);

                // Sending little Message to screen
                Toast.makeText(getActivity(), "Loading Administration Locations",
                        Toast.LENGTH_LONG).show();

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
            for (WifiLocation w : wifiLocations) {
                mClusterManager.addItem(w);
            }
            super.onPostExecute(wifiLocations);
        }
    }

    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private final View myContentsView;
        private boolean wifiBoolean = false;

        public MarkerInfoWindowAdapter(boolean wifi) {
            wifiBoolean = wifi;
            myContentsView = getActivity().getLayoutInflater().inflate(
                    R.layout.map_info_window_dialog, null);

        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub


            TextView tvTitle = ((TextView) myContentsView
                    .findViewById(R.id.txtHeader));
            TextView tvSnippet = ((TextView) myContentsView
                    .findViewById(R.id.txtAddress));
            LinearLayout linearLayout = (LinearLayout) myContentsView.findViewById(R.id.icons);
            TextView location = (TextView) myContentsView.findViewById(R.id.location);
            TextView phone = (TextView) myContentsView.findViewById(R.id.phone);
            TextView mail = (TextView) myContentsView.findViewById(R.id.mail);
            TextView homepage = (TextView) myContentsView.findViewById(R.id.homepage);
            TextView openingtimes = (TextView) myContentsView.findViewById(R.id.openingtimes);


            if (clickedClusterItem != null || clickedClusterItem2 != null) {
                if (wifiBoolean) {
                    tvTitle.setText("Wifi");
                    tvSnippet.setText("Free Wifi HotSpot");
                    linearLayout.setVisibility(View.GONE);
                } else {
                    tvSnippet.setVisibility(View.GONE);
                    tvTitle.setText("Authority Information:");
                    location.setText(clickedClusterItem.getAddress());
                    phone.setText(clickedClusterItem.getPhone());
                    mail.setText(clickedClusterItem.getEmail());
                    homepage.setText(clickedClusterItem.getWebsite());
                    //Log.d("clickedClusterI: "+clickedClusterItem.getDetailText(),"");
                    openingtimes.setText(clickedClusterItem.getOpen_times());


                }

            }
            return myContentsView;
        }


    }

    public class ClusterInfoWindow implements GoogleMap.InfoWindowAdapter {
        private final View myContentsView;

        ClusterInfoWindow() {
            myContentsView = getActivity().getLayoutInflater().inflate(
                    R.layout.map_info_window_dialog, null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub


            TextView tvTitle = ((TextView) myContentsView
                    .findViewById(R.id.txtHeader));
            TextView tvSnippet = ((TextView) myContentsView
                    .findViewById(R.id.txtAddress));
            tvSnippet.setVisibility(View.GONE);

            if (clickedCluster != null) {
                tvTitle.setText(String.valueOf(clickedCluster.getItems().size()) + " more items");
            }
            if (clickedCluster2 != null) {
                tvTitle.setText(String.valueOf(clickedCluster2.getItems().size()) + " more items");
            }


            return myContentsView;
        }
    }

    class MyClusterRenderer extends DefaultClusterRenderer<Authority> {


        public MyClusterRenderer(Context context, GoogleMap map, ClusterManager<Authority> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(Authority item, MarkerOptions markerOptions) {
            super.onBeforeClusterItemRendered(item, markerOptions);
        }

        @Override
        protected void onClusterItemRendered(Authority clusterItem, Marker marker) {
            super.onClusterItemRendered(clusterItem, marker);
        }
    }


}