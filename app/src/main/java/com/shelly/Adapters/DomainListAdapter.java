package com.shelly.Adapters;

import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shelly.R;

import java.util.HashMap;
import java.util.List;

public class DomainListAdapter extends RecyclerView.Adapter<DomainListAdapter.ViewHolder> {

    private List<String> mDomainsList;
    private Context mContext;
    private HashMap<String, Boolean> mSelectedDomains;

    public DomainListAdapter(List<String> mDomainsList, Context mContext) {
        this.mDomainsList = mDomainsList;
        this.mContext = mContext;
        this.mSelectedDomains = new HashMap<>();
        for(int i = 0; i < mDomainsList.size(); i++) {
            this.mSelectedDomains.put(mDomainsList.get(i), false);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_domain_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final LayerDrawable mLayerDrawable = (LayerDrawable) mContext.getResources().getDrawable(R.drawable.bg_round_checkbox);
        holder.mDomainTextView.setText(mDomainsList.get(position));
        holder.mWrapperConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mCheckedDomain = !holder.mCheckedDomain;
                if(holder.mCheckedDomain) {
                    holder.mCheckBoxImageView.setBackground(mLayerDrawable.findDrawableByLayerId(R.id.bgDomainSelected));
                    holder.mCheckBoxImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_selected_domain));
                    mSelectedDomains.put(mDomainsList.get(holder.getAdapterPosition()), true);
                } else {
                    holder.mCheckBoxImageView.setBackground(mLayerDrawable.findDrawableByLayerId(R.id.bgDomainUnselected));
                    holder.mCheckBoxImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_unselected_domain));
                    mSelectedDomains.put(mDomainsList.get(holder.getAdapterPosition()), false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDomainsList.size();
    }

    public HashMap<String, Boolean> getSelectedDomains() {
        return this.mSelectedDomains;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mDomainTextView;
        public ImageView mCheckBoxImageView;
        public ConstraintLayout mWrapperConstraintLayout;

        public boolean mCheckedDomain;

        public ViewHolder(View itemView) {
            super(itemView);
            mDomainTextView = itemView.findViewById(R.id.DomainTextView);
            mCheckBoxImageView = itemView.findViewById(R.id.CheckBoxImageView);
            mWrapperConstraintLayout = itemView.findViewById(R.id.DomainWrapperConstraintLayout);
            mCheckedDomain = false;
        }
    }
}
