package com.shelly.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.shelly.Models.EntryContent;
import com.shelly.Models.TextTransformationModel;
import com.shelly.R;

import java.util.List;

public class TextTransformListAdapter extends RecyclerView.Adapter<TextTransformListAdapter.ViewHolder> {

    private static int CODE_SINGLE_CHECK = 0;

    private List<TextTransformationModel> mTextTransformList;
    private Context mContext;
    private int mMode;
    private EntryContent mEntryContent;
    private List<String> mTags;

    public TextTransformListAdapter(List<TextTransformationModel> mTextTransformList, Context mContext, int mMode, EntryContent mEntryContent) {
        this.mTextTransformList = mTextTransformList;
        this.mContext = mContext;
        this.mMode = mMode;
        this.mEntryContent = mEntryContent;
        mTags = mEntryContent.getTags();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_text_transform, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final TextTransformationModel textTransformationModel = mTextTransformList.get(position);
        holder.mToggleButton.setTypeface(textTransformationModel.getTextFont());
        holder.mToggleButton.setTextOff(Html.fromHtml(textTransformationModel.getFieldValue()));
        holder.mToggleButton.setTextOn(Html.fromHtml(textTransformationModel.getFieldValue()));
        holder.mToggleButton.setChecked(textTransformationModel.isSelected());
        holder.mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("Checked", "" + isChecked);
                if(!textTransformationModel.isSelected()) {
                    if (isChecked) {
                        if(mMode == CODE_SINGLE_CHECK) {
                            oneItemSelected(textTransformationModel);
                        } else {
                            mTags.add(textTransformationModel.getAssignedTag());
                            addTag(textTransformationModel.getAssignedTag());
                        }
                    } else {
                        mTags.remove(textTransformationModel.getAssignedTag());
                        removeTag(textTransformationModel.getAssignedTag());
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

    private void oneItemSelected(TextTransformationModel item) {
        for(int i = 0; i < mTextTransformList.size(); i++) {
            TextTransformationModel textTransformationModel = mTextTransformList.get(i);
            if(textTransformationModel.equals(item) && !item.isSelected()) {
                textTransformationModel.setSelected(true);
                mTextTransformList.remove(i);
                mTextTransformList.add(i, textTransformationModel);
                mTags.add(textTransformationModel.getAssignedTag());
                addTag(textTransformationModel.getAssignedTag());
            } else if(!textTransformationModel.equals(item) && textTransformationModel.isSelected()) {
                textTransformationModel.setSelected(false);
                mTextTransformList.remove(i);
                mTextTransformList.add(i, textTransformationModel);
                mTags.remove(textTransformationModel.getAssignedTag());
                removeTag(textTransformationModel.getAssignedTag());
            }
        }
        notifyDataSetChanged();
    }

    private void addTag(String tag) {
        mEntryContent.setText(mEntryContent.getText().append("<" + tag + ">"));
    }

    private void removeTag(String tag) {
        mEntryContent.setText(mEntryContent.getText().append("</" + tag + ">"));
    }
}
