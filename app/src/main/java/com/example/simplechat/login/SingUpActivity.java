package com.example.simplechat.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.simplechat.MainActivity;
import com.example.simplechat.R;
import com.example.simplechat.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SingUpActivity extends AppCompatActivity {

    private static final String TAG = "SingUp ->";

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText nameEditText;
    private Button SingUpButton;
    private TextView tapToSingInTextView;
    private String userName;

    private boolean isSingUpModeActive;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference userDBReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createFirebaseSetup();

        createActivitySetup();

        useSingUpButton();

        forwardSingInUserToChat();
    }

    private void createFirebaseSetup() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userDBReference = database.getReference().child("user");
    }

    private void useSingUpButton() {
        SingUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSingUpOrSingInUser(
                        emailEditText.getText().toString().trim(),
                        passwordEditText.getText().toString().trim());
            }
        });
    }

    private void forwardSingInUserToChat() {
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SingUpActivity.this,
                    MainActivity.class));
        }
    }

    private void createActivitySetup() {
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        nameEditText = findViewById(R.id.name_edit_text);
        SingUpButton = findViewById(R.id.sign_up_button);
        tapToSingInTextView = findViewById(R.id.switch_sing_in_sign_up);
    }

    public void checkSingUpOrSingInUser(String email, String password) {
        if (isSingUpModeActive) {
            if (!passwordEditText.getText().toString().trim()
                    .equals(confirmPasswordEditText.getText().toString().trim())) {
                Toast.makeText(this, "Passwords don't match",
                        Toast.LENGTH_SHORT).show();

            } else if (emailEditText.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please input Email",
                        Toast.LENGTH_SHORT).show();
            } else if (passwordEditText.getText().toString().trim().length() <= 6) {
                Toast.makeText(this,
                        "Password length must be at least 7 characters",
                        Toast.LENGTH_SHORT).show();
            } else {
                singUpUser(email, password);
            }
        } else {
            if (emailEditText.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please input Email",
                        Toast.LENGTH_SHORT).show();
            } else if (passwordEditText.getText().toString().trim().length() <= 6) {
                Toast.makeText(this,
                        "Password length must be at least 7 characters",
                        Toast.LENGTH_SHORT).show();
            } else{
                singInUser(email, password);
            }
        }
    }

    private void singUpUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Sing Up User -> success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            createUser(user);

                            bindUserName();

                            //updateUI(user);
                        } else {
                            Log.w(TAG, "Sing Up User -> failure",
                                    task.getException());
                            Toast.makeText(SingUpActivity.this,
                                    "Sing Up User failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void singInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Sing In User -> success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            bindUserName();
                            //updateUI(user);
                        } else {

                            Log.w(TAG, "Sing In User -> failure", task
                                    .getException());
                            Toast.makeText(SingUpActivity.this,
                                    "Sing In User failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            // ...
                        }

                        // ...
                    }
                });
    }

    private void bindUserName() {
        Intent intent = new Intent(SingUpActivity.this,
                MainActivity.class);

        intent.putExtra("userName",
                nameEditText.getText().toString().trim());
        startActivity(intent);
    }

    private void createUser(FirebaseUser firebaseUser) {
        User user = new User(
                firebaseUser.getUid(),
                firebaseUser.getEmail(),
                nameEditText.getText().toString().trim());

        userDBReference.push().setValue(user);
    }

    public void switchSingUpInMode(View view) {
        if (isSingUpModeActive) {
            confirmPasswordEditText.setVisibility(View.GONE);
            SingUpButton.setText(getString(R.string.sing_ip));
            tapToSingInTextView.setText(getString(R.string.tap_to_sing_up));

            isSingUpModeActive = false;
        } else {
            confirmPasswordEditText.setVisibility(View.VISIBLE);
            SingUpButton.setText(getString(R.string.sing_up));
            tapToSingInTextView.setText(R.string.tap_to_sing_in);

            isSingUpModeActive = true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
}