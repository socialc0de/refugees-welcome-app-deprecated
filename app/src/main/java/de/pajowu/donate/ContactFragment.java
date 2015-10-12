package de.pajowu.donate;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.melnykov.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;

public class ContactFragment extends android.support.v4.app.Fragment {
    public FloatingActionButton editButton;
    private boolean editMode = false;
    private Context mContext;
    ArrayList<ContactRow> data_list;
    RecyclerView recyclerView;

    public ContactFragment(Context context, ArrayList<ContactRow> arli) {
        this.data_list = arli;
        this.mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("MainActivity","onCreateView");
        View viewRoot = inflater.inflate(R.layout.fragment_contact, container, false);
        recyclerView = (RecyclerView) viewRoot.findViewById(R.id.contact_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addOnItemTouchListener(
            new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                // do whatever
                //int itemPosition = recyclerView.getChildPosition(view);
                ContactRow item = data_list.get(position);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(item.getUrl()));
                startActivity(i);
            }
            })
        );
        ContactViewAdapter adapter = new ContactViewAdapter(mContext, getData());
        recyclerView.setAdapter(adapter);

        // TODO set Content to each individual Textview and ImageView in xml File

        return viewRoot;
    }
    public ArrayList<ContactRow> getData()
    {
        return data_list;
    }
}
