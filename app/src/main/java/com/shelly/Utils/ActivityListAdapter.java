package com.shelly.Utils;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.LayerDrawable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shelly.Activities.MainActivity;
import com.shelly.Models.CurrentActivity;
import com.shelly.R;

import junit.framework.Test;

import java.util.List;

public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.ViewHolder> {

    private List<CurrentActivity> mActivityList;
    private Context mContext;

    public ActivityListAdapter(List<CurrentActivity> mActivityList, Context mContext) {
        this.mActivityList = mActivityList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_activity, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        String auxString = "Day " + mActivityList.get(position).getNumber();
        holder.mActivityTitleTV.setText(auxString);

        auxString = "Progress: " + mActivityList.get(position).getProgress() + "%";
        holder.mActivityProgressTV.setText(auxString);

        holder.mActivityProgressBar.setProgress(mActivityList.get(position).getProgress());

        LayerDrawable mLayerDrawable = (LayerDrawable) mContext.getResources().getDrawable(R.drawable.bg_cardview_activity);
        holder.mActivityCardView.setBackground(mLayerDrawable
                .findDrawableByLayerId(
                        mContext.getResources().
                                getIdentifier("Gradient" + position % 5, "id", "com.shelly"))
                );

        if(mActivityList.get(position).isLocked()) {
            holder.mLockedActivity.setVisibility(View.VISIBLE);
            Bitmap mBlurBitmap = BlurMethods.createBlurBitmap(holder.mActivityCardView, mContext);
            BlurMethods.setBackgroundOnView(holder.mActivityBlurIV, mBlurBitmap, mContext, 8);
            holder.mActivityCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Activity locked.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            holder.mActivityCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Activity pressed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mActivityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout mActivityCardView;
        public ImageView mActivityIconIV;
        public TextView mActivityTitleTV;
        public TextView mActivityProgressTV;
        public ProgressBar mActivityProgressBar;
        public ConstraintLayout mLockedActivity;
        public ImageView mActivityBlurIV;

        public ViewHolder(View itemView) {
            super(itemView);
            mActivityCardView = itemView.findViewById(R.id.ActivityCardView);
            mActivityIconIV = itemView.findViewById(R.id.ActivityIconImageView);
            mActivityTitleTV = itemView.findViewById(R.id.ActivityTitleTextView);
            mActivityProgressTV = itemView.findViewById(R.id.ActivityProgressTextView);
            mActivityProgressBar = itemView.findViewById(R.id.ActivityProgressBar);
            mLockedActivity = itemView.findViewById(R.id.LockedActivity);
            mActivityBlurIV = itemView.findViewById(R.id.BluredImageView);
        }
    }
}
