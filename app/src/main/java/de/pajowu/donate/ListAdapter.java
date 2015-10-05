package de.pajowu.donate;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import android.support.v4.app.FragmentActivity;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private int targetedLayout;
    private static Context context;
    private static ArrayList<ListItem> arrayList;
    private Typeface typeface1;

// Provide a suitable constructor (depends on the kind of dataset)
    public ListAdapter(Context context, int resource, ArrayList<ListItem> arrayList) {
        this.context = context;
        this.targetedLayout = resource;
        this.arrayList = arrayList;
        // Define Font for ListItem titles
        typeface1 = Typeface.createFromAsset(context.getAssets(), "ralewaybold.ttf");
        Log.d("MainActivity",arrayList.toString());
    }

// Create new views (invoked by the layout manager)
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
        int viewType) {
// create a new view
        View itemLayoutView = LayoutInflater.from(context).inflate(
            R.layout.list_layout, null);

// create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

// Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

// - get data from your itemsData at this position
// - replace the contents of the view with that itemsData
        ListItem currentData = arrayList.get(position);
        viewHolder.title.setText(currentData.title);
        viewHolder.subtitle.setText(currentData.subtitle);
        viewHolder.category.setText(currentData.category);
        if (currentData.resourceImage != null && currentData.resourceImage != ""){
            Log.d("MainActivity",currentData.toString());
            Picasso.with(this.context).load(currentData.resourceImage).into(viewHolder.imgV);
        } else if (currentData.image != 0){
            Picasso.with(this.context).load(currentData.image).into(viewHolder.imgV);
        }
        Log.d("MainActivity","onBindViewHolder");

    }

// Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        Log.d("MainActivity",new Integer(arrayList.size()).toString());
        return arrayList.size();
    }

// inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public TextView subtitle;
        public TextView category;
        public ImageView imgV;
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            title = (TextView) itemLayoutView.findViewById(R.id.listLayoutTextView1);
            subtitle = (TextView) itemLayoutView.findViewById(R.id.listLayoutTextView2);
            category = (TextView) itemLayoutView.findViewById(R.id.listLayoutTextView3);
            imgV = (ImageView) itemLayoutView.findViewById(R.id.listLayoutImageView);
            itemLayoutView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            Log.d("MainActivity","position = " + getPosition());
            ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProductFragment(context, arrayList.get(getPosition()).primaryKey)).addToBackStack(null).commit();
            ((MainActivity)context).mDrawer.setSelection(-1, false);
        }
    }

}
/*package de.pajowu.donate;

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
import android.support.v7.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListItem> {
    private int targetedLayout;
    private Context context;
    private ArrayList<ListItem> arrayList;
    private Typeface typeface1;

    public ListAdapter(Context context, int resource, ArrayList<ListItem> arrayList) {
        super(context, resource, arrayList);
        this.context = context;
        this.targetedLayout = resource;
        this.arrayList = arrayList;
        // Define Font for ListItem titles
        typeface1 = Typeface.createFromAsset(context.getAssets(), "ralewaybold.ttf");

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            // get layout from mobile.xml
            v = inflater.inflate(R.layout.list_layout, null);
            Log.d("getView called, ", "convertView == null");
            //Log.e("Convertview == ", "null");
        } else {
            v = convertView;
            //Log.e("Convertview =/= ", "null");
            Log.d("getView called, ", "View = convertView");

        }

        // get DisplayMetrics to adjust ImageSize to display Screen
        float ht_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, v.getResources().getDisplayMetrics());
        //Log.d("List ht_px = ",""+ht_px);
        float wt_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, v.getResources().getDisplayMetrics());
        //Log.d("List wt_px = ",""+wt_px);

        ImageView imgV = (ImageView) v.findViewById(R.id.listLayoutImageView);
        TextView textView = (TextView) v.findViewById(R.id.listLayoutTextView1);
        TextView textView2 = (TextView) v.findViewById(R.id.listLayoutTextView2);
        TextView textView3 = (TextView) v.findViewById(R.id.listLayoutTextView3);

        ListItem currentData = arrayList.get(position);


        // Resize image to save storage on disk


        //Same as ImageAdapter, if there is no chance for some devs to reach ServerURL, there should be an option for NullPointers

        // resize image with Picasso and load it into ImageView
        if (currentData.resourceImage != null && currentData.resourceImage != ""){
            Log.d("MainActivity",currentData.toString());
            Picasso.with(this.context).load(currentData.resourceImage).into(imgV);
        } else if (currentData.image != 0){
            Picasso.with(this.context).load(currentData.image).into(imgV);
        }
        // set Texts to TextView
        textView.setText(currentData.title);
        textView.setTypeface(typeface1);
        textView2.setText(currentData.subtitle);
        textView3.setText(currentData.category);
        return v;
    }

    @Override
    public int getCount() {
        if (this.arrayList != null) {
            return this.arrayList.size();
        } else {
            return 0;
        }
        
    }
}*/