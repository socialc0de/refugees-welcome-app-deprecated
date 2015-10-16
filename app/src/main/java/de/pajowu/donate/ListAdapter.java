package de.pajowu.donate;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import android.support.v4.app.FragmentActivity;

import de.pajowu.donate.models.ListItem;

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
        viewHolder.title.setText(currentData.getTitle());
        viewHolder.subtitle.setText(currentData.getSubtitle());
        viewHolder.category.setText(currentData.getCategory());
        if (currentData.getResourceImage() != null && currentData.getResourceImage() != ""){
            Log.d("GSW MainActivity",currentData.toString());
            Picasso.with(this.context).load(currentData.getResourceImage()).into(viewHolder.imgV);
        } else if (currentData.getImage() != 0){
            Picasso.with(this.context).load(currentData.getImage()).into(viewHolder.imgV);
        }
        Log.d("GSW MainActivity","onBindViewHolder");

    }

// Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (arrayList != null) {
            return arrayList.size();
        } else {
            return 0;
        }
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
            Log.d("GSW MainActivity","position = " + getPosition());
            ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProductFragment(context, arrayList.get(getPosition()).getPrimaryKey())).addToBackStack(null).commit();
            ((MainActivity)context).mDrawer.setSelection(-1, false);
        }
    }

}