package com.shelly.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shelly.Models.ActivityStatus;
import com.shelly.R;
import com.shelly.Adapters.ActivityListAdapter;
import com.shelly.Utils.Constants;
import com.shelly.Utils.FirebaseMethods;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;

public class MainActivity extends AppCompatActivity {


    //Views
    private ImageView mBackground;
    private ImageView mSecondBackground;
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
    private List<ActivityStatus> mActivityList;
    private int mPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View Binding
        mBackground = findViewById(R.id.MainActivityBackground);
        mSecondBackground = findViewById(R.id.MainActivityBackground2);
        mUserPhoto = findViewById(R.id.CircleImageView);
        mGreetingsTV = findViewById(R.id.GreetingTextView);
        mUsernameTV = findViewById(R.id.UsernameTextView);
        mWelcomingTextTV = findViewById(R.id.WelcomingTextTextView);
        mPostsRV = findViewById(R.id.RecommandedPostsRecyclerView);
        mActivitiesRV = findViewById(R.id.ActivitiesRecyclerView);

        //Firebase Binding
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mRefDatabase = mDatabase.getReference();
        mFirebaseMethods = new FirebaseMethods(this);

        if(mUser == null) {
            Intent i = new Intent(this, WelcomeActivity.class);
            startActivity(i);
            finish();
        } else {
            Log.e("ID", mUser.getUid());
            mFirebaseMethods.checkAccountSettingsCompletion();

            //Implementing Functionalities
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window w = getWindow(); // in Activity's onCreate() for instance
                w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
            initializeData();
        }
    }

    private void initializeData() {
        Query query = mRefDatabase.child(getString(R.string.dbfield_members)).child(mUser.getUid()).child(getString(R.string.dbfield_members_activities));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mActivityList = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    ActivityStatus activityStatus = ds.getValue(ActivityStatus.class);
                    if(activityStatus !=null) {
                        mFirebaseMethods.advanceActivityStatus(activityStatus, dataSnapshot);
                        if(activityStatus.isLocked()) {
                            mActivityList.add(activityStatus);
                        } else {
                            mActivityList.add(0, activityStatus);
                        }
                    }
                }
                initializeWidgets();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initializeWidgets() {
        mActivityListAdapter = new ActivityListAdapter(mActivityList, MainActivity.this);
        mActivitiesRV.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mActivitiesRV.setAdapter(mActivityListAdapter);
        mActivitiesRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == SCROLL_STATE_IDLE) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int pos = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    if(pos != mPosition && pos != -1) {
                        mPosition = pos;
                        LayerDrawable layerDrawable = (LayerDrawable) getResources().getDrawable(R.drawable.bg_activity_main);
                        final Drawable drawable = layerDrawable.findDrawableByLayerId(getResources().getIdentifier("Background" + (mPosition % Constants.NUMBER_OF_GRADIENTS), "id", "com.shelly"));

                        Animation fadeOut = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out);
                        mSecondBackground.setBackground(mBackground.getBackground());
                        mSecondBackground.setVisibility(View.VISIBLE);
                        mSecondBackground.startAnimation(fadeOut);

                        fadeOut.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                mBackground.setBackground(drawable);
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                mSecondBackground.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                }
            }
        });
    }
}
