package com.shelly;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    //Views
    ImageButton mBackBtn;
    Button mSignUpBtn;
    private EditText mEmailET;
    private EditText mPasswordET;
    private EditText mConfirmPasswordET;
    private EditText mUsernameEt;

    //Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Firebase Binding
        mAuth = FirebaseAuth.getInstance();

        //Views Binding
        mBackBtn = (ImageButton) findViewById(R.id.BackImageButton);
        mSignUpBtn = (Button) findViewById(R.id.SignUpButton);
        mEmailET = (EditText) findViewById(R.id.EmailEditText);
        mPasswordET = (EditText) findViewById(R.id.PasswordEditText);
        mConfirmPasswordET = (EditText) findViewById(R.id.ConfirmPasswordEditText);
        mUsernameEt = (EditText) findViewById(R.id.UsernameEditText);

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
                final String Email = mEmailET.getText().toString();
                final String Password = mPasswordET.getText().toString();
                final String Username = mUsernameEt.getText().toString();
                String ConfPassword = mConfirmPasswordET.getText().toString();

                if(TextUtils.isEmpty(Email)) {
                    Toast.makeText(SignUpActivity.this, "Please enter an email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(Username)) {
                    Toast.makeText(SignUpActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(Password)){
                    Toast.makeText(SignUpActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Password.length() < 6) {
                    Toast.makeText(SignUpActivity.this, "Please enter a minimum 6 character password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Password.equals(ConfPassword)) {
                    Toast.makeText(SignUpActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {

                            HashMap<String, String> mUserData = new HashMap<String, String>();
                            mUserData.put("Email", Email);
                            mUserData.put("Password", Password);
                            mUserData.put("Username", Username);
                            Intent i = new Intent(SignUpActivity.this, AccountRegisterActivity.class);
                            i.putExtra("UserData", mUserData);
                            startActivity(i);

                        } else {
                            Toast.makeText(SignUpActivity.this, "Authentification failed." + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
