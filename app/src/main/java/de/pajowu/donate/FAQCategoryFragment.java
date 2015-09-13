package de.pajowu.donate;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.appspot.donate_backend.donate.*;
import com.appspot.donate_backend.donate.Donate.*;
import com.appspot.donate_backend.donate.model.*;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.json.JsonFactory;
import com.github.androidprogresslayout.ProgressLayout;
/**
 * A simple {@link Fragment} subclass.
 */
public class FAQCategoryFragment extends Fragment {
    private ArrayList<String> parentItems = new ArrayList<String>();
    private ArrayList<Object> childItems = new ArrayList<Object>();
    private View viewRoot;
    private String cat_id;
    FAQItemCollection result;
    public FAQCategoryFragment(String cid) {
        // Required empty public constructor
        cat_id = cid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot = inflater.inflate(R.layout.fragment_faqcategory, container, false);
        loadFragmentData();
        return viewRoot;
    }
    public void fillLayout() {

        if (result.getItems() != null) {
            final ExpandableListView expandableList = (ExpandableListView) viewRoot.findViewById(R.id.list); // you can use (ExpandableListView) findViewById(R.id.list)
            expandableList.setDividerHeight(2);
            expandableList.setGroupIndicator(null);
            expandableList.setClickable(true);
            FAQCategoryAdapter adapter = new FAQCategoryAdapter(new ArrayList<FAQItem>(result.getItems()), getActivity().getApplicationContext());
            expandableList.setAdapter(adapter);
        } else {
            ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showErrorText("No Questions found");
        }
        
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

                
                try {
                    result = service.faqitem().bycat().setCategory(cat_id).execute();
                    /*offerList = new ArrayList<FAQItem>();
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
                    }*/
                    Log.d("MainActivity",result.toString());

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
                        ((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showContent();
                        fillLayout();
                        
                    }
                });
            }
        };
        new Thread(runnable).start();
    }
    public void setGroupParents() {
        parentItems.add("Where may I get food for little money?");
        parentItems.add("Does german food contain pig flesh?");
        parentItems.add("Is german food expensive?");
        parentItems.add("Where can I find typically Syrian Restaurants?");
    }
/*
    public void setChildData() {

        // Android
        ArrayList<String> child = new ArrayList<String>();
        child.add("Kindertafel");
        childItems.add(child);

        // Core Java
        child = new ArrayList<String>();
        child.add("Yes");
        childItems.add(child);

        // Desktop Java
        child = new ArrayList<String>();
        child.add("Unfortunately, yes.");

        childItems.add(child);

        // Enterprise Java
        child = new ArrayList<String>();
        child.add("EJB3");

        childItems.add(child);
    }
*/

}
