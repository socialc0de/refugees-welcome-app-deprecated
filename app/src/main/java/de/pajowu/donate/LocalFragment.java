package de.pajowu.donate;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

import com.appspot.donate_backend.donate.*;
import com.appspot.donate_backend.donate.Donate.Builder;
import com.appspot.donate_backend.donate.model.*;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.github.androidprogresslayout.ProgressLayout;
import com.google.android.gms.maps.model.LatLng;

import android.location.LocationManager;
import android.location.Criteria;
import android.location.Location;

import android.support.v4.view.ViewPager;

import com.melnykov.fab.FloatingActionButton;
public class LocalFragment extends Fragment implements View.OnClickListener {
    public ArrayList<ListItem> offerList;
    public ListView listView;
    public Context context;
    public FragmentTabHost mTabHost;
    public Bundle bundleTab1 = new Bundle();
    View viewRoot;

    /*public LocalFragment(Context context, ArrayList<ListItem> offerList, ArrayList<ListItem> searchList, ArrayList<ListItem> allList) {
        this.offerList = offerList;
        this.searchList = searchList;
        this.allList = allList;
        this.context = context;

        Log.d("LocalFragment called", "");
    }*/
    public LocalFragment(Context context) {
        this.context = context;

        Log.d("LocalFragment called", "");
    }

    public LocalFragment() {
        Log.d("LocalFragment called", "no Args");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_list_fab, container, false);
        Log.d("MainActivity","onCreateView Local");
        //Implementation of custom Toolbar

        /*SpannableString s = new SpannableString(getString(R.string.app_name));
        s.setSpan(new de.pajowu.donate.TypefaceSpan(context, "fabiolo.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((MaterialNavigationDrawer) this.getActivity()).getToolbar().setTitle(s);*/
        if (offerList != null) {
            fillLayout();
        } else {
            loadFragmentData();
        }
        return viewRoot;
    }
    public void fillLayout() {
        try {
            FloatingActionButton editButton = (FloatingActionButton) viewRoot.findViewById(R.id.fab);
            editButton.setOnClickListener(this);
            editButton.setVisibility(View.VISIBLE);
            Log.d("MainActivity","fillLayout Locals");
            ArrayList<ListTabFragment> tbs = new ArrayList<ListTabFragment>();
            tbs.add(new ListTabFragment(this.offerList, "Offer"));
            Log.d("MainActivity","create ViewPagerAdapter");
            ViewPagerAdapter adapter =  new ViewPagerAdapter(getChildFragmentManager(),tbs);
            Log.d("MainActivity","created ViewPagerAdapter");
            // Assigning ViewPager View and setting the adapter
            ViewPager pager = (ViewPager) viewRoot.findViewById(R.id.pager);
            pager.setAdapter(adapter);

            // Assiging the Sliding Tab Layout View
            SlidingTabLayout tabs = (SlidingTabLayout) viewRoot.findViewById(R.id.tabs);
            tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
     
            // Setting Custom Color for the Scroll bar indicator of the Tab View
            tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                @Override
                public int getIndicatorColor(int position) {
                    return getResources().getColor(R.color.accentColor);
                }
            });
     
            // Setting the ViewPager For the SlidingTabsLayout
            tabs.setViewPager(pager); 
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void loadFragmentData() {
        ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showProgress();
        /*LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                loadData(location);
            }
         
            public void onStatusChanged(String provider, int status, Bundle extras) {}
         
            public void onProviderEnabled(String provider) {}
         
            public void onProviderDisabled(String provider) {}
          };
         
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);*/
        loadData(getLocation());
    }
    public void loadData(final Location loc) {
        Log.d("MainActivity",loc.toString());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Builder endpointBuilder = new Donate.Builder(
                        AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(),
                        CloudEndpointBuilderHelper.getRequestInitializer());

                Donate service = CloudEndpointBuilderHelper.updateBuilder(endpointBuilder).build();

                OfferProtoIdTitleSubtitleImageUrlsCategoriesCollection result;
                //LatLng loc = getLocation();

                //Log.d("MainActivity", loc.toString());
                if (loc != null)  {


                    double r = 6371;  // earth radius in km

                    double radius = 10; // km

                    String bbox = new Double(loc.getLongitude() - Math.toDegrees(radius / r / Math.cos(Math.toRadians(loc.getLatitude())))).toString() + ",";
                    bbox += new Double(loc.getLatitude() - Math.toDegrees(radius / r)).toString() + ",";
                    bbox += new Double(loc.getLongitude() + Math.toDegrees(radius / r / Math.cos(Math.toRadians(loc.getLatitude())))).toString() + ",";
                    bbox += new Double(loc.getLatitude() + Math.toDegrees(radius / r)).toString();

                    try {
                        result = service.offer().listNear().setBbox(bbox).execute();
                        Log.d("MainActivity", result.toString());
                        offerList = new ArrayList<ListItem>();
                        if (result.getItems() != null) {
                            for (OfferProtoIdTitleSubtitleImageUrlsCategories off : result.getItems()) {
                                ListItem li = new ListItem("", off.getTitle(), off.getSubtitle(), "CAT", off.getId());
                                String catstr = "";
                                for (String cat : off.getCategories()) {
                                    Category category = ((MainActivity) getActivity()).categories.get(cat);
                                    if (category != null) {
                                        catstr += category.getName() + " ";
                                    }
                                
                                }
                                li.category = catstr;
                                if (off.getImageUrls() != null) {
                                    //IMAGES.add(off.getImageUrls().get(0));
                                    // tmp fix, save image from url, give the path to HomeFragment
                                    li.resourceImage = off.getImageUrls().get(0);
                                }
                                offerList.add(li);
                            }
                        }

                    } catch (UserRecoverableAuthIOException e) {
                        final UserRecoverableAuthIOException e2 = e;
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                startActivityForResult(e2.getIntent(), 2);
                            }
                        });
                        Log.d("MainActivity", "e", e);
                    } catch (Exception e) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showErrorText("Couldn't load offers");
                            }
                        });
                        Log.d("MainActivity", "e", e);
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            fillLayout();
                            ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showContent();
                        }
                    });
                } else {
                     getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showErrorText("Couldn't get Location");
                        }
                    });
                }
            }
        };
        new Thread(runnable).start();
    }
    /*public LatLng getLocation() {
        // Get the location manager
        LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(getActivity().getApplicationContext().LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        Double lat, lon;
        try {
            lat = location.getLatitude();
            lon = location.getLongitude();
            return new LatLng(lat, lon);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }*/
    public Location getLocation() {
        // Get the location manager
        LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(getActivity().getApplicationContext().LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        return location;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Log.d("MainActivity", "pressed");
                //((MaterialNavigationDrawer) getActivity()).setFragment(new NewOfferFragment(context),"New Offer");
                //((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showErrorText("New Offer is not implemented yet");
                //TODO Set Editable = true (search fitting code for it
                //TODO Maybe add lines again to make obvious, that they can be edited
                break;
        }
    }
}
