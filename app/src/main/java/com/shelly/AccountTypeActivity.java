package com.shelly;

import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class AccountTypeActivity extends AppCompatActivity {

    //Views
    ImageButton mFinishActivityBtn;
    View mAmbassadorView;
    View mMemberView;
    ImageView mAccountTypeIV;
    TextView mAccountTypeTitleTV;

    //Resources
    LayerDrawable mLayerDrawable;

    //Variables
    private boolean mMemberAccountType = true;
    private HashMap<String, String> mUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_type);

        //Varables binding
        //mUserData = (HashMap<String, String>) getIntent().getSerializableExtra("UserData");
        mUserData = new HashMap<>();
        mLayerDrawable = (LayerDrawable) getResources().getDrawable(R.drawable.bg_cardview_account_type);

        //Views binding
        mAmbassadorView = findViewById(R.id.AmbassadorCardview);
        mMemberView = findViewById(R.id.MemberCardview);
        mFinishActivityBtn = findViewById(R.id.FinishRegisterBtn);
        mAccountTypeTitleTV = findViewById(R.id.AccountTypeTextView);

        mAccountTypeTitleTV.setText(Html.fromHtml(getResources().getString(R.string.accout_type_text)));

        //Member setup
        mMemberView.findViewById(R.id.BackgroundConstraintLayout).setBackground(mLayerDrawable.findDrawableByLayerId(R.id.bgSelectedType));
        mMemberView.findViewById(R.id.SelectedType).setVisibility(View.VISIBLE);

        //Ambassador setup
        mAccountTypeIV = (ImageView) mAmbassadorView.findViewById(R.id.AccountTypeImageView);
        mAccountTypeIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_ambassador));
        TextView textView = (TextView) mAmbassadorView.findViewById(R.id.AccountTypeTextView);
        textView.setText(R.string.ambassador_account_type_title);
        textView = (TextView) mAmbassadorView.findViewById(R.id.AccountDescriptionTextView);
        textView.setText(R.string.become_an_ambassador_text);

        //Implementing functionalities
        mAmbassadorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAmbassadorView.findViewById(R.id.SelectedType).setVisibility(View.VISIBLE);
                mAmbassadorView.findViewById(R.id.BackgroundConstraintLayout).setBackground(mLayerDrawable.findDrawableByLayerId(R.id.bgSelectedType));
                mMemberView.findViewById(R.id.BackgroundConstraintLayout).setBackground(mLayerDrawable.findDrawableByLayerId(R.id.bgNotSelectedType));
                mMemberView.findViewById(R.id.SelectedType).setVisibility(View.GONE);
                mMemberAccountType = false;
            }
        });
        mMemberView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAmbassadorView.findViewById(R.id.SelectedType).setVisibility(View.GONE);
                LayerDrawable ld = (LayerDrawable) getResources().getDrawable(R.drawable.bg_cardview_account_type);
                mMemberView.findViewById(R.id.BackgroundConstraintLayout).setBackground(ld.findDrawableByLayerId(R.id.bgSelectedType));
                mAmbassadorView.findViewById(R.id.BackgroundConstraintLayout).setBackground(ld.findDrawableByLayerId(R.id.bgNotSelectedType));
                mMemberView.findViewById(R.id.SelectedType).setVisibility(View.VISIBLE);
                mMemberAccountType = true;
            }
        });
        mFinishActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMemberAccountType) {
                    mUserData.put("Type", "Member");
                } else {
                    mUserData.put("Type", "Ambassador");
                }
                Intent i = new Intent(AccountTypeActivity.this, FinalSetUp.class);
                i.putExtra("UserData", mUserData);
                startActivity(i);
            }
        });


    }

}
