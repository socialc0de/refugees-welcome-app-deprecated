package de.pajowu.donate;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by hp1 on 21-01-2015.
 */
public class CategoryViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<CategoryListTabFragment> fragments;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public CategoryViewPagerAdapter(FragmentManager fm, ArrayList<CategoryListTabFragment> frag) {
        super(fm);
        this.fragments = frag;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        Log.d("GSW MainActivity", "getItem");
        return fragments.get(position);
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        Log.d("GSW MainActivity", "getPageTitle");
        return fragments.get(position).getTitle();
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        Log.d("GSW MainActivity", "getCount");
        return fragments.size();
    }
}