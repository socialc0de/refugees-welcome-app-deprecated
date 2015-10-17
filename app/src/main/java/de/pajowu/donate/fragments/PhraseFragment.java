package de.pajowu.donate.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.pajowu.donate.CategoryListAdapter;
import de.pajowu.donate.PhraseViewPagerAdapter;
import de.pajowu.donate.R;
import de.pajowu.donate.SlidingTabLayout;
import de.pajowu.donate.fragments.PhraseListTabFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhraseFragment extends Fragment {
    public PhraseFragment() {

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

    public ArrayList<HashMap<String, String>> loadPhrasesFromAsset(String filename) {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            JSONArray m_jArry = new JSONArray(json);
            ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                HashMap<String, String> phrase = new HashMap<String, String>();
                Iterator<?> keys = jo_inside.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    if (jo_inside.get(key) instanceof String) {
                        phrase.put(key, (String) jo_inside.get(key));
                    }
                }
                Log.d("GSW MainActivity", jo_inside.toString());
                formList.add(phrase);
            }
            return formList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_phrase_tab, container, false);
        ArrayList<PhraseListTabFragment> tbs = new ArrayList<PhraseListTabFragment>();
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("basic.json"), getString(R.string.basic_conversation)));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("medical.json"), getString(R.string.medical_terms)));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("food.json"), getString(R.string.food)));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("clothing.json"), getString(R.string.clothing)));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("children.json"), getString(R.string.children)));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("profession.json"), getString(R.string.profession)));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("animals.json"), getString(R.string.animals)));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("children.json"), getString(R.string.children)));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("nature.json"), getString(R.string.nature_weather)));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("basic_adjectives.json"), getString(R.string.basic_adjectives)));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("colours.json"), getString(R.string.colours)));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("direction.json"), getString(R.string.direction_places_transport)));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("sexual.json"), getString(R.string.sexual_gender_identity)));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("misc.json"), getString(R.string.misc)));
        PhraseViewPagerAdapter adapter = new PhraseViewPagerAdapter(getChildFragmentManager(), tbs);

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
