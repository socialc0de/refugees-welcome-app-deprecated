package de.pajowu.donate;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
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
public class FAQFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    View viewRoot;
    Map<String, Integer> num;
    HashMap<String,FAQCategory> cats;

    public FAQFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot = inflater.inflate(R.layout.fragment_faqfragment, container, false);
        if (cats == null) {
            loadFragmentData();
        } else {
            fillLayout();
        }
        return viewRoot;
    }

    public void fillLayout() {
        final ArrayList<CategoryCardItem> list = new ArrayList<CategoryCardItem>();
        final RecyclerView recList = (RecyclerView) viewRoot.findViewById(R.id.cardList);
        for (HashMap.Entry<String,FAQCategory> entry : cats.entrySet()) {
            list.add(new CategoryCardItem(entry.getValue().getName(), entry.getValue().getImage(), entry.getValue().getId()));
        }
        recList.addOnItemTouchListener(
                new RecyclerItemClickListener(viewRoot.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d("pos = ", position + "");
                        FAQCategoryFragment faqCategoryFragment = new FAQCategoryFragment(list.get(position).id);
                        ((MaterialNavigationDrawer) getActivity()).setFragmentChild(faqCategoryFragment,"Answers");
                    }
                })
        );

        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(viewRoot.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        Log.d("CategoryItems1: ", list + "");
        RecyclerViewAdapter ca = new RecyclerViewAdapter(list);
        recList.setAdapter(ca);

    }
    public void loadFragmentData() {
        Log.d("MainActivity","loadFragmentData");
        ((ProgressLayout) viewRoot.findViewById(R.id.faqfragment_progress_layout)).showProgress();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("MainActivity","try");
                    Builder endpointBuilder = new Donate.Builder(
                        AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(),
                        CloudEndpointBuilderHelper.getRequestInitializer());

                    Donate service = CloudEndpointBuilderHelper.updateBuilder(endpointBuilder).build();
                    FAQCategoryCollection result;
                    result = service.faqcat().list().execute();
                    cats = new HashMap<String,FAQCategory>();
                    Log.d("MainActivity res", result.toString());
                    for (FAQCategory cat : result.getItems()) {
                        cats.put(cat.getId(), cat);
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
                        ((ProgressLayout) viewRoot.findViewById(R.id.faqfragment_progress_layout)).showContent();
                    }
                });
            }
        };
        new Thread(runnable).start();
    }

}

