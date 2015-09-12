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

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<ListItem> {
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

        /*
        Bitmap originalImg = BitmapFactory.decodeResource(v.getResources(), currentData.resourceImage);
        Bitmap resizedImage = Bitmap.createScaledBitmap(originalImg, Math.round(originalImg.getWidth() / (originalImg.getWidth() / wt_px)), Math.round(originalImg.getHeight() / (originalImg.getHeight() / ht_px)), true);

        Log.d(" Bitmap resImgWidth = ",""+resizedImage.getWidth());
        Drawable d = new BitmapDrawable(v.getResources(), resizedImage);
        */

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
}
