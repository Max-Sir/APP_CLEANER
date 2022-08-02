package com.security.applock.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.security.applock.R;
import com.security.applock.databinding.ItemAppSelectBinding;
import com.security.applock.model.TaskInfo;

import java.util.List;

public class AppSelectAdapter extends BaseRecyclerAdapter<TaskInfo, AppSelectAdapter.ViewHolder> {

    private ItemClickListener itemClickListener;
    private PackageManager packageManager;

    public AppSelectAdapter(Context context, List<TaskInfo> list) {
        super(context, list);
        this.packageManager = mContext.getPackageManager();
    }

    public interface ItemClickListener {
        void OnClickItem();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(AppSelectAdapter.ViewHolder holder, int position) {
        holder.binData(list.get(position));
    }

    @Override
    public AppSelectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = layoutInflater.inflate(R.layout.item_app_select, parent, false);
        return new ViewHolder(mView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemAppSelectBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemAppSelectBinding.bind(itemView);
        }

        public void binData(TaskInfo mTaskInfo) {
            if (mTaskInfo != null) {
                if (!TextUtils.isEmpty(mTaskInfo.getTitle()))
                    binding.tvAppname.setText(mTaskInfo.getTitle());
                binding.imIcon.setImageDrawable(mTaskInfo.getAppinfo().loadIcon(packageManager));
                binding.cbSelect.setChecked(mTaskInfo.isChceked());
                binding.cbSelect.setOnClickListener(v -> {
                    mTaskInfo.setChceked(!mTaskInfo.isChceked());
                    if (itemClickListener != null)
                        itemClickListener.OnClickItem();
                });

                itemView.setOnClickListener(view -> {
                    binding.cbSelect.setChecked(!binding.cbSelect.isChecked());
                    mTaskInfo.setChceked(!mTaskInfo.isChceked());
                    if (itemClickListener != null)
                        itemClickListener.OnClickItem();
                });
            }
        }
    }
}
