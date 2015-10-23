package com.github.socialc0de.gsw.android.models;


import android.support.v4.app.Fragment;
import com.github.socialc0de.gsw.android.fragments.ListTabFragment;
import com.github.socialc0de.gsw.android.list.items.ListItem;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListTabContainer {
    public ArrayList<ListItem> arrayList;
    public String title;
    Boolean offer;
    Boolean mentoring;
    Boolean internship;
    ListTabFragment frag;

    public ListTabContainer(ArrayList<ListItem> arli, String ttl) {
        arrayList = arli;
        title = ttl;
        frag = new ListTabFragment(arrayList, title);
    }

    public ListTabContainer(ArrayList<ListItem> arli, String ttl, Boolean off, Boolean ment, Boolean intern) {
        arrayList = arli;
        title = ttl;
        offer = off;
        mentoring = ment;
        internship = intern;
        frag = new ListTabFragment(arrayList, title, offer, mentoring, internship);
    }

    public ListTabFragment getFragment() {
        return frag;
    }

    public String getTitle() {
        return title;
    }
}
