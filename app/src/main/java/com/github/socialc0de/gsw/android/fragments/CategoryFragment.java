package com.github.socialc0de.gsw.android.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.appspot.donate_backend.donate.model.Category;

import java.util.ArrayList;
import java.util.HashMap;

import com.github.socialc0de.gsw.android.MainActivity;
import com.github.socialc0de.gsw.android.R;
import com.github.socialc0de.gsw.android.SlidingTabLayout;
import com.github.socialc0de.gsw.android.adapter.pager.CategoryViewPagerAdapter;
import com.github.socialc0de.gsw.android.list.adapter.CategoryListAdapter;
import com.github.socialc0de.gsw.android.list.items.CategoryListItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    public HashMap<String, Category> categories;
    public long primaryKeyValue;
    public String title;
    ArrayList<CategoryListItem> itemList;
    ArrayList<CategoryListItem> serviceList;
    MainActivity mMainActivity;

    public CategoryFragment(MainActivity mac) {
        mMainActivity = mac;

    }

    public static void getTotalHeightofListView(ListView listView, CategoryListAdapter listAdapter) {

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
        categories = mMainActivity.categories;
        Log.d("GSW MainActivity", categories.toString());
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_list_nofab, container, false);
        itemList = new ArrayList<CategoryListItem>();
        serviceList = new ArrayList<CategoryListItem>();
        for (HashMap.Entry<String, Category> cat : categories.entrySet()) {
            Log.d("GSW MainActivity", cat.getValue().getGroup());
            if (cat.getValue().getGroup().equals("service")) {
                serviceList.add(new CategoryListItem("", cat.getValue().getName(), cat.getValue().getDescription(), cat.getValue().getId()));
            } else if (cat.getValue().getGroup().equals("item")) {
                itemList.add(new CategoryListItem("", cat.getValue().getName(), cat.getValue().getDescription(), cat.getValue().getId()));
            }
        }
        Log.d("GSW MainActivity", itemList.toString());
        ArrayList<CategoryListTabFragment> tbs = new ArrayList<CategoryListTabFragment>();
        tbs.add(new CategoryListTabFragment(serviceList, getString(R.string.service)));
        tbs.add(new CategoryListTabFragment(itemList, getString(R.string.item)));
        CategoryViewPagerAdapter adapter = new CategoryViewPagerAdapter(getChildFragmentManager(), tbs);

        // Assigning ViewPager View and setting the adapter
        ViewPager pager = (ViewPager) viewRoot.findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        SlidingTabLayout tabs = (SlidingTabLayout) viewRoot.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.accentColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        return viewRoot;
    }
}
