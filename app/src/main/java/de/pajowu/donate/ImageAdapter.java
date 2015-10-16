package de.pajowu.donate;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.pajowu.donate.models.ListItem;

public class ImageAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private ArrayList<ListItem> arrayList;
    private Typeface headline;
    private int currentPosition;


    public ImageAdapter(Context context, ArrayList<ListItem> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        headline = Typeface.createFromAsset(context.getAssets(), "ralewaybold.ttf");

        Log.d("ImageAdapter was called", "");
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        currentPosition = position;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {
            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.item, null);
            //Log.e("Convertview == ", "null");
        } else {
            gridView = convertView;
            //Log.e("Convertview =/= ", "null");
        }
        CardView cardView = (CardView) gridView.findViewById(R.id.cardView);
        cardView.setPreventCornerOverlap(false);
        //cardView.setOnClickListener(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, gridView.getResources().getDisplayMetrics());
            cardView.setRadius(radius);
            Log.d("Radius set :", radius + "dp");
        }

        //Handle Image accoording to Screen Size
        float ht_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, gridView.getResources().getDisplayMetrics());
        float wt_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, gridView.getResources().getDisplayMetrics());

        ImageView imgV = (ImageView) gridView.findViewById(R.id.cardViewImage);
        if (this.arrayList.get(position).getResourceImage() != "") {
            Picasso.with(this.context).load(this.arrayList.get(position).getResourceImage()).fit().into(imgV);

        }
        Log.d("resourceImageID:", this.arrayList.get(position).getImage() + "");

        TextView textView = (TextView) gridView.findViewById(R.id.cardViewText);
        textView.setText(this.arrayList.get(position).getTitle());
        textView.setTypeface(headline);

        TextView textView2 = (TextView) gridView.findViewById(R.id.cardViewText2);
        textView2.setText(this.arrayList.get(position).getSubtitle());

        TextView textView3 = (TextView) gridView.findViewById(R.id.cardViewCategory);
        textView3.setText(this.arrayList.get(position).getCategory());

        return gridView;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void onClick(View v) {

    }
}
