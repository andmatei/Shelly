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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shelly.Models.CurrentActivity;
import com.shelly.R;
import com.shelly.Utils.ActivityListAdapter;
import com.shelly.Utils.BlurMethods;

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

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefDatabase;
    private FirebaseUser mUser;

    //Variables
    private BlurMethods mBlurMethods;
    private List<CurrentActivity> mTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTest();

        //View Binding
        mUserPhoto = findViewById(R.id.CircleImageView);
        mGreetingsTV = findViewById(R.id.GreetingTextView);
        mUsernameTV = findViewById(R.id.UsernameTextView);
        mWelcomingTextTV = findViewById(R.id.WelcomingTextTextView);
        mActivitiesRV = findViewById(R.id.ActivitiesRecyclerView);
        mPostsRV = findViewById(R.id.RecommandedPostsRecyclerView);
        ActivityListAdapter mActivityListAdapter = new ActivityListAdapter(mTest, this);
        mActivitiesRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mActivitiesRV.setAdapter(mActivityListAdapter);

        //Firebase Binding
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mRefDatabase = mDatabase.getReference();

        //Variable Binding
        mBlurMethods = new BlurMethods();

        //Implementing Functionalities
        /*if(mUser == null) {
            Intent i = new Intent(this, WelcomeActivity.class);
            startActivity(i);
        }*/

        //ViewTreeObserver vto = Test.getViewTreeObserver();
        /*vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Test.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width  = Test.getMeasuredWidth();
                int height = Test.getMeasuredHeight();
                ImageView BlurImgView = Test.findViewById(R.id.BluredImageView);
                View viewContainer = Test.findViewById(R.id.ActivityCardViewRoot);
                Bitmap mBlurBitmap = BlurMethods.createBlurBitmap(viewContainer, MainActivity.this);
                BlurMethods.setBackgroundOnView(BlurImgView, mBlurBitmap, MainActivity.this, 8);
            }
        });*/
    }
    public void initTest() {
        CurrentActivity currentActivity;
        mTest = new ArrayList<>();
        for(int i = 0 ; i < 7; i++) {
            currentActivity = new CurrentActivity(1, "Depression", 30, true, null);
            mTest.add(currentActivity);
        }
    }
}
