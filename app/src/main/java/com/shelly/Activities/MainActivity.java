package com.shelly.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shelly.Models.CurrentActivity;
import com.shelly.R;
import com.shelly.Utils.ActivityListAdapter;
import com.shelly.Utils.BlurMethods;
import com.shelly.Utils.FirebaseMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {


    //Views
    private CircleImageView mUserPhoto;
    private TextView mGreetingsTV;
    private TextView mUsernameTV;
    private TextView mWelcomingTextTV;
    private RecyclerView mActivitiesRV;
    private RecyclerView mPostsRV;
    private ActivityListAdapter mActivityListAdapter;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefDatabase;
    private FirebaseUser mUser;
    private FirebaseMethods mFirebaseMethods;

    //Variables
    private BlurMethods mBlurMethods;
    private List<CurrentActivity> mActivityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View Binding
        mUserPhoto = findViewById(R.id.CircleImageView);
        mGreetingsTV = findViewById(R.id.GreetingTextView);
        mUsernameTV = findViewById(R.id.UsernameTextView);
        mWelcomingTextTV = findViewById(R.id.WelcomingTextTextView);
        mPostsRV = findViewById(R.id.RecommandedPostsRecyclerView);

        //Firebase Binding
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mRefDatabase = mDatabase.getReference();
        mFirebaseMethods = new FirebaseMethods(this);

        //Variable Binding
        mBlurMethods = new BlurMethods();

        if(mUser == null) {
            Intent i = new Intent(this, WelcomeActivity.class);
            startActivity(i);
            finish();
        } else {
            mFirebaseMethods.checkAccountSettingsCompletion();
            //Implementing Functionalities
            Log.e("UserID", mUser.getUid());
            initializeData();
        }
    }

    public void initializeData() {
        mRefDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mActivityList = new ArrayList<>();
                for(DataSnapshot ds: dataSnapshot.child(getString(R.string.dbfield_members)).child(mUser.getUid()).child(getString(R.string.dbfield_members_activities)).getChildren()) {
                    Log.e("Ds", ds.toString());
                    CurrentActivity currentActivity = ds.getValue(CurrentActivity.class);
                    mActivityList.add(currentActivity);
                }
                mActivitiesRV = findViewById(R.id.ActivitiesRecyclerView);
                mActivityListAdapter = new ActivityListAdapter(mActivityList, MainActivity.this);
                mActivitiesRV.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                mActivitiesRV.setAdapter(mActivityListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
