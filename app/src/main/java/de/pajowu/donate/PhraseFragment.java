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
import java.util.HashMap;
import java.util.ArrayList;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

import com.appspot.donate_backend.donate.*;
import com.appspot.donate_backend.donate.Donate.*;
import com.appspot.donate_backend.donate.model.*;
import android.support.v4.view.ViewPager;
import android.app.Activity;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.InputStream;

import android.text.Spannable;
import android.text.SpannableString;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhraseFragment extends Fragment{
    public PhraseFragment() {
        /*HashMap<String, String> phrase = new HashMap<String, String>();
        phrase.put("German","Hallo");
        phrase.put("English","Hello");
        phrase.put("Arabic","مرحبا اهلا");
        phrase.put("Arabic Phonetic","Saalam/ Merhaba");
        phrase.put("French","Salut");
        phrases.add(phrase);
        phrase = new HashMap<String, String>();
        phrase.put("German","guten Morgen");
        phrase.put("English","Good morning");
        phrase.put("Arabic"," صباح الخیر");
        phrase.put("Arabic Phonetic","Sabáh al-khayr");
        phrase.put("French","Bonjour");
        phrases.add(phrase);
        phrase = new HashMap<String, String>();
        phrase.put("German","guten Abend");
        phrase.put("English","good evening");
        phrase.put("Arabic"," مسا الخير /  مساء الخیر");
        phrase.put("Arabic Phonetic","massa Alchayr     Masa al-khayr      ");
        phrase.put("French","Bonsoir");
        phrases.add(phrase);*/

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
public ArrayList<HashMap<String,String>> loadPhrasesFromAsset(String filename) {
    String json = null;
    try {
        InputStream is = getActivity().getAssets().open(filename);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        json = new String(buffer, "UTF-8");
        //JSONObject obj = new JSONObject(json);
        JSONArray m_jArry = new JSONArray(json);
        ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < m_jArry.length(); i++) {
            JSONObject jo_inside = m_jArry.getJSONObject(i);
            HashMap<String, String> phrase = new HashMap<String, String>();
            Iterator<?> keys = jo_inside.keys();
            while( keys.hasNext() ) {
                String key = (String)keys.next();
                if ( jo_inside.get(key) instanceof String ) {
                    phrase.put(key, (String)jo_inside.get(key));
                }
            }
            Log.d("MainActivity",jo_inside.toString());
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
        //categories = mMainActivity.categories;
        //Log.d("MainActivity",categories.toString());
        View viewRoot = inflater.inflate(R.layout.fragment_phrase_tab, container, false);
        /*SpannableString s = new SpannableString(getString(R.string.app_name));
        s.setSpan(new de.pajowu.donate.TypefaceSpan(getActivity().getApplicationContext(), "fabiolo.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((MaterialNavigationDrawer) this.getActivity()).getToolbar().setTitle(s);
        itemList = new ArrayList<CategoryListItem>();
        serviceList = new ArrayList<CategoryListItem>();
        for (HashMap.Entry<String,Category> cat: categories.entrySet()) {
            Log.d("MainActivity",cat.getValue().getGroup());
            if (cat.getValue().getGroup().equals("service")) {
                serviceList.add(new CategoryListItem("", cat.getValue().getName(), cat.getValue().getDescription(), cat.getValue().getId()));
            } else if (cat.getValue().getGroup().equals("item")) {
                itemList.add(new CategoryListItem("", cat.getValue().getName(), cat.getValue().getDescription(), cat.getValue().getId()));
            }
        }
        Log.d("MainActivity",itemList.toString());*/
        ArrayList<PhraseListTabFragment> tbs = new ArrayList<PhraseListTabFragment>();
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("basic.json"), "Basic Conversation"));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("medical.json"), "Medical Terms"));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("food.json"), "Food"));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("clothing.json"), "Clothing"));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("children.json"), "Children"));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("profession.json"), "Profession"));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("animals.json"), "Animals"));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("children.json"), "Children"));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("nature.json"), "Nature and Weather"));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("basic_adjectives.json"), "Basic Adjectives"));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("colours.json"), "Colours"));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("direction.json"), "Direction, places and transport"));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("sexual.json"), "Sexual and gender identity"));
        tbs.add(new PhraseListTabFragment(loadPhrasesFromAsset("misc.json"), "Misc"));
        //tbs.add(new CategoryListTabFragment(itemList, "Item"));
        PhraseViewPagerAdapter adapter =  new PhraseViewPagerAdapter(getChildFragmentManager(),tbs);
 
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
}
