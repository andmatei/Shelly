package com.shelly.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shelly.Models.User;
import com.shelly.R;
import com.shelly.Utils.FirebaseMethods;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    //Views
    ImageButton mBackBtn;
    Button mSignUpBtn;
    private EditText mEmailET;
    private EditText mPasswordET;
    private EditText mConfirmPasswordET;
    private EditText mUsernameEt;

    //Variables
    private String Username, Password, Email;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefDatabase;
    private FirebaseMethods mFirebaseMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Firebase Binding
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRefDatabase = mDatabase.getReference();
        mFirebaseMethods = new FirebaseMethods(this);
        mAuth = FirebaseAuth.getInstance();

        //Views Binding
        mBackBtn = findViewById(R.id.BackImageButton);
        mSignUpBtn = findViewById(R.id.SignUpButton);
        mEmailET = findViewById(R.id.EmailEditText);
        mPasswordET = findViewById(R.id.PasswordEditText);
        mConfirmPasswordET = findViewById(R.id.ConfirmPasswordEditText);
        mUsernameEt = findViewById(R.id.UsernameEditText);

        //Implementing Functionalities
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, WelcomeActivity.class);
                startActivity(i);
                finish();
            }
        });

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = mEmailET.getText().toString();
                Password = mPasswordET.getText().toString();
                Username = mUsernameEt.getText().toString();
                String ConfPassword = mConfirmPasswordET.getText().toString();

                if(TextUtils.isEmpty(Email)) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.error_email_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(Username)) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.error_username_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(Password)){
                    Toast.makeText(SignUpActivity.this, getString(R.string.error_password_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Password.length() < 6) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.error_password_short), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Password.equals(ConfPassword)) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.error_password_match), Toast.LENGTH_SHORT).show();
                    return;
                }
                checkIfUsernameExists();
            }
        });

    }

    private void checkIfUsernameExists() {

        Query query = mRefDatabase
                .child(getString(R.string.dbfield_users))
                .orderByChild(getString(R.string.dbfield_user_username))
                .equalTo(Username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean UsernameExists = false;
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    if (singleSnapshot.exists()){
                        Toast.makeText(SignUpActivity.this, getString(R.string.error_username_exists), Toast.LENGTH_SHORT).show();
                        UsernameExists = true;
                        break;
                    }
                }
                if(!UsernameExists) {
                    mFirebaseMethods.registerNewEmail(Email, Password, Username);
                    Toast.makeText(SignUpActivity.this, getString(R.string.sign_up_successful), Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
