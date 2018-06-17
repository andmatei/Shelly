package com.shelly.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shelly.R;

import java.util.List;

public class DomainListAdapter extends RecyclerView.Adapter<DomainListAdapter.ViewHolder> {

    private List<String> DomainList;
    private Context mContext;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.domain_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final String Domain = DomainList.get(position);
        holder.mDomainTV.setText(Domain);
        holder.mWraper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mCheckedDomainIB.setBackground(mContext.getResources().getDrawable(R.drawable.ic_selected_domain));
            }
        });

    }

    @Override
    public int getItemCount() {
        return DomainList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton mCheckedDomainIB;
        public TextView mDomainTV;
        public ConstraintLayout mWraper;

        public ViewHolder(View itemView) {
            super(itemView);
            mWraper = itemView.findViewById(R.id.DomainItemWraper);
            mCheckedDomainIB = itemView.findViewById(R.id.checkedDomainImageBtn);
            mDomainTV = itemView.findViewById(R.id.domainTextView);
        }
    }

}
