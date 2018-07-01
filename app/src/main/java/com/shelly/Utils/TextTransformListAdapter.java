package com.shelly.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.ToggleButton;

import com.shelly.Models.TextTransformationUtils;
import com.shelly.R;

import java.util.ArrayList;
import java.util.List;

public class TextTransformListAdapter extends RecyclerView.Adapter<TextTransformListAdapter.ViewHolder> {

    private static int CODE_SINGLE_CHECK = 0;
    private static int CODE_MULTIPLE_CHECK = 1;

    private List<TextTransformationUtils> mTextTransformList;
    private Context mContext;
    private int mMode;
    private List<String> mTags;

    public TextTransformListAdapter(List<TextTransformationUtils> mTextTransformList, Context mContext, int mMode, List<String> mTags) {
        this.mTextTransformList = mTextTransformList;
        this.mContext = mContext;
        this.mMode = mMode;
        this.mTags = mTags;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_text_transform, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final TextTransformationUtils textTransformationUtils = mTextTransformList.get(position);
        holder.mToggleButton.setTypeface(textTransformationUtils.getTextFont());
        holder.mToggleButton.setTextOff(Html.fromHtml(textTransformationUtils.getFieldValue()));
        holder.mToggleButton.setTextOn(Html.fromHtml(textTransformationUtils.getFieldValue()));
        holder.mToggleButton.setChecked(textTransformationUtils.isSelected());
        holder.mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("Checked", "" + isChecked);
                if(!textTransformationUtils.isSelected()) {
                    if (isChecked) {
                        if(mMode == CODE_SINGLE_CHECK) {
                            oneItemSelected(textTransformationUtils);
                        } else {
                            mTags.add(textTransformationUtils.getAssignedTag());
                        }
                    } else {
                        mTags.remove(textTransformationUtils.getAssignedTag());
                    }
                } else {
                    if(mMode == CODE_SINGLE_CHECK) {
                        if(!isChecked) {
                            holder.mToggleButton.setChecked(true);
                        }
                    }
                }
                for(String s : mTags) {
                    Log.e("Tag", s);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTextTransformList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ToggleButton mToggleButton;

        private ViewHolder(View itemView) {
            super(itemView);
            mToggleButton = itemView.findViewById(R.id.ToggleTextTransformation);
        }
    }

    private void oneItemSelected(TextTransformationUtils item) {
        for(int i = 0; i < mTextTransformList.size(); i++) {
            TextTransformationUtils textTransformationUtils = mTextTransformList.get(i);
            if(textTransformationUtils.equals(item) && !item.isSelected()) {
                textTransformationUtils.setSelected(true);
                mTextTransformList.remove(i);
                mTextTransformList.add(i, textTransformationUtils);
                mTags.add(textTransformationUtils.getAssignedTag());
            } else if(!textTransformationUtils.equals(item) && textTransformationUtils.isSelected()) {
                textTransformationUtils.setSelected(false);
                mTextTransformList.remove(i);
                mTextTransformList.add(i, textTransformationUtils);
                mTags.remove(textTransformationUtils.getAssignedTag());
            }
        }
        notifyDataSetChanged();
    }
}
