package com.example.simplechat.message;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.simplechat.R;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    public MessageAdapter(Context context, int resource,
                          List<Message> messages) {
        super(context, resource, messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = ((Activity)getContext()).getLayoutInflater()
                    .inflate(R.layout.message_item, parent, false);
        }

        ImageView photoImageView =
                convertView.findViewById(R.id.photo_image_view);

        TextView userTextView =
                convertView.findViewById(R.id.user_text_view);

        TextView messageTextView =
                convertView.findViewById(R.id.message_text_view);


        Message message = getItem(position);

        boolean isTextSend = message.getImageUlr() == null;
        if(isTextSend){
            photoImageView.setVisibility(View.GONE);
            messageTextView.setVisibility(View.VISIBLE);
            messageTextView.setText(message.getText());
        }else {
            messageTextView.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext())
                    .load(message.getImageUlr()).into(photoImageView);
        }

        userTextView.setText(message.getUserName());

        return convertView;
    }
}