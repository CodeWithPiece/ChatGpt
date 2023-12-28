package com.app.chatgpt.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.chatgpt.R;
import com.app.chatgpt.model.Message;
import com.app.chatgpt.views.activity.MainActivity;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    MainActivity mainActivity;
    List<Message> messageList;

    public ChatAdapter(MainActivity mainActivity, List<Message> messageList) {
        this.mainActivity = mainActivity;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messageList.get(holder.getAdapterPosition());
        if (message.getRole().equals("system")) {
            holder.botLayout.setVisibility(View.GONE);
            holder.userLayout.setVisibility(View.VISIBLE);
            holder.txtRightChat.setText(message.getContent());
        } else {
            holder.userLayout.setVisibility(View.GONE);
            holder.botLayout.setVisibility(View.VISIBLE);
            holder.txtLeftChat.setText(message.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtLeftChat, txtRightChat;
        LinearLayout userLayout, botLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtLeftChat = itemView.findViewById(R.id.txtLeftChat);
            txtRightChat = itemView.findViewById(R.id.txtRightChat);
            userLayout = itemView.findViewById(R.id.userLayout);
            botLayout = itemView.findViewById(R.id.botLayout);

        }
    }

}
