package de.pajowu.donate.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.appspot.donate_backend.donate.Donate;
import com.appspot.donate_backend.donate.Donate.Builder;
import com.appspot.donate_backend.donate.model.Category;
import com.appspot.donate_backend.donate.model.OfferProtoIdTitleSubtitleImageUrlsCategoriesLatLon;
import com.appspot.donate_backend.donate.model.OfferProtoIdTitleSubtitleImageUrlsCategoriesLatLonCollection;
import com.github.androidprogresslayout.ProgressLayout;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import de.pajowu.donate.CloudEndpointBuilderHelper;
import de.pajowu.donate.MainActivity;
import de.pajowu.donate.R;
import de.pajowu.donate.SlidingTabLayout;
import de.pajowu.donate.ViewPagerAdapter;
import de.pajowu.donate.models.ListItem;

public class LocalFragment extends Fragment implements View.OnClickListener {
    public ArrayList<ListItem> offerList;
    public ListView listView;
    public Context context;
    public FragmentTabHost mTabHost;
    public Bundle bundleTab1 = new Bundle();
    View viewRoot;

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
        Log.d("GSW MainActivity", "onCreateView Local");
        //Implementation of custom Toolbar

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


            Log.d("GSW MainActivity", "fillLayout Locals");
            ArrayList<ListTabFragment> tbs = new ArrayList<ListTabFragment>();
            tbs.add(new ListTabFragment(this.offerList, getString(R.string.offer)));
            Log.d("GSW MainActivity", "create ViewPagerAdapter");
            ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), tbs);
            Log.d("GSW MainActivity", "created ViewPagerAdapter");
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
        loadData(getLocation());
    }

    public void loadData(final Location loc) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Builder endpointBuilder = new Donate.Builder(
                        AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(),
                        CloudEndpointBuilderHelper.getRequestInitializer());

                Donate service = CloudEndpointBuilderHelper.updateBuilder(endpointBuilder).build();

                OfferProtoIdTitleSubtitleImageUrlsCategoriesLatLonCollection result;
                if (loc != null) {


                    double r = 6371;  // earth radius in km

                    double radius = 10; // km

                    String bbox = new Double(loc.getLongitude() - Math.toDegrees(radius / r / Math.cos(Math.toRadians(loc.getLatitude())))).toString() + ",";
                    bbox += new Double(loc.getLatitude() - Math.toDegrees(radius / r)).toString() + ",";
                    bbox += new Double(loc.getLongitude() + Math.toDegrees(radius / r / Math.cos(Math.toRadians(loc.getLatitude())))).toString() + ",";
                    bbox += new Double(loc.getLatitude() + Math.toDegrees(radius / r)).toString();

                    try {
                        result = service.offer().listNear().setBbox(bbox).execute();
                        Log.d("GSW MainActivity", result.toString());
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
                                    // tmp fix, save image from url, give the path to HomeFragment
                                    li.setResourceImage(off.getImageUrls().get(0));
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
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showErrorText(getString(R.string.couldnt_load_offers));
                            }
                        });
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
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        Double lat, lon;
        return location;
    }

    /*public Location getLocation() {
        // Get the location manager
        LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(getActivity().getApplicationContext().LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        return location;
    }*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Log.d("GSW MainActivity", "pressed");
                if (((MainActivity) getActivity()).credential.getSelectedAccountName() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new NewOfferFragment(context)).addToBackStack(null).commit();
                    ((MainActivity) getActivity()).mDrawer.setSelection(-1, false);
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle(getString(R.string.please_sign_in));
                    alert.setMessage(R.string.cant_create_offer_if_not_signed_in);
                    alert.setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            getActivity().startActivityForResult(((MainActivity) getActivity()).credential.newChooseAccountIntent(), MainActivity.REQUEST_ACCOUNT_PICKER);
                        }
                    });
                    alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
                    alert.show();
                }

                //TODO Set Editable = true (search fitting code for it
                //TODO Maybe add lines again to make obvious, that they can be edited
                break;
        }
    }
}
