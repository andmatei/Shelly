package com.shelly;

import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.HashMap;

public class AccountRegisterActivity extends AppCompatActivity {

    //Views
    ImageButton mFinishActivityBtn;
    View mAmbassadorView;
    View mMemberView;
    ImageView mAccountTypeIV;

    //Resources
    LayerDrawable layerDrawable;

    //Variables
    private boolean mMemberAccountType = true;
    private HashMap<String, String> mUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_register);
        mUserData = (HashMap<String, String>) getIntent().getSerializableExtra("UserData");
        layerDrawable = (LayerDrawable) getResources().getDrawable(R.drawable.bg_cardview_account_type);


        mAmbassadorView = findViewById(R.id.AmbassadorCardview);
        mMemberView = findViewById(R.id.MemberCardview);
        mFinishActivityBtn = findViewById(R.id.FinishRegisterBtn);

        mMemberView.findViewById(R.id.BackgroundConstraintLayout).setBackground(layerDrawable.findDrawableByLayerId(R.id.bgSelectedType));
        mMemberView.findViewById(R.id.SelectedType).setVisibility(View.VISIBLE);

        mAccountTypeIV = (ImageView) mAmbassadorView.findViewById(R.id.AccountTypeImageView);
        mAccountTypeIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_ambassador));

        mAmbassadorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAmbassadorView.findViewById(R.id.SelectedType).setVisibility(View.VISIBLE);
                mAmbassadorView.findViewById(R.id.BackgroundConstraintLayout).setBackground(ld.findDrawableByLayerId(R.id.bgSelectedType));
                mMemberView.findViewById(R.id.BackgroundConstraintLayout).setBackground(ld.findDrawableByLayerId(R.id.bgNotSelectedType));
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
                Intent i = new Intent(AccountRegisterActivity.this, MainActivity.class);
                i.putExtra("UserData", mUserData);
                startActivity(i);
            }
        });


    }

}
