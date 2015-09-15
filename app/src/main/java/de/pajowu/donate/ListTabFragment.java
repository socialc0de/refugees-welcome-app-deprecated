package de.pajowu.donate;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.ArrayList;
/**
 * A simple {@link Fragment} subclass.
 */
public class ListTabFragment extends Fragment{
    public ArrayList<ListItem> arrayList;
    public long primaryKeyValue;
    public String title;
    public ListTabFragment(ArrayList<ListItem> arli, String ttl) {
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
        Log.d("MainActivity","onPause");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d("MainActivity","onResume");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("MainActivity","onCreateView ListTabFragment");
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_list_tab, container, false);

        ListView listView = (ListView) viewRoot.findViewById(R.id.listView);
        View empty = viewRoot.findViewById(R.id.empty);
        listView.setEmptyView(empty);
        ScrollView scrollView = (ScrollView) viewRoot.findViewById(R.id.fragment_list_scrollView);

        ListAdapter listAdapter = new ListAdapter(getActivity(), R.layout.list_layout, this.arrayList);

        Log.d("R.layout.list_layout = ", "" + R.layout.list_layout);
        Log.d("R.layout.list_layout = ", ""+R.layout.list_layout);

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //primaryKeyValue = arrayList.get(position).primaryKey;
                
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProductFragment(getActivity().getApplicationContext(), arrayList.get(position).primaryKey)).addToBackStack(null).commit();
                ((MainActivity)getActivity()).mDrawer.setSelection(-1, false);
                Log.d("ListView clicked: ", ""+position);
                Log.d("ListView Pos: ", ""+primaryKeyValue);
                
            }
        });

        getTotalHeightofListView(listView, listAdapter);

        // Make List scroll by default to top
        scrollView.smoothScrollTo(0, 0);


        return viewRoot;
    }

    public static void getTotalHeightofListView(ListView listView, ListAdapter listAdapter) {

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
    public String getTitle() {
        return title;
    }
}
