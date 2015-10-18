package com.github.socialc0de.gsw.android.models;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.melnykov.fab.FloatingActionButton;

import com.github.socialc0de.gsw.android.fragments.*;
import com.github.socialc0de.gsw.android.*;
import com.github.socialc0de.gsw.android.list.adapter.ListAdapter;
import com.github.socialc0de.gsw.android.list.items.ListItem;

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
