package de.pajowu.donate;


import java.util.ArrayList;

 
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 
public class ContactViewAdapter extends RecyclerView.Adapter<ContactRowHolder> {
    Context context;
    ArrayList<ContactRow> itemsList;
 
    public ContactViewAdapter(Context context, ArrayList<ContactRow> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }
 
    @Override
    public int getItemCount() {
        if (itemsList == null) {
            return 0;
        } else {
            return itemsList.size();
        }
    }
 
    @Override
    public ContactRowHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.contact_row, null);
        ContactRowHolder viewHolder = new ContactRowHolder(view);
        return viewHolder;
    }
 
    @Override
    public void onBindViewHolder(ContactRowHolder ContactRowHolder, int position) {
        ContactRow items = itemsList.get(position);
        ContactRowHolder.textView.setText(String.valueOf(items.getTitle()));
        ContactRowHolder.imageView.setBackgroundResource(items.getImgIcon());
    }
}