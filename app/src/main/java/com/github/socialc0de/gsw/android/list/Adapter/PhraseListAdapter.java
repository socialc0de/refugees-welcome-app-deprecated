package com.github.socialc0de.gsw.android.list.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.github.socialc0de.gsw.android.R;

public class PhraseListAdapter extends ArrayAdapter<HashMap<String, String>> {
    private int targetedLayout;
    private Context context;
    private ArrayList<HashMap<String, String>> arrayList;
    private Typeface typeface1;

    public PhraseListAdapter(Context context, int resource, ArrayList<HashMap<String, String>> arrayList) {
        super(context, resource, arrayList);
        this.context = context;
        this.targetedLayout = resource;
        this.arrayList = arrayList;

        // Define Font for PhraseListItem titles
        typeface1 = Typeface.createFromAsset(context.getAssets(), "ralewaybold.ttf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            // get layout from mobile.xml
            v = inflater.inflate(R.layout.phrase_row, null);
            Log.d("getView called, ", "convertView == null");
            //Log.e("Convertview == ", "null");
        } else {
            v = convertView;
            //Log.e("Convertview =/= ", "null");
            Log.d("getView called, ", "View = convertView");

        }
        LinearLayout ll = (LinearLayout) v.findViewById(R.id.text);
        HashMap<String, String> currentData = arrayList.get(position);
        HashMap<Integer, String> fields = new HashMap<Integer, String>();
        fields.put(R.id.german_phrase, "German");
        fields.put(R.id.english_phrase, "English");
        fields.put(R.id.arabic_phrase, "Arabic / Syrian");
        for (HashMap.Entry<Integer, String> entry: fields.entrySet()) {
            TextView tv = (TextView) v.findViewById(entry.getKey().intValue());
            tv.setText(currentData.get(entry.getValue()));
            tv.setTypeface(typeface1);
        }
        return v;
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }
}
