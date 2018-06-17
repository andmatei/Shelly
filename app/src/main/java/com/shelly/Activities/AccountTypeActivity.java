package com.shelly.Activities;

import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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

    //Firebase
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_type);

        //Firebase binding
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mRefDatabase = mDatabase.getReference();

        //Varables binding
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
        textView.setText(R.string.ambassador_account_type_description);

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
                String AccountType = "Member";
                if(!mMemberAccountType) {
                    AccountType = "Ambassador";
                }
                mRefDatabase.child(getString(R.string.dbfield_users)).
                                child(mUser.getUid()).
                                    child(getString(R.string.dbfield_user_accounttype)).
                                        setValue(AccountType);
                Intent i = new Intent(AccountTypeActivity.this, FinalSetUpActivity.class);
                i.putExtra("AccountType", AccountType);
                startActivity(i);
            }
        });


    }

}
