package com.security.applock.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.security.applock.R;
import com.security.applock.databinding.ItemSelectedBinding;
import com.security.applock.model.SelectModel;

import java.util.List;

public class SelectItemAdapter extends BaseRecyclerAdapter<SelectModel, SelectItemAdapter.ViewHolder> {

    private int itemSelected;

    public int getItemSelected() {
        return itemSelected;
    }

    public void setItemSelected(int itemSelected) {
        this.itemSelected = itemSelected;
    }

    public SelectItemAdapter(Context context, List<SelectModel> list) {
        super(context, list);
    }

    @Override
    public void onBindViewHolder(SelectItemAdapter.ViewHolder holder, int position) {
        holder.binData(list.get(position),position);
    }

    @Override
    public SelectItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_selected, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemSelectedBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSelectedBinding.bind(itemView);
        }

        public void binData(SelectModel selectModel, int position) {
            if (selectModel == null)
                return;
            if (!TextUtils.isEmpty(selectModel.getTitle()))
                binding.rdSelect.setText(selectModel.getTitle());
            binding.rdSelect.setChecked(selectModel.getId() == itemSelected);

            binding.rdSelect.setOnClickListener(v -> {
                itemSelected = selectModel.getId();
                notifyDataSetChanged();
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(position);
            });
        }
    }
}
