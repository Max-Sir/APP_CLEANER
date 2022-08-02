package com.lubuteam.sellsourcecode.supercleaner.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lubuteam.sellsourcecode.supercleaner.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppIconAdapter extends RecyclerView.Adapter<AppIconAdapter.ViewHolder> {

    private Context mContext;
    private List<String> lstApp;
    private OnClickItemListener mOnClickItemListener;

    public void setmOnClickItemListener(OnClickItemListener mOnClickItemListener) {
        this.mOnClickItemListener = mOnClickItemListener;
    }

    public interface OnClickItemListener {
        void clickItem(String pkgName);
    }

    public AppIconAdapter(List<String> lstApp) {
        this.lstApp = lstApp;
    }

    @NonNull
    @Override
    public AppIconAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_app_icon, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppIconAdapter.ViewHolder holder, int position) {
        holder.binData(lstApp.get(position));
    }

    @Override
    public int getItemCount() {
        return lstApp.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.im_iconApp)
        ImageView imIconApp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void binData(String pkgName) {
            if (pkgName != null) {
                try {
                    Drawable icon = mContext.getPackageManager().getApplicationIcon(pkgName);
                    imIconApp.setImageDrawable(icon);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                imIconApp.setImageResource(R.drawable.ic_add_round);
            }
            itemView.setOnClickListener(v -> {
                if (mOnClickItemListener != null)
                    mOnClickItemListener.clickItem(pkgName);
            });
        }
    }
}
