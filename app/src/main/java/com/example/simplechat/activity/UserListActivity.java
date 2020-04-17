package com.example.simplechat.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.simplechat.R;
import com.example.simplechat.user.User;
import com.example.simplechat.user.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private List<User> userList;
    private RecyclerView userRecyclerView;
    private RecyclerView.LayoutManager userLayoutManager;
    private UserAdapter userAdapter;

    FirebaseAuth mAuth;
    private DatabaseReference userDBRef;
    private ChildEventListener userChildDBEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        createFirebaseSetup();

        attachUserDBRefListener();

        buildRecyclerView();

    }

    private void createFirebaseSetup() {
        mAuth = FirebaseAuth.getInstance();
        userDBRef = FirebaseDatabase.getInstance().getReference().child("user");
    }

    private void attachUserDBRefListener() {
        if(userChildDBEventListener == null){
            userChildDBEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    User user = dataSnapshot.getValue(User.class);

                    if(!user.getId().equals(mAuth.getCurrentUser().getUid())){
                        user.setAvatarResource(R.drawable.ic_person_white_50dp);
                        userList.add(user);
                        userAdapter.notifyDataSetChanged();
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
    }

    private void buildRecyclerView(){
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList);
        userLayoutManager = new LinearLayoutManager(this);

        userRecyclerView = findViewById(R.id.user_list_recycler_view);
        userRecyclerView.setHasFixedSize(true);
        userRecyclerView.setLayoutManager(userLayoutManager);
        userRecyclerView.setAdapter(userAdapter);
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
            startActivity(new Intent(UserListActivity.this, SingUpActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}