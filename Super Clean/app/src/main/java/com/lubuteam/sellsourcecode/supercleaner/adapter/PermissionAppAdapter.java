package com.lubuteam.sellsourcecode.supercleaner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.utils.Config;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PermissionAppAdapter extends RecyclerView.Adapter<PermissionAppAdapter.ViewHolder> {

    private List<Config.PERMISSION_DANGEROUS> lstData;

    public PermissionAppAdapter(List<Config.PERMISSION_DANGEROUS> lstData) {
        this.lstData = lstData;
    }

    @NonNull
    @Override
    public PermissionAppAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_permission_app, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull PermissionAppAdapter.ViewHolder holder, int position) {
        if (lstData.get(position) != null)
            holder.binData(lstData.get(position));
    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_content)
        TextView tvContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void binData(Config.PERMISSION_DANGEROUS mPermissionDangerous) {
            tvTitle.setText(mPermissionDangerous.title);
            tvContent.setText(mPermissionDangerous.description);
        }
    }
}
