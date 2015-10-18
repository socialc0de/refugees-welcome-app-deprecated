package com.github.socialc0de.gsw.android.fragments;


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

import com.github.socialc0de.gsw.android.R;
import com.github.socialc0de.gsw.android.MainActivity;
import com.github.socialc0de.gsw.android.list.adapter.ListAdapter;
import com.github.socialc0de.gsw.android.list.items.ListItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListTabFragment extends Fragment implements View.OnClickListener {
    public ArrayList<ListItem> arrayList;
    public long primaryKeyValue;
    public String title;
    Boolean offer;
    Boolean mentoring;
    Boolean internship;
    public ListTabFragment(ArrayList<ListItem> arli, String ttl) {
        arrayList = arli;
        title = ttl;
    }
    public ListTabFragment(ArrayList<ListItem> arli, String ttl, Boolean off, Boolean ment, Boolean intern) {
        arrayList = arli;
        title = ttl;
        offer = off;
        mentoring = ment;
        internship = intern;
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
        if (arrayList != null) {
            FloatingActionButton editButton = (FloatingActionButton) viewRoot.findViewById(R.id.fab);
            editButton.setOnClickListener(this);

            RecyclerView listView = (RecyclerView) viewRoot.findViewById(R.id.cardList);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            listView.setLayoutManager(llm);
            Log.d("MainActivity",arrayList.toString());
            ListAdapter listAdapter = new ListAdapter(getActivity(), R.layout.list_layout, arrayList);

            Log.d("R.layout.list_layout = ", "" + R.layout.list_layout);
            Log.d("R.layout.list_layout = ", "" + R.layout.list_layout);

            listView.setAdapter(listAdapter);
            listView.setHasFixedSize(true);
        } else {
            getActivity().finish();
        }
        return viewRoot;
    }

    public String getTitle() {
        return title;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Log.d("GSW MainActivity", "pressed");
                if (MainActivity.getCredential().getSelectedAccountName() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new NewOfferFragment(getActivity().getApplicationContext(), offer, mentoring, internship)).addToBackStack(null).commit();
                    MainActivity.getMainActivity().getmDrawer().setSelection(-1, false);
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle(getString(R.string.please_sign_in));
                    alert.setMessage(R.string.cant_create_offer_if_not_signed_in);
                    alert.setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            getActivity().startActivityForResult(MainActivity.getCredential().newChooseAccountIntent(), MainActivity.REQUEST_ACCOUNT_PICKER);
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
