package com.security.applock.ui.themes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.security.applock.R;
import com.security.applock.adapter.BaseRecyclerAdapter;
import com.security.applock.databinding.ItemThemeLockBinding;

import java.util.List;

public class ThemesAdapter extends BaseRecyclerAdapter<Integer, RecyclerView.ViewHolder> {
    private int itemSelected;

    public int getItemSelected() {
        return itemSelected;
    }

    public void setItemSelected(int itemSelected) {
        this.itemSelected = itemSelected;
    }

    public ThemesAdapter(Context context, List<Integer> list, int itemSelected) {
        super(context, list);
        this.itemSelected = itemSelected;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).bind(list.get(position));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemThemeLockBinding binding = ItemThemeLockBinding.bind(LayoutInflater.from(mContext).inflate(R.layout.item_theme_lock, parent, false));
        return new ViewHolder(binding);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemThemeLockBinding binding;

        public ViewHolder(ItemThemeLockBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Integer integer) {
            Glide.with(mContext).load(integer)
                    .apply(new RequestOptions().override(150, 300))
                    .into(binding.imvTheme);
            binding.rb.setChecked(integer == itemSelected);
//            binding.imPattern.setVisibility(getAdapterPosition() == 0 ? View.GONE : View.VISIBLE);

            binding.imPattern.setVisibility(View.VISIBLE);


            binding.container.setOnClickListener(v -> {
                itemSelected = integer;
                notifyDataSetChanged();
            });
        }
    }
}

