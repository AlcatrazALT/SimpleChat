package com.example.simplechat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.simplechat.login.SingUpActivity;
import com.example.simplechat.message.Message;
import com.example.simplechat.message.MessageAdapter;
import com.example.simplechat.user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView messageListView;
    private MessageAdapter messageAdapter;
    private ProgressBar progressBar;
    private ImageButton sendImageButton;
    private Button sendMessageButton;
    private EditText messageEditText;

    private String userName;
    List<Message> messageList;

    FirebaseDatabase database;
    DatabaseReference messageDBReference;
    ChildEventListener messageChildDBEventListener;

    DatabaseReference userDBReference;
    ChildEventListener userChildDBEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createMainActivitySetup();

        setUserNameInChat();

        useSendTextMessageButton();

        useSendImageButton();

        setupMessageEditText();

        createFireBaseSetup();
    }

    private void setUserNameInChat() {
        Intent intent = getIntent();
        if(intent != null){
            userName = intent.getStringExtra("userName");
        } else{
            userName = "Default User";
        }
    }

    private void setupMessageEditText() {
        messageEditText.setFilters(new InputFilter[]
                {new InputFilter.LengthFilter(130)});

        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

                if (s.toString().trim().length() > 0) {
                    sendMessageButton.setEnabled(true);
                } else {
                    sendMessageButton.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void useSendImageButton() {
        sendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void useSendTextMessageButton() {
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message(
                        userName,
                        messageEditText.getText().toString(),
                        null);

                messageDBReference.push().setValue(message);
                messageEditText.setText("");
            }
        });
    }

    private void createFireBaseSetup() {
        database = FirebaseDatabase.getInstance();

        createMessageChildDB();

        createUserChildDB();
    }

    private void createUserChildDB() {
        userDBReference = database.getReference().child("user");
        userChildDBEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                if(user.getId()
                        .equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    userName = user.getName();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        userDBReference.addChildEventListener(userChildDBEventListener);
    }

    private void createMessageChildDB() {
        messageDBReference = database.getReference().child("message");
        messageChildDBEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot,
                                     @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);

                messageAdapter.add(message);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot,
                                       @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot,
                                     @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        messageDBReference.addChildEventListener(messageChildDBEventListener);
    }

    private void createMainActivitySetup() {

        messageList = new ArrayList<>();

        messageListView = findViewById(R.id.send_message_list_view);
        messageAdapter = new MessageAdapter(this,
                R.layout.message_item, messageList);

        progressBar = findViewById(R.id.progress_bar);
        sendImageButton = findViewById(R.id.send_photo);
        sendMessageButton = findViewById(R.id.send_message_button);
        messageEditText = findViewById(R.id.message_edit_text);

        messageListView.setAdapter(messageAdapter);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sing_out) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, SingUpActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}