package de.pajowu.donate.list.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.pajowu.donate.R;
import de.pajowu.donate.models.CategoryListItem;

public class CategoryListAdapter extends ArrayAdapter<CategoryListItem> {
    private int targetedLayout;
    private Context context;
    private ArrayList<CategoryListItem> arrayList;
    private Typeface typeface1;

    public CategoryListAdapter(Context context, int resource, ArrayList<CategoryListItem> arrayList) {
        super(context, resource, arrayList);
        this.context = context;
        this.targetedLayout = resource;
        this.arrayList = arrayList;
        // Define Font for CategoryListItem titles
        typeface1 = Typeface.createFromAsset(context.getAssets(), "ralewaybold.ttf");

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            // get layout from mobile.xml
            v = inflater.inflate(R.layout.category_list_layout, null);
            Log.d("getView called, ", "convertView == null");
            //Log.e("Convertview == ", "null");
        } else {
            v = convertView;
            //Log.e("Convertview =/= ", "null");
            Log.d("getView called, ", "View = convertView");

        }

        TextView textView = (TextView) v.findViewById(R.id.listLayoutTextView1);
        CategoryListItem currentData = arrayList.get(position);
        textView.setText(currentData.getName());
        textView.setTypeface(typeface1);
        return v;
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }
}
