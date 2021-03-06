package com.github.socialc0de.gsw.android.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import com.github.socialc0de.gsw.android.MainActivity;
import com.github.socialc0de.gsw.android.R;
import com.github.socialc0de.gsw.android.list.Adapter.PhraseListAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhraseListTabFragment extends Fragment {
    public ArrayList<HashMap<String, String>> arrayList;
    public long primaryKeyValue;
    public String title;

    public PhraseListTabFragment(ArrayList<HashMap<String, String>> arli, String ttl) {
        this.arrayList = arli;
        Log.d("GSW MainActivity", arrayList.toString());
        title = ttl;
    }

    public static void getTotalHeightofListView(ListView listView, PhraseListAdapter listAdapter) {

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View mView = listAdapter.getView(i, null, listView);

            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),

                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            totalHeight += mView.getMeasuredHeight();
            Log.w("HEIGHT" + i, String.valueOf(totalHeight));

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_list_tab, container, false);

        ListView listView = (ListView) viewRoot.findViewById(R.id.listView);
        View empty = viewRoot.findViewById(R.id.empty);
        listView.setEmptyView(empty);
        ScrollView scrollView = (ScrollView) viewRoot.findViewById(R.id.fragment_list_scrollView);
        Log.d("GSW MainActivity", "onCreateView");
        Log.d("GSW MainActivity", this.arrayList.toString());
        Log.d("GSW MainActivity", arrayList.toString());
        PhraseListAdapter listAdapter = new PhraseListAdapter(getActivity(), R.layout.list_layout, this.arrayList);

        Log.d("R.layout.list_layout = ", "" + R.layout.list_layout);
        Log.d("R.layout.list_layout = ", "" + R.layout.list_layout);

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("ListView clicked: ", "" + position);
                Log.d("GSW MainActivity", arrayList.get(position).toString());
                //((MaterialNavigationDrawer) getActivity()).setFragmentChild(new PhraseDetailFragment(arrayList.get(position)),"Phrase");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new PhraseDetailFragment(arrayList.get(position))).addToBackStack(null).commit();
                MainActivity.getMainActivity().getmDrawer().setSelection(-1, false);

            }
        });

        getTotalHeightofListView(listView, listAdapter);

        // Make List scroll by default to top
        scrollView.smoothScrollTo(0, 0);


        return viewRoot;
    }

    public String getTitle() {
        return title;
    }
}
