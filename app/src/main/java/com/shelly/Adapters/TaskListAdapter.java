package com.shelly.Adapters;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shelly.Models.ActivityTask;
import com.shelly.R;
import com.shelly.Utils.TextTransformationUtils;

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ActivityTask activityTask = mTaskList.get(position);
        final String mDescriptionCondensed = activityTask.getDescription().substring(0, 25) + "...";
        final AnimatedVectorDrawable lineToTick = (AnimatedVectorDrawable) mContext.getDrawable(R.drawable.avd_line_to_tick);
        final AnimatedVectorDrawable tickToLine = (AnimatedVectorDrawable) mContext.getDrawable(R.drawable.avd_tick_to_line);

        holder.mTaskTitle.setText(activityTask.getTitle());
        holder.mTaskDescription.setText(Html.fromHtml(mDescriptionCondensed));
        if(mTaskStatusList.get(position)) {
            holder.mCheckTask.setImageDrawable(tickToLine);
            holder.mTaskTitle.setTextColor(mContext.getResources().getColor(R.color.textColor50));
            holder.mTaskDescription.setTextColor(mContext.getResources().getColor(R.color.textColor50));
            TextTransformationUtils.animateStrikeThrough(holder.mTaskTitle, TextTransformationUtils.CODE_PUT_STRIKETHROUGH);

        } else {
            holder.mCheckTask.setImageDrawable(lineToTick);
            holder.mTaskTitle.setTextColor(mContext.getResources().getColor(R.color.textColor));
            holder.mTaskDescription.setTextColor(mContext.getResources().getColor(R.color.textColor));
        }
        if(mCheckable) {
            holder.mCheckTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mTaskStatusList.get(holder.getAdapterPosition())) {
                        holder.mCheckTask.setImageDrawable(tickToLine);
                        holder.mTaskTitle.setTextColor(mContext.getResources().getColor(R.color.textColor));
                        holder.mTaskDescription.setTextColor(mContext.getResources().getColor(R.color.textColor));
                        TextTransformationUtils.animateStrikeThrough(holder.mTaskTitle, TextTransformationUtils.CODE_ERASE_STRIKETHROUGH);
                        tickToLine.start();
                    } else {
                        holder.mCheckTask.setImageDrawable(lineToTick);
                        holder.mTaskTitle.setTextColor(mContext.getResources().getColor(R.color.textColor50));
                        holder.mTaskDescription.setTextColor(mContext.getResources().getColor(R.color.textColor50));
                        lineToTick.start();
                        TextTransformationUtils.animateStrikeThrough(holder.mTaskTitle, TextTransformationUtils.CODE_PUT_STRIKETHROUGH);
                    }
                    mTaskStatusList.set(holder.getAdapterPosition(), !mTaskStatusList.get(holder.getAdapterPosition()));
                }


            });
        } else {
            holder.mCheckTask.setOnClickListener(null);
            TextTransformationUtils.animateStrikeThrough(holder.mTaskTitle, TextTransformationUtils.CODE_PUT_STRIKETHROUGH);
        }
        holder.mExpandDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mExpandDescription.setScaleY(holder.mExpandDescription.getScaleY() * -1);
                if(holder.mDescriptionCondensed) {
                    holder.mTaskDescription.setText(Html.fromHtml(activityTask.getDescription()));
                } else {
                    holder.mTaskDescription.setText(Html.fromHtml(mDescriptionCondensed));
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
