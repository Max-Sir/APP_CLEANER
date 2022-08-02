package com.security.applock.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.security.applock.R;
import com.security.applock.databinding.ItemImgSelfieBinding;
import com.security.applock.utils.DateUtils;
import com.security.applock.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SelfieAdapter extends BaseRecyclerAdapter<File, SelfieAdapter.ViewHolder> {

    public SelfieAdapter(Context context, List<File> list) {
        super(context, list);
    }

    @Override
    public void onBindViewHolder(SelfieAdapter.ViewHolder holder, int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public SelfieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_img_selfie, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemImgSelfieBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemImgSelfieBinding.bind(itemView);
        }

        public void bindData(File file) {
            if (file == null)
                return;
            Glide.with(binding.imSelfie)
                    .load(file.getPath())
                    .into(binding.imSelfie);
            String fileName = file.getName()
                    .replace(".jpg", "")
                    .replace(".png", "");
            if (!TextUtils.isEmpty(fileName)) {
                String[] data = fileName.split("_");
                if (data.length > 1 && !TextUtils.isEmpty(data[1])) {
                    try {
                        binding.tvTime.setText(DateUtils.longToDateString(Long.parseLong(data[1]), DateUtils.DATE_FORMAT_3));
                    } catch (Exception e) {

                    }
                }
                if (data.length > 2 && !TextUtils.isEmpty(data[2])) {
                    binding.tvPin.setText(binding.tvPin.getContext().getString(R.string.pin_incorrect, data[2]));
                }
            }

            itemView.setOnClickListener(v -> {
                try {
                    FileUtil.openFile(mContext, file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
