package de.pajowu.donate;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.appspot.donate_backend.donate.*;
import com.appspot.donate_backend.donate.Donate.Builder;
import com.appspot.donate_backend.donate.model.*;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.github.androidprogresslayout.ProgressLayout;

import java.util.ArrayList;

import com.melnykov.fab.FloatingActionButton;

public class HomeFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    public ScrollView scrollView;
    public ExpandableGridView gridView;
    private View viewRoot;
    //TODO Get Labels, Sub_labels, categories, images, objects
    Context mContext;
    public ArrayList<ListItem> arrayList;

    public HomeFragment(Context context, ArrayList<ListItem> arrayList) {
        this.mContext = context;
        this.arrayList = arrayList;

    }

    public HomeFragment(Context context) {
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewRoot = inflater.inflate(R.layout.fragment_home, container, false);
        FloatingActionButton editButton = (FloatingActionButton) viewRoot.findViewById(R.id.fab);
        editButton.setOnClickListener(this);
        if (arrayList != null) {
            fillLayout();
        } else {
            loadFragmentData();
        }

        return viewRoot;

    }

    private void fillLayout() {
        if (arrayList != null) {
            gridView = (ExpandableGridView) viewRoot.findViewById(R.id.gridView1);
            gridView.setAdapter(new ImageAdapter(getActivity(), arrayList));

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //TODO Call Activity with Parameters -> PrimaryKey
                    MainActivity mac = (MainActivity) getActivity();
                    Log.d("position was clicked:", position + "");
                    Log.d("Equivalent ID is", arrayList.get(position) + "");

                }
            });

            scrollView = (ScrollView) viewRoot.findViewById(R.id.scrollView);
            gridView.setExpanded(true);

            scrollView.smoothScrollTo(0, 0);
        }
        
    }

    public void loadFragmentData() {
        /*((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showProgress();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Builder endpointBuilder = new Donate.Builder(
                        AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(),
                        CloudEndpointBuilderHelper.getRequestInitializer());

                Donate service = CloudEndpointBuilderHelper.updateBuilder(endpointBuilder).build();

                OfferProtoIdTitleSubtitleImageUrlsCategoriesCollection result;
                try {
                    result = service.offer().top().execute();
                    Log.d("MainActivity", result.toString());
                    // Do NOT use the same variable name for different things, just as dummy and database content
                    arrayList = new ArrayList<ListItem>();


                    for (OfferProtoIdTitleSubtitleImageUrlsCategories off : result.getItems()) {
                        ListItem li = new ListItem("", off.getTitle(), off.getSubtitle(), "", off.getId());
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
                        arrayList.add(li);
                    }
                    Log.d("MainActivity", arrayList.toString());
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
        new Thread(runnable).start();*/
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Log.d("MainActivity", "pressed");
                //((MaterialNavigationDrawer) getActivity()).setFragment(new NewOfferFragment(mContext),"New Offer");
                //((ProgressLayout) viewRoot.findViewById(R.id.progress_layout)).showErrorText("New Offer is not implemented yet");
                //TODO Set Editable = true (search fitting code for it
                //TODO Maybe add lines again to make obvious, that they can be edited
                break;
        }
    }
}

