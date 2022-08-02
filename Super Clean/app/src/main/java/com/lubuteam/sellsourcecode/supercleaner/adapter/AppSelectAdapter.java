package com.lubuteam.sellsourcecode.supercleaner.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.model.TaskInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppSelectAdapter extends RecyclerView.Adapter<AppSelectAdapter.ViewHolder> {

    private Context mContext;
    private List<TaskInfo> arrList;
    private PackageManager packageManager;
    private ItemClickListener mItemClickListener;
    private TYPE_SELECT mTypeSelect;

    public enum TYPE_SELECT {
        CHECK_BOX, SWITCH, ONLY_VIEW
    }

    public interface ItemClickListener {
        void OnClickItem(int position);
    }

    public void setmItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public AppSelectAdapter(Context mContext, TYPE_SELECT mTypeSelect, List<TaskInfo> arrList) {
        this.arrList = arrList;
        this.mContext = mContext;
        this.mTypeSelect = mTypeSelect;
        this.packageManager = mContext.getPackageManager();
    }

    @NonNull
    @Override
    public AppSelectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_app_select, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppSelectAdapter.ViewHolder holder, int position) {
        holder.binData(arrList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.im_icon)
        ImageView imApp;
        @BindView(R.id.tv_appname)
        TextView tvAppName;
        @BindView(R.id.cb_select)
        CheckBox cbSelect;
        @BindView(R.id.sw_select)
        SwitchCompat swSelect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void binData(TaskInfo mTaskInfo) {
            if (mTaskInfo != null) {
                if (mTypeSelect == TYPE_SELECT.CHECK_BOX) {
                    cbSelect.setVisibility(View.VISIBLE);
                    swSelect.setVisibility(View.GONE);
                } else if (mTypeSelect == TYPE_SELECT.SWITCH) {
                    swSelect.setVisibility(View.VISIBLE);
                    cbSelect.setVisibility(View.GONE);
                } else if (mTypeSelect == TYPE_SELECT.ONLY_VIEW) {
                    swSelect.setVisibility(View.GONE);
                    cbSelect.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(mTaskInfo.getTitle()))
                    tvAppName.setText(mTaskInfo.getTitle());
                imApp.setImageDrawable(mTaskInfo.getAppinfo().loadIcon(packageManager));
                cbSelect.setChecked(mTaskInfo.isChceked());
                swSelect.setChecked(mTaskInfo.isChceked());
                cbSelect.setOnClickListener(v -> {
                    callClick(mTaskInfo);
                });
                swSelect.setOnClickListener(v -> {
                    if (mTaskInfo.isClickEnable())
                        callClick(mTaskInfo);
                    else
                        swSelect.setChecked(mTaskInfo.isChceked());
                });
                itemView.setOnClickListener(v -> {
                    if (mTypeSelect == TYPE_SELECT.ONLY_VIEW && mItemClickListener != null)
                        mItemClickListener.OnClickItem(getAdapterPosition());
                });
            }
        }

        public void callClick(TaskInfo mTaskInfo) {
            mTaskInfo.setChceked(!mTaskInfo.isChceked());
            if (mItemClickListener != null)
                mItemClickListener.OnClickItem(getAdapterPosition());
        }
    }
}
