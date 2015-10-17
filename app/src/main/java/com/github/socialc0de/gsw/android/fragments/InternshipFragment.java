package com.github.socialc0de.gsw.android.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.github.socialc0de.gsw.android.R;
import com.github.socialc0de.gsw.android.list.items.ListItem;
import com.github.socialc0de.gsw.android.list.adapter.ListAdapter;


public class InternshipFragment extends Fragment {

    public ArrayList<ListItem> arrayList;
    public long primaryKeyValue;
    public String title;

    public InternshipFragment(ArrayList<ListItem> arli, String ttl) {
        arrayList = arli;
        title = ttl;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("GSW MainActivity", "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("GSW MainActivity", "onResume");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("GSW MainActivity", "onCreateView ListTabFragment");
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_faqfragment, container, false);

        RecyclerView listView = (RecyclerView) viewRoot.findViewById(R.id.cardList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);

        ListAdapter listAdapter = new ListAdapter(getActivity(), R.layout.list_layout, this.arrayList);

        Log.d("R.layout.list_layout = ", "" + R.layout.list_layout);
        Log.d("R.layout.list_layout = ", "" + R.layout.list_layout);

        listView.setAdapter(listAdapter);
        listView.setHasFixedSize(true);

        return viewRoot;
    }

    public String getTitle() {
        return title;
    }

}
