package com.github.socialc0de.gsw.android.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.socialc0de.gsw.android.R;
import com.github.socialc0de.gsw.android.adapter.view.ContactViewAdapter;
import com.github.socialc0de.gsw.android.listener.RecyclerItemClickListener;
import com.github.socialc0de.gsw.android.models.ContactRow;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

public class ContactFragment extends android.support.v4.app.Fragment {
    public FloatingActionButton editButton;
    ArrayList<ContactRow> data_list;
    RecyclerView recyclerView;
    private boolean editMode = false;
    private Context mContext;

    public ContactFragment(Context context, ArrayList<ContactRow> arli) {
        this.data_list = arli;
        this.mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("GSW MainActivity", "onCreateView");
        View viewRoot = inflater.inflate(R.layout.fragment_contact, container, false);
        recyclerView = (RecyclerView) viewRoot.findViewById(R.id.contact_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
                        //int itemPosition = recyclerView.getChildPosition(view);
                        ContactRow item = data_list.get(position);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(item.getUrl()));
                        startActivity(i);
                    }
                })
        );
        ContactViewAdapter adapter = new ContactViewAdapter(mContext, getData());
        recyclerView.setAdapter(adapter);

        // TODO set Content to each individual Textview and ImageView in xml File

        return viewRoot;
    }

    public ArrayList<ContactRow> getData() {
        return data_list;
    }
}
