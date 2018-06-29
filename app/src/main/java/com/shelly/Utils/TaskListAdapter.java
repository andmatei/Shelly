package com.shelly.Utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shelly.Models.ActivityStatus;
import com.shelly.Models.ActivityTask;
import com.shelly.R;

import org.w3c.dom.Text;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private List<ActivityTask> mTaskList;
    private boolean mCheckable;
    private List<Boolean> mTaskStatusList;
    private Context mContext;

    public TaskListAdapter(List<ActivityTask> mTaskList, boolean mCheckable, List<Boolean> mTaskStatusList, Context mContext) {
        this.mTaskList = mTaskList;
        this.mCheckable = mCheckable;
        this.mTaskStatusList = mTaskStatusList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_task, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.mExpandDescription.setScaleY(-1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ActivityTask activityTask = mTaskList.get(position);
        final String mDescriptionCondensed = activityTask.getDescription().substring(0, 25) + "...";
        final LayerDrawable layerDrawable = (LayerDrawable) mContext.getResources().getDrawable(R.drawable.bg_task_checkbox);

        holder.mTaskTitle.setText(activityTask.getTitle());
        holder.mTaskDescription.setText(Html.fromHtml(mDescriptionCondensed));
        if(mTaskStatusList.get(position)) {
            holder.mCheckTask.setBackground(layerDrawable.findDrawableByLayerId(R.id.Checked));
            holder.mCheckTask.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_checked_task));
        } else {
            holder.mCheckTask.setBackground(layerDrawable.findDrawableByLayerId(R.id.Unchecked));
            holder.mCheckTask.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_unselected_domain));
        }
        if(mCheckable) {
            holder.mCheckTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTaskStatusList.set(position, !mTaskStatusList.get(position));
                    if(mTaskStatusList.get(position)) {
                        holder.mCheckTask.setBackground(layerDrawable.findDrawableByLayerId(R.id.Checked));
                        holder.mCheckTask.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_checked_task));
                    } else {
                        holder.mCheckTask.setBackground(layerDrawable.findDrawableByLayerId(R.id.Unchecked));
                        holder.mCheckTask.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_unselected_domain));
                    }
                }


            });
        } else {
            holder.mCheckTask.setOnClickListener(null);
            holder.mTaskTitle.setPaintFlags(holder.mTaskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.mExpandDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mExpandDescription.setScaleY(holder.mExpandDescription.getScaleY() * -1);
                if(holder.mDescriptionCondensed) {
                    holder.mTaskDescription.setText(Html.fromHtml(activityTask.getDescription()));
                    holder.mTaskDescription.setTranslationY(16);
                } else {
                    holder.mTaskDescription.setText(Html.fromHtml(mDescriptionCondensed));
                    holder.mTaskDescription.setTranslationY(0);
                }
                holder.mDescriptionCondensed = !holder.mDescriptionCondensed;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTaskTitle;
        public TextView mTaskDescription;
        public ImageButton mCheckTask;
        public ImageButton mExpandDescription;

        boolean mDescriptionCondensed;


        public ViewHolder(View itemView) {
            super(itemView);
            mDescriptionCondensed = true;
            mTaskTitle = itemView.findViewById(R.id.TaskTitleTextView);
            mTaskDescription = itemView.findViewById(R.id.TaskDescriptionTextView);
            mCheckTask = itemView.findViewById(R.id.TaskCheckedImageButton);
            mExpandDescription = itemView.findViewById(R.id.ExpandDescriptionButton);
        }
    }
}
