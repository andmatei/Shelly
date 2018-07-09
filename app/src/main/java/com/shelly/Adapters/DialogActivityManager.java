package com.shelly.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shelly.Adapters.TaskListAdapter;
import com.shelly.Models.ActivityResource;
import com.shelly.Models.ActivityStatus;
import com.shelly.Models.ActivityTask;
import com.shelly.R;
import com.shelly.Utils.Constants;
import com.shelly.Utils.FirebaseMethods;

import java.util.ArrayList;
import java.util.List;

public class DialogActivityManager extends Dialog {

    private Context mContext;
    private int mPosition;

    //Views
    private ConstraintLayout mActivityManagerWrapper;
    private TextView mActivityLevelTV;
    private TextView mActivityDialogTitleTV;
    private ImageView mActivityIconIV;
    private ImageButton mExitDialogIB;
    private TextView mActivityProgressTV;
    private ProgressBar mActivityProgressbar;
    private RecyclerView mTaskRecyclerView;
    private TextView mActivityTotalPoints;
    private ImageButton mNextActivity;
    private ImageButton mPreviousActivity;
    private TaskListAdapter mTaskListAdapter;
    private LinearLayout mCongratulationsDialog;
    private Button mFinishDialogBtn;
    private TextView mEarnedPointsTextView;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefDatabase;
    private FirebaseUser mUser;
    private FirebaseMethods mFirebaseMethods;

    //Variables
    private ActivityResource mActivityResource;
    private ActivityStatus mActivityStatus;
    private ActivityResource mInitialActivityResource;
    private long mNumber;
    private long mLevel;

    public DialogActivityManager(@NonNull Context context, ActivityStatus activityStatus, int position) {
        super(context);
        this.mContext = context;
        this.mActivityStatus = activityStatus;
        this.mPosition = position;
        Log.e("ActivityStatus", "" + this.mActivityStatus.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity_manager);
        getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //Binding Views
        mActivityManagerWrapper = findViewById(R.id.DialogActivityManager);
        mActivityIconIV = findViewById(R.id.ActivityDialogIconImageView);
        mActivityLevelTV = findViewById(R.id.ActivityLevelTextView);
        mActivityDialogTitleTV = findViewById(R.id.ActivityDialogTitleTextView);
        mExitDialogIB = findViewById(R.id.ExitDialogImageButton);
        mActivityProgressTV = findViewById(R.id.ActivityDialogProgressTextView);
        mActivityProgressbar = findViewById(R.id.ActivityDialogProgressBar);
        mTaskRecyclerView = findViewById(R.id.ActivityDialogTasksRecyclerView);
        mActivityTotalPoints = findViewById(R.id.ActivityDialogPointsTextView);
        mNextActivity = findViewById(R.id.NextActivityButton);
        mPreviousActivity = findViewById(R.id.PreviousActivityButton);
        mCongratulationsDialog = findViewById(R.id.CongratulationsDialogScreen);
        mFinishDialogBtn = findViewById(R.id.CongratulationsDialogButton);
        mEarnedPointsTextView = findViewById(R.id.CongratulationsDialogPointsTextView);

        //Firebase Binding
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mRefDatabase = mDatabase.getReference();
        mFirebaseMethods = new FirebaseMethods(mContext);

        //Implementing Functionalities
        initializeActivityData();
    }

    private void initializeActivityData() {
        Query query = mRefDatabase.child(mContext.getString(R.string.dbfield_resources))
                .child(mContext.getString(R.string.dbfield_activities))
                .child(mActivityStatus.getFactor())
                .child(""+mActivityStatus.getLevel())
                .child(""+mActivityStatus.getNumber());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mActivityResource = new ActivityResource();
                mInitialActivityResource = new ActivityResource();

                mActivityResource = dataSnapshot.getValue(ActivityResource.class);
                mInitialActivityResource.setImageURL(mActivityResource.getImageURL());
                mInitialActivityResource.setTasks(mActivityResource.getTasks());
                Log.e("Initial Resource", mInitialActivityResource.toString());

                mLevel = mActivityStatus.getLevel();
                mNumber = mActivityStatus.getNumber();
                initializeDataWidgets(true, mActivityStatus.getTaskStatusList());
                initializeNavigationWidgets();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initializeDataWidgets(boolean Checkable, List<Boolean> TaskStatusList) {

        Log.e("Resource", "" + mActivityResource.getTasks());
        Log.e("TaskList", ""+TaskStatusList);

        String mString = "Level " + (mLevel + 1);
        mActivityLevelTV.setText(mString);

        mString = "Activity " + (mNumber + 1);
        mActivityDialogTitleTV.setText(mString);

        int Progress = 0;
        for(Boolean b : TaskStatusList) {
            if(b) {
                Progress++;
            }
        }
        Progress = Progress * 100 / TaskStatusList.size();
        mString = "Progress: " + Progress + "%";
        mActivityProgressTV.setText(mString);
        mActivityProgressbar.setProgress(Progress);

        int mTotalPoints = 0;
        for(ActivityTask activityTask : mActivityResource.getTasks()) {
            mTotalPoints += activityTask.getPoints();
        }
        mString = "" + mTotalPoints;
        mActivityTotalPoints.setText(mString);

        mTaskListAdapter = new TaskListAdapter(mActivityResource.getTasks(), Checkable, TaskStatusList, mContext);
        mTaskRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mTaskRecyclerView.setAdapter(mTaskListAdapter);
    }

    private void initializeNavigationWidgets() {


        LayerDrawable mLayerDrawable = (LayerDrawable) mContext.getResources().getDrawable(R.drawable.bg_cardview_activity);
        final GradientDrawable mGradientDrawable = (GradientDrawable) mLayerDrawable
                .findDrawableByLayerId(mContext.getResources().getIdentifier("Gradient" + (mPosition % Constants.NUMBER_OF_GRADIENTS), "id", "com.shelly")
        );
        mGradientDrawable.setCornerRadius(Constants.DIALOG_ICON_RADIUS);
        mActivityIconIV.setBackground(mGradientDrawable);

        mExitDialogIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TaskStatusList", "" + mActivityStatus.getTaskStatusList());
                Log.e("Progress", "" + mActivityStatus.getProgress());
                int mProgress = 0;
                for(Boolean b : mActivityStatus.getTaskStatusList()) {
                    if(b) {
                        mProgress++;
                    }
                }
                mProgress = mProgress * 100 / mActivityStatus.getTaskStatusList().size();
                mActivityStatus.setProgress(mProgress);
                mFirebaseMethods.updateActivityStatus(mActivityStatus);
                if(mProgress == 100) {
                    mCongratulationsDialog.setVisibility(View.VISIBLE);
                    mActivityManagerWrapper.setVisibility(View.GONE);
                    int mPoints = 0;
                    for(ActivityTask activityTask : mInitialActivityResource.getTasks()) {
                        mPoints += activityTask.getPoints();
                    }
                    mEarnedPointsTextView.setText(mPoints + " points");
                    mFirebaseMethods.addPoints(mPoints);
                    mFinishDialogBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mGradientDrawable.setCornerRadius(0);
                            dismiss();
                        }
                    });
                } else {
                    mGradientDrawable.setCornerRadius(0);
                    dismiss();
                }
            }
        });

        if(mLevel == 0 && mNumber == 0) {
            mPreviousActivity.setVisibility(View.GONE);
        }
        mPreviousActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNumber--;
                mNextActivity.setVisibility(View.VISIBLE);
                if(mNumber == 0 && mLevel == 0) {
                    mPreviousActivity.setVisibility(View.GONE);
                }

                Query query = mRefDatabase.child(mContext.getString(R.string.dbfield_resources))
                        .child(mContext.getString(R.string.dbfield_activities))
                        .child(mActivityStatus.getFactor())
                        .child("" + mLevel);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(mNumber < 0) {
                            mLevel--;
                            mNumber = dataSnapshot.getChildrenCount() - 1;
                        }
                        mActivityResource = dataSnapshot
                                .child("" + mNumber)
                                .getValue(ActivityResource.class);
                        List<Boolean> TaskStatusList = new ArrayList<>();
                        for(int i = 0 ; i < mActivityResource.getTasks().size(); i++) {
                            TaskStatusList.add(true);
                        }
                        Log.e("ActivityResource", mActivityResource.toString());
                        initializeDataWidgets(false, TaskStatusList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        mNextActivity.setVisibility(View.GONE);
        mNextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNumber++;
                mPreviousActivity.setVisibility(View.VISIBLE);
                if(mNumber == mActivityStatus.getNumber() && mLevel == mActivityStatus.getLevel()) {
                    mNextActivity.setVisibility(View.GONE);
                }

                Query query = mRefDatabase.child(mContext.getString(R.string.dbfield_resources))
                        .child(mContext.getString(R.string.dbfield_activities))
                        .child(mActivityStatus.getFactor())
                        .child(""+mLevel);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(mNumber > dataSnapshot.getChildrenCount() - 1) {
                            mNumber = 0;
                            mLevel++;
                        }
                        mActivityResource = dataSnapshot
                                .child("" + mNumber)
                                .getValue(ActivityResource.class);
                        List<Boolean> TaskStatusList = new ArrayList<>();
                        if(mNumber == mActivityStatus.getNumber() && mLevel == mActivityStatus.getLevel()) {
                            initializeDataWidgets(true, mActivityStatus.getTaskStatusList());
                        } else {
                            for(int i = 0 ; i < mActivityResource.getTasks().size(); i++) {
                                TaskStatusList.add(true);
                                initializeDataWidgets(false, TaskStatusList);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}
