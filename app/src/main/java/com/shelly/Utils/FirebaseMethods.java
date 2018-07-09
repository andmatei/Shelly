package com.shelly.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shelly.Activities.AccountTypeActivity;
import com.shelly.Activities.FinalSetUpActivity;
import com.shelly.Models.ActivityResource;
import com.shelly.Models.ActivityStatus;
import com.shelly.Models.User;
import com.shelly.Models.UserMember;
import com.shelly.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class FirebaseMethods {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRefDatabase;
    public String UserID;

    private Context mContext;

    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRefDatabase = mFirebaseDatabase.getReference();
        mContext = context;

        if(mAuth.getCurrentUser() != null){
            UserID = mAuth.getCurrentUser().getUid();
        }
    }

    public void registerNewEmail(final String email, String password, final String username){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, "Authentication failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if(task.isSuccessful()){
                            //sendVerificationEmail();
                            UserID = mAuth.getCurrentUser().getUid();
                            addNewUser(email, username, "");
                            Log.e("User Id", UserID);
                        }

                    }
                });
    }

    private void sendVerificationEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful())
                        Toast.makeText(mContext, mContext.getString(R.string.error_send_email_verification), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void addNewUser(String email, String username, String accountType){
        User user = new User(UserID, username, email, accountType);
        mRefDatabase.child(mContext.getString(R.string.dbfield_users))
                .child(UserID)
                .setValue(user);
    }

    public void checkAccountSettingsCompletion() {
        mRefDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String AccountType = (String) dataSnapshot.child(mContext.getString(R.string.dbfield_users)).
                                        child(UserID).
                                            child(mContext.getString(R.string.dbfield_user_accounttype)).
                                                getValue();
                if(TextUtils.isEmpty(AccountType)) {
                    Intent i = new Intent(mContext, AccountTypeActivity.class);
                    mContext.startActivity(i);
                    ((Activity) mContext).finish();
                } else {
                    if(AccountType.equals(mContext.getString(R.string.member))) {
                        if(!dataSnapshot.child(mContext.getString(R.string.dbfield_members)).hasChild(UserID)) {
                            Intent i = new Intent(mContext, FinalSetUpActivity.class);
                            mContext.startActivity(i);
                            ((Activity) mContext).finish();
                        }
                    } else if(AccountType.equals(mContext.getString(R.string.ambassador))) {
                        if(!dataSnapshot.child(mContext.getString(R.string.dbfield_ambassadors)).hasChild(UserID)) {
                            Intent i = new Intent(mContext, FinalSetUpActivity.class);
                            mContext.startActivity(i);
                            ((Activity) mContext).finish();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void initializeActivities(final HashMap<String, Integer> mTestResults) {

        //TODO 1: Implement the method to advance the user to the next level when retaking the test
        //TODO 2: Implement the method to verify if activities exist

        Query query = mRefDatabase.child(mContext.getString(R.string.dbfield_resources)).child(mContext.getString(R.string.dbfield_activities));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, ActivityStatus> mActivityList = new HashMap<>();
                for(String key : mTestResults.keySet()) {
                    if(!key.toLowerCase().equals("vigour")) {
                        int score = mTestResults.get(key);
                        List<Boolean> mTaskStatusList = new ArrayList<>();
                        for(int i = 0 ; i < dataSnapshot.child(key).child("0").child("0").child(mContext.getString(R.string.dbfield_activitiy_tasks)).getChildrenCount(); i++) {
                            mTaskStatusList.add(false);
                        }
                        score -= (mTestResults.get("vigour")/(mTestResults.size()-1));
                        mTestResults.put(key, score);
                        if(score >= 10) {
                            ActivityStatus activityStatus = new ActivityStatus(key, 0, 0, 0, false, mTaskStatusList, null);
                            mActivityList.put(key, activityStatus);
                        } else {
                            ActivityStatus activityStatus = new ActivityStatus(key, 0, 0, 0, true, mTaskStatusList, null);
                            mActivityList.put(key, activityStatus);
                        }
                    }
                }
                UserMember userMember = new UserMember(0, mTestResults, mActivityList);
                mRefDatabase.child(mContext.getString(R.string.dbfield_members)).child(UserID).setValue(userMember);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addPoints(final int Points) {
        Query query = mRefDatabase
                .child(mContext.getString(R.string.dbfield_members))
                .child(UserID);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int mPoints = dataSnapshot.getValue(UserMember.class).getPoints();
                mPoints += Points;
                mRefDatabase
                        .child(mContext.getString(R.string.dbfield_members)).
                        child(UserID).child(mContext.getString(R.string.dbfield_members_points)).setValue(mPoints);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void updateActivityStatus(ActivityStatus activityStatus) {
        if(activityStatus.getProgress() == 100) {
            Log.e("Date", getTimestamp(1));
            activityStatus.setChangeDate(getTimestamp(1));
            activityStatus.setLocked(true);
        } else {
            activityStatus.setChangeDate(null);
        }
        mRefDatabase.
                child(mContext.getString(R.string.dbfield_members)).
                child(UserID).
                child(mContext.getString(R.string.dbfield_members_activities)).
                child(activityStatus.getFactor()).setValue(activityStatus);
    }

    private boolean checkActivityStatus(ActivityStatus activityStatus) {
        if (activityStatus.getChangeDate()!=null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
            String Date = getTimestamp(0);
            try {
                if(sdf.parse(activityStatus.getChangeDate()).compareTo(sdf.parse(Date)) <= 0) {
                    return true;
                }
            } catch (ParseException e) {
                Log.e("Exception:", "" + e);
            }
        }
        return false;
    }

    public void advanceActivityStatus(ActivityStatus activityStatus, DataSnapshot dataSnapshot) {
        if(checkActivityStatus(activityStatus)) {
            if(dataSnapshot.child(mContext.getString(R.string.dbfield_resources)).
                    child(mContext.getString(R.string.dbfield_activities)).
                    child(activityStatus.getFactor()).
                    child(""+activityStatus.getLevel()).getChildrenCount() > activityStatus.getNumber() + 1) {

                /*if(dataSnapshot.child(mContext.getString(R.string.dbfield_resources)).
                        child(mContext.getString(R.string.dbfield_activities)).
                        child(activityStatus.getFactor()).getChildrenCount() > activityStatus.getLevel() + 1) {

                    activityStatus.setProgress(0);
                    activityStatus.setChangeDate(null);
                    activityStatus.setLocked(false);
                    activityStatus.setNumber(0);
                    activityStatus.setLevel(activityStatus.getLevel() + 1);
                    List<Boolean> mTaskStatusList = new ArrayList<>();
                    for(int i = 0 ; i < dataSnapshot.child(mContext.getString(R.string.dbfield_resources)).
                            child(mContext.getString(R.string.dbfield_activities)).
                            child(activityStatus.getFactor()).
                            child(""+activityStatus.getLevel()).
                            child(""+activityStatus.getNumber()).getChildrenCount(); i++) {
                            mTaskStatusList.add(false);
                    }
                    activityStatus.setTaskStatusList(mTaskStatusList);
                }*/
                activityStatus.setProgress(0);
                activityStatus.setChangeDate(null);
                activityStatus.setLocked(false);
                activityStatus.setNumber(activityStatus.getNumber() + 1);
                List<Boolean> mTaskStatusList = new ArrayList<>();
                for(int i = 0 ; i < dataSnapshot.child(mContext.getString(R.string.dbfield_resources)).
                        child(mContext.getString(R.string.dbfield_activities)).
                        child(activityStatus.getFactor()).
                        child(""+activityStatus.getLevel()).
                        child(""+activityStatus.getNumber()).
                        child(mContext.getString(R.string.dbfield_activitiy_tasks)).getChildrenCount(); i++) {
                    mTaskStatusList.add(false);
                }
                activityStatus.setTaskStatusList(mTaskStatusList);
            }

            mRefDatabase.child(mContext.getString(R.string.dbfield_members)).
                    child(UserID).
                    child(mContext.getString(R.string.dbfield_members_activities)).
                    child(activityStatus.getFactor()).setValue(activityStatus);
        }
    }

    private String getTimestamp(int mode) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        sdf.setTimeZone(TimeZone.getTimeZone("Romania"));
        String Date = sdf.format(new Date());
        if(mode == 1) {
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(Date));
            } catch (ParseException e) {
                Log.e("Exception:", "" + e);
            }
            c.add(Calendar.DAY_OF_MONTH, 1);
            return sdf.format(c.getTime());
        }
        return Date;
    }

}


