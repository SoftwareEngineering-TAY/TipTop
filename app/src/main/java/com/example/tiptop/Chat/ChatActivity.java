package com.example.tiptop.Chat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiptop.Adapters.ChatAdapter;
import com.example.tiptop.Database.DataChangeListener;
import com.example.tiptop.Database.Database;
import com.example.tiptop.Objects.Message;
import com.example.tiptop.R;

import java.util.ArrayList;

import static com.example.tiptop.Database.Database.sendMessage;
import static com.example.tiptop.Database.Database.updateChatListFromDB;


public class ChatActivity extends AppCompatActivity implements DataChangeListener {

    ArrayList<Message> messages;
    ChatAdapter mAdapter;
    Message msgToSend;
    RecyclerView mRecycle;
    EditText type;
    ImageButton sendBtn;
    TextView notAv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initializeClassVariables();

        notifyOnChange();
    }

    private void initializeClassVariables() {
        messages = new ArrayList<>();
        mAdapter = new ChatAdapter(messages);

        mRecycle = (RecyclerView) findViewById(R.id.recycler_view_chat);
        type = (EditText) findViewById(R.id.edit_text_message);
        sendBtn = (ImageButton) findViewById(R.id.text_message_send);
        notAv = (TextView)findViewById(R.id.no_chats_avilable);

        mRecycle.setAdapter(mAdapter);
    }

    @Override
    public void notifyOnChange() {
        createClickEvent();
        updateChatListFromDB(messages, mAdapter);
    }

    private void createClickEvent() {
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texkMsg = type.getText().toString();
                sendMessage(texkMsg);
                type.setText("");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Database.addListener(this);
    }

    @Override
    protected void onPause() {
        Database.removeListener(this);
        super.onPause();
    }
}
