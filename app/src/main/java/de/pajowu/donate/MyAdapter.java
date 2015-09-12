package de.pajowu.donate;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ContactViewHolder> {

    private List<ContactInfo> contactList;

    public MyAdapter(List<ContactInfo> contactList) {
        this.contactList = contactList;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        ContactInfo ci = contactList.get(i);
        contactViewHolder.vCategory.setText(ci.category);
        contactViewHolder.vImage.setImageResource(ci.image);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.faq_layout, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView vCategory;
        protected ImageView vImage;


        public ContactViewHolder(View v) {
            super(v);
            vImage = (ImageView) v.findViewById(R.id.headImage);
            vCategory = (TextView) v.findViewById(R.id.txtName);
        }
    }
}
