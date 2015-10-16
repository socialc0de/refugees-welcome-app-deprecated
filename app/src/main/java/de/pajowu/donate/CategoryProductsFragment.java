package de.pajowu.donate;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import com.appspot.donate_backend.donate.*;
import com.appspot.donate_backend.donate.Donate.Builder;
import com.appspot.donate_backend.donate.model.*;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.github.androidprogresslayout.ProgressLayout;

import android.location.LocationManager;
import android.location.Location;

import android.support.v4.view.ViewPager;
import java.util.Arrays;

import de.pajowu.donate.models.ListItem;

public class CategoryProductsFragment extends Fragment {
    public ArrayList<ListItem> offerList;
    public ListView listView;
    public Context context;
    public FragmentTabHost mTabHost;
    public Bundle bundleTab1 = new Bundle();
    View viewRoot;
    String cat_id;
    public CategoryProductsFragment(Context cont, String catId) {
        this.context = cont;
        this.cat_id = catId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_list_nofab, container, false);
        loadFragmentData(getLocation());
        return viewRoot;
    }
    public void fillLayout() {
        ArrayList<ListTabFragment> tbs = new ArrayList<ListTabFragment>();
        tbs.add(new ListTabFragment(this.offerList, getString(R.string.offer)));
        ViewPagerAdapter adapter =  new ViewPagerAdapter(getChildFragmentManager(),tbs);
 
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
    }
    public void loadFragmentData(final Location loc) {
        ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showProgress();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Builder endpointBuilder = new Donate.Builder(
                        AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(),
                        CloudEndpointBuilderHelper.getRequestInitializer());

                Donate service = CloudEndpointBuilderHelper.updateBuilder(endpointBuilder).build();

                OfferProtoIdTitleSubtitleImageUrlsCategoriesLatLonCollection result;
                if (loc != null)  {


                    double r = 6371;  // earth radius in km

                    double radius = 10; // km

                    String bbox = new Double(loc.getLongitude() - Math.toDegrees(radius / r / Math.cos(Math.toRadians(loc.getLatitude())))).toString() + ",";
                    bbox += new Double(loc.getLatitude() - Math.toDegrees(radius / r)).toString() + ",";
                    bbox += new Double(loc.getLongitude() + Math.toDegrees(radius / r / Math.cos(Math.toRadians(loc.getLatitude())))).toString() + ",";
                    bbox += new Double(loc.getLatitude() + Math.toDegrees(radius / r)).toString();
                    try {
                        result = service.offer().bycat().setCategories(Arrays.asList(cat_id)).setBbox(bbox).execute();
                        offerList = new ArrayList<ListItem>();
                        if (result.getItems() != null) {
                            for (OfferProtoIdTitleSubtitleImageUrlsCategoriesLatLon off : result.getItems()) {
                                ListItem li = new ListItem("", off.getTitle(), off.getSubtitle(), "CAT", off.getId());
                                String catstr = "";
                                for (String cat : off.getCategories()) {
                                    Category category = ((MainActivity) getActivity()).categories.get(cat);
                                    if (category != null) {
                                        catstr += category.getName() + " ";
                                    }
                                
                                }
                                li.setCategory(catstr);
                                if (off.getImageUrls() != null) {
                                    if (off.getImageUrls().size() >= 1) {
                                        // tmp fix, save image from url, give the path to HomeFragment
                                        li.setResourceImage(off.getImageUrls().get(0));
                                    }
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
                        Log.d("GSW MainActivity", "e", e);
                    } catch (Exception e) {
                        Log.d("GSW MainActivity", "e", e);
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
                            ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showErrorText(getString(R.string.no_location));
                        }
                    });
                }
            }
        };
        new Thread(runnable).start();
    }
    public Location getLocation() {
        // Get the location manager
        LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(getActivity().getApplicationContext().LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        return location;
    }
}
