package com.shelly.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shelly.Models.User;
import com.shelly.R;

import java.util.HashMap;

public class FinalSetUpActivity extends AppCompatActivity {

    //Views
    ImageView mAccountTypeIV;
    TextView mTaskTitleTV;
    TextView mTaskDescriptionTV;
    Button mStartActivityBtn;

    //Firebase
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mRefDatabase;

    //Variables
    String mAccountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_set_up);

        //Firebase Binding
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mRefDatabase = mDatabase.getReference();

        //View Binding
        mAccountTypeIV = findViewById(R.id.SetUpImageView);
        mTaskTitleTV = findViewById(R.id.SetUpTextView);
        mTaskDescriptionTV = findViewById(R.id.SetUpDescriprionTextView);
        mStartActivityBtn = findViewById(R.id.StartActivityButton);

        //Implementing Functionalities
        mRefDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mAccountType = (String) dataSnapshot.child(getString(R.string.dbfield_users)).
                                child(mUser.getUid()).
                                    child(getString(R.string.dbfield_user_accounttype)).getValue();
                if (mAccountType != null && mAccountType.equals(getString(R.string.ambassador))) {
                    mAccountTypeIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_ambassador));
                    mTaskTitleTV.setText(getResources().getString(R.string.set_up_ambassador_title));
                    mTaskDescriptionTV.setText(getResources().getString(R.string.set_up_ambassador_description));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        
        mStartActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                if(mAccountType.equals(getString(R.string.ambassador))) {
                    i = new Intent(FinalSetUpActivity.this, DomainActivity.class);
                } else {
                    i = new Intent(FinalSetUpActivity.this, TestActivity.class);
                }
                startActivity(i);
                finish();
            }
        });
    }
}
