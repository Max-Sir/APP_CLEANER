package com.lubuteam.sellsourcecode.supercleaner.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.databinding.ItemSelected2Binding;

import java.util.List;


public class SelectItemAdapter2 extends BaseRecyclerAdapter2<SelectModel2, SelectItemAdapter2.ViewHolder> {

    public SelectItemAdapter2(Context context, List<SelectModel2> list) {
        super(context, list);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binData(list.get(position), position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_selected2, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemSelected2Binding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSelected2Binding.bind(itemView);
        }

        public void binData(SelectModel2 selectModel, int position) {
            if (selectModel == null) {
                return;
            }

            if (!TextUtils.isEmpty(selectModel.getTitle())) {
                binding.rdTitle.setText(selectModel.getTitle());
            }

            binding.rdItem.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            });
        }
    }
}
