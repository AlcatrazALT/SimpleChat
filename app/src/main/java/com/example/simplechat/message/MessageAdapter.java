package com.example.simplechat.message;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.simplechat.R;

import org.w3c.dom.Text;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    public static final int SENDER_SIDE = 0;
    public static final int RECIPIENT_SIDE = 1;
    public static final int NUMBER_VIEW_TYPE_COUNT = 2;


    private List<Message> messageList;
    private Activity activity;

    private class ViewHolder {
        private TextView messageTextView;
        private ImageView photoImageView;

        public ViewHolder(View view) {
            messageTextView = view.findViewById(R.id.message_text_view);
            photoImageView = view.findViewById(R.id.photo_image_view);
        }
    }

    public MessageAdapter(Activity context, int resource,
                          List<Message> messages) {
        super(context, resource, messages);
        this.messageList = messages;
        this.activity = context;
    }

    @Override
    public int getItemViewType(int position) {

        int switchSenderSide;
        Message message = messageList.get(position);

        if (message.isSenderSide()) {
            switchSenderSide = SENDER_SIDE;
        } else {
            switchSenderSide = RECIPIENT_SIDE;
        }
        return switchSenderSide;
    }

    @Override
    public int getViewTypeCount() {
        return NUMBER_VIEW_TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        LayoutInflater layoutInflater = (LayoutInflater)
                activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        Message message = getItem(position);
        int layoutResource = setSenderSide(position);

        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = layoutInflater.
                    inflate(layoutResource, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        setMessageType(viewHolder, message);

        return convertView;
    }

    private void setMessageType(ViewHolder viewHolder, Message message) {
        boolean isTextSend = message.getImageUlr() == null;

        if (isTextSend) {
            viewHolder.photoImageView.setVisibility(View.GONE);
            viewHolder.messageTextView.setVisibility(View.VISIBLE);
            viewHolder.messageTextView.setText(message.getText());

        } else {
            viewHolder.messageTextView.setVisibility(View.GONE);
            viewHolder.photoImageView.setVisibility(View.VISIBLE);
            Glide.with(viewHolder.photoImageView.getContext())
                    .load(message.getImageUlr())
                    .into(viewHolder.photoImageView);
        }
    }

    private int setSenderSide(int position) {
        int layoutResource = 0;
        int viewType = getItemViewType(position);

        if (viewType == SENDER_SIDE) {
            layoutResource = R.layout.sender_message_item;
        } else {
            layoutResource = R.layout.recipient_message_item;
        }
        return layoutResource;
    }
}

















