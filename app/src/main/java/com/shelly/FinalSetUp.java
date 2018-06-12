package com.shelly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.HashMap;

public class FinalSetUp extends AppCompatActivity {

    //Views
    private ImageView mAccountTypeIV;
    private TextView mTaskTitleTV;
    private TextView mTaskDescriptionTV;
    private Button mStartActivityBtn;

    //UserData
    private HashMap<String, String> mUserData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_set_up);

        //Variable Binding
        mUserData = (HashMap<String, String>) getIntent().getSerializableExtra("UserData");

        //View Binding
        mAccountTypeIV = findViewById(R.id.SetUpImageView);
        mTaskTitleTV = findViewById(R.id.SetUpTextView);
        mTaskDescriptionTV = findViewById(R.id.SetUpDescriprionTextView);
        mStartActivityBtn = findViewById(R.id.StartActivityButton);

        //Assign function
        if(mUserData.get("Type").toString().equals("Ambassador")) {
            mAccountTypeIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_ambassador));
            mTaskTitleTV.setText(getResources().getString(R.string.set_up_ambassador_title));
            mTaskDescriptionTV.setText(getResources().getString(R.string.set_up_ambassador_description));
        }
        
        mStartActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = null;
                if(mUserData.get("Type").toString().equals("Ambassador")) {
                    i = new Intent(FinalSetUp.this, MainActivity.class);
                } else if(mUserData.get("Type").toString().equals("Member")) {
                    i = new Intent(FinalSetUp.this, MainActivity.class);
                }
                i.putExtra("UserData", mUserData);
                startActivity(i);
            }
        });
    }
}
