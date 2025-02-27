package com.example.simplechat.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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

import com.example.simplechat.R;
import com.example.simplechat.message.Message;
import com.example.simplechat.message.MessageAdapter;
import com.example.simplechat.user.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private static final int RC_IMAGE_PICKER = 300;

    private ListView messageListView;
    private MessageAdapter messageAdapter;
    private ProgressBar progressBar;
    private ImageButton sendImageButton;
    private Button sendMessageButton;
    private EditText messageEditText;

    private String userName;
    private String recipientUserId;
    private String recipientUserName;

    private List<Message> messageList;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference messageDBRef;
    private ChildEventListener messageChildDBEventListener;
    private DatabaseReference userDBRef;
    private ChildEventListener userChildDBEventListener;

    private FirebaseStorage firebaseStorage;
    private StorageReference chatImageStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        createMainActivitySetup();

        createFireBaseSetup();

        createFirebaseStorageSetup();

        setInfoToChat();

        useSendTextMessageButton();

        useSendImageButton();

        setupMessageEditText();

    }

    private void createFirebaseStorageSetup() {
        firebaseStorage = FirebaseStorage.getInstance();
        chatImageStorageRef = firebaseStorage.getReference().child("chat_images");
    }

    private void setInfoToChat() {
        Intent intent = getIntent();
        if(intent != null){
            userName = intent.getStringExtra("userName");
            recipientUserId = intent.getStringExtra("recipientUserId");
            recipientUserName = intent.getStringExtra("recipientUserName");
        }

        setTitle(recipientUserName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_IMAGE_PICKER && resultCode == RESULT_OK){
            Uri selectedImageUri = data.getData();
            final StorageReference imageStorageRef = chatImageStorageRef
                    .child(selectedImageUri.getLastPathSegment());

            UploadTask uploadTask = imageStorageRef.putFile(selectedImageUri);

            getDownloadURL(imageStorageRef, uploadTask);
        }
    }

    private void getDownloadURL(final StorageReference imageStorageRef, UploadTask uploadTask) {
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return imageStorageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Message message = new Message(
                            userName,
                            downloadUri.toString(),
                            auth.getCurrentUser().getUid(),
                            recipientUserId);

                    messageDBRef.push().setValue(message);
                } else {
                    // Handle failures
                    // ...
                }
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
                        null,
                        auth.getCurrentUser().getUid(),
                        recipientUserId);

                messageDBRef.push().setValue(message);
                messageEditText.setText("");
            }
        });
    }

    private void useSendImageButton() {
        sendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent,
                        "Choose an image"),
                        RC_IMAGE_PICKER);
            }
        });
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

    private void createFireBaseSetup() {
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        createMessageChildDB();

        createUserChildDB();
    }

    private void createUserChildDB() {
        userDBRef = database.getReference().child("user");
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
        userDBRef.addChildEventListener(userChildDBEventListener);
    }

    private void createMessageChildDB() {
        messageDBRef = database.getReference().child("message");
        messageChildDBEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot,
                                     @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                if(message.getSender().equals((auth.getCurrentUser().getUid()))
                        &&
                        message.getRecipient().equals(recipientUserId))
                {
                    message.setSenderSide(true);
                    messageAdapter.add(message);
                }else if(message.getRecipient().equals((auth.getCurrentUser().getUid()))
                        &&
                        message.getSender().equals(recipientUserId))
                {
                    message.setSenderSide(false);
                    messageAdapter.add(message);
                }
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
        messageDBRef.addChildEventListener(messageChildDBEventListener);
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
            startActivity(new Intent(ChatActivity.this, SingUpActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}