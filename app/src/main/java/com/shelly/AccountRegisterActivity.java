package com.shelly;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class AccountRegisterActivity extends AppCompatActivity {

    ImageButton mFinishActivityBtn;
    View mAmbassadorView;
    View mMemberView;
    ImageView mAccountTypeIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_register);

        mAmbassadorView = findViewById(R.id.AmbassadorCardview);
        mMemberView = findViewById(R.id.MemberCardview);
        mFinishActivityBtn = findViewById(R.id.FinishRegisterBtn);

        mAccountTypeIV = (ImageView) mAmbassadorView.findViewById(R.id.AccountTypeImageView);
        mAccountTypeIV.setBackground(R.drawable.ic_member);



    }

}
