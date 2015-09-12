package de.pajowu.donate;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FAQCategoryFragment extends Fragment {
    private ArrayList<String> parentItems = new ArrayList<String>();
    private ArrayList<Object> childItems = new ArrayList<Object>();


    public FAQCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_faqcategory, container, false);
        ExpandableListView expandableList = (ExpandableListView) v.findViewById(R.id.list); // you can use (ExpandableListView) findViewById(R.id.list)

        expandableList.setDividerHeight(2);
        expandableList.setGroupIndicator(null);
        expandableList.setClickable(true);

        setGroupParents();
        setChildData();

        MyExpandableAdapter adapter = new MyExpandableAdapter(parentItems, childItems);

        adapter.setInflater((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), getActivity());
        expandableList.setAdapter(adapter);

        return v;
    }

    public void setGroupParents() {
        parentItems.add("Where may I get food for little money?");
        parentItems.add("Does german food contain pig flesh?");
        parentItems.add("Is german food expensive?");
        parentItems.add("Where can I find typically Syrian Restaurants?");
    }

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


}
