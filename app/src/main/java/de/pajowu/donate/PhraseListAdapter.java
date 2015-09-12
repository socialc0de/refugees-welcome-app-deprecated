package de.pajowu.donate;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.Arrays;
import java.util.List;

import java.util.ArrayList;
import java.util.HashMap;
import android.widget.LinearLayout;
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
        LinearLayout ll = (LinearLayout)v.findViewById(R.id.text);
        HashMap<String, String> currentData = arrayList.get(position);
        List<String> langs = Arrays.asList("German", "English","Arabic / Syrian");
        for (String lang: langs) {
            TextView tv = new TextView(context);
            tv.setText(currentData.get(lang));
            tv.setTypeface(typeface1);
            ll.addView(tv);
        }
        return v;
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }
}
