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

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

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
import java.util.Arrays;

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
        viewRoot = inflater.inflate(R.layout.fragment_list, container, false);

        //Implementation of custom Toolbar
        SpannableString s = new SpannableString(getString(R.string.app_name));
        s.setSpan(new de.pajowu.donate.TypefaceSpan(context, "fabiolo.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((MaterialNavigationDrawer) this.getActivity()).getToolbar().setTitle(s);
        
        /*mTabHost = (FragmentTabHost) viewRoot.findViewById(R.id.tabhost);
        Log.d("TabHost ", "" + mTabHost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.fragment_list_mainContent);
        bundleTab1.putSerializable("lstObject", (Serializable) new ArrayList<ListItem>());
        mTabHost.addTab(mTabHost.newTabSpec("fragmentb").setIndicator("Offers"),
                ListFragmentTab.class, bundleTab1);*/

        loadFragmentData();
        return viewRoot;
    }
    public void fillLayout() {
        ArrayList<ListTabFragment> tbs = new ArrayList<ListTabFragment>();
        tbs.add(new ListTabFragment(this.offerList, "Offer"));
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
        /*mTabHost.getTabWidget().removeAllViews();
        bundleTab1 = new Bundle();
        bundleTab1.putSerializable("lstObject", (Serializable) this.offerList);
        bundleTab2.putSerializable("lstObject", (Serializable) this.searchList);
        bundleTab3.putSerializable("lstObject", (Serializable) this.allList);*/
        
        /*mTabHost = (FragmentTabHost) viewRoot.findViewById(R.id.tabhost);
        Log.d("TabHost ", "" + mTabHost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.fragment_list_mainContent);*/

        /*mTabHost.addTab(mTabHost.newTabSpec("fragmentb").setIndicator("Offers"),
                ListFragmentTab.class, bundleTab1);
        mTabHost.addTab(mTabHost.newTabSpec("fragmentc").setIndicator("Searches"),
                ListFragmentTab.class, bundleTab2);
        mTabHost.addTab(mTabHost.newTabSpec("fragmentd").setIndicator("All"),
                ListFragmentTab.class, bundleTab3);*/
    }
    public void loadFragmentData() {
        ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showProgress();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Builder endpointBuilder = new Donate.Builder(
                        AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(),
                        CloudEndpointBuilderHelper.getRequestInitializer());

                Donate service = CloudEndpointBuilderHelper.updateBuilder(endpointBuilder).build();

                OfferProtoIdTitleSubtitleImageUrlsCategoriesCollection result;
                try {
                    result = service.offer().bycat().setCategories(Arrays.asList(cat_id)).execute();
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
                                if (off.getImageUrls().size() >= 1) {
                                    //IMAGES.add(off.getImageUrls().get(0));
                                    // tmp fix, save image from url, give the path to HomeFragment
                                    li.resourceImage = off.getImageUrls().get(0);
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
                    Log.d("MainActivity", "e", e);
                } catch (Exception e) {
                    Log.d("MainActivity", "e", e);
                }

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        fillLayout();
                        ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showContent();
                    }
                });
            }
        };
        new Thread(runnable).start();
    }
}
