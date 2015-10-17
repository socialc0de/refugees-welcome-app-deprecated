package de.pajowu.donate.models.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.pajowu.donate.R;

public class ContactRowHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private TextView textView;

    public ContactRowHolder(View view) {
        super(view);
        this.textView = (TextView) view.findViewById(R.id.title);
        this.imageView = (ImageView) view.findViewById(R.id.image);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}