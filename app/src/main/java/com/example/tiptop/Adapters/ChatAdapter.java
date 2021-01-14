package com.example.tiptop.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tiptop.Objects.Message;
import com.example.tiptop.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import static com.example.tiptop.Database.Database.getUserID;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 2;
    private List<Message> messages;

    //default constructor
    public ChatAdapter(List<Message> mChats) {
        this.messages = mChats;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_ME:
                View viewChatMine = layoutInflater.inflate(R.layout.item_chat_mine, parent, false);
                viewHolder = new MyChatViewHolder(viewChatMine);
                break;
            case VIEW_TYPE_OTHER:
                View viewChatOther = layoutInflater.inflate(R.layout.item_chat_other, parent, false);
                viewHolder = new OtherChatViewHolder(viewChatOther);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (messages.get(position).senderUid.equals(getUserID())) {
            configureMyChatViewHolder((MyChatViewHolder) holder, position);
        }
        else {
            configureOtherChatViewHolder((OtherChatViewHolder) holder, position);
        }
    }

    //set data to user massage view
    private void configureMyChatViewHolder(final MyChatViewHolder myChatViewHolder, int position) {
        Message msg = messages.get(position);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sfd = new SimpleDateFormat("hh:mm a");
        String date=sfd.format(new Date(msg.timestamp + (1000*60*60*2)).getTime());
        myChatViewHolder.senderMsgTime.setText(date);
        myChatViewHolder.txtChatMessage.setText(msg.message);
        myChatViewHolder.txtUserAlphabet.setText(msg.sender.substring(0,2));
    }

    //set data to other's massage views
    private void configureOtherChatViewHolder(final OtherChatViewHolder otherChatViewHolder, int position) {
        Message message = messages.get(position);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sfd = new SimpleDateFormat("hh:mm a");
        String date=sfd.format(new Date(message.timestamp+ (1000*60*60*2)).getTime());
        otherChatViewHolder.receiverMsgTime.setText(date);
        otherChatViewHolder.txtChatMessage.setText(message.message);
        otherChatViewHolder.txtUserAlphabet.setText(message.sender.substring(0,2));
    }

    //num of massages
    @Override
    public int getItemCount() {
        return messages.size();
    }

    //how send the massage
    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).senderUid.equals(getUserID())) {
            return VIEW_TYPE_ME;
        } else {
            return VIEW_TYPE_OTHER;
        }
    }

    //class for user messages with view holder
    private static class MyChatViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtChatMessage;
        private final TextView txtUserAlphabet;
        private final TextView senderMsgTime;

        //set items references of massages
        public MyChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            senderMsgTime=(TextView) itemView.findViewById(R.id.senderMsgTime);
        }
    }

    //class for other's messages with view holder
    private static class OtherChatViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtChatMessage;
        private final TextView txtUserAlphabet;
        private final TextView receiverMsgTime;

        //set items references of massages
        public OtherChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            receiverMsgTime=(TextView) itemView.findViewById(R.id.receiverMsgTime);
        }
    }
}
