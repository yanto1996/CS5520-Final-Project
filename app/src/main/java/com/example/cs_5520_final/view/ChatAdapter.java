package com.example.cs_5520_final.view;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs_5520_final.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private final List<String> messages;

    public ChatAdapter(List<String> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_bubble, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        String message = messages.get(position);

        if (message.startsWith("You:")) {
            holder.messageText.setBackgroundResource(R.drawable.user_message_background);
            holder.messageText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.messageContainer.setGravity(Gravity.END);
            holder.messageText.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.userMessageColor));

        } else {
            holder.messageText.setBackgroundResource(R.drawable.bot_message_background);
            holder.messageText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            holder.messageContainer.setGravity(Gravity.START);
        }

        holder.messageText.setText(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        LinearLayout messageContainer;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
            messageContainer = (LinearLayout) itemView; // 根布局即为 LinearLayout
        }
    }
}

