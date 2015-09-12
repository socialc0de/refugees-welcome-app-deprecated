package de.pajowu.donate;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import android.util.Log;
/**
 * Created by hp1 on 21-01-2015.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
 
    ArrayList<ListTabFragment> fragments; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    //int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
 
 
    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm, ArrayList<ListTabFragment> frag) {
        super(fm);
        this.fragments = frag;
        Log.d("MainActivity","new ViewPagerAdapter");
 
    }
 
    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        Log.d("MainActivity", fragments.get(position).toString());
        return fragments.get(position);
 
 
    }
 
    // This method return the titles for the Tabs in the Tab Strip
 
    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getTitle();
    }
 
    // This method return the Number of tabs for the tabs Strip
 
    @Override
    public int getCount() {
        return fragments.size();
    }
}