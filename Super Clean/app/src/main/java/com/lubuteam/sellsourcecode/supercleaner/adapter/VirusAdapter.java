package com.lubuteam.sellsourcecode.supercleaner.adapter;

import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.model.TaskInfo;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VirusAdapter extends RecyclerView.Adapter<VirusAdapter.ViewHolder> {

    private List<TaskInfo> lstData;
    private PackageManager packageManager;
    private ClickButtonListener mClickButtonListener;

    public VirusAdapter(List<TaskInfo> lstData, ClickButtonListener mClickButtonListener) {
        this.lstData = lstData;
        this.mClickButtonListener = mClickButtonListener;
    }

    public interface ClickButtonListener {
        void onClickUninstall(int position);

        void onClickIgnore(int position);
    }

    @NonNull
    @Override
    public VirusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_virus, parent, false);
        this.packageManager = parent.getContext().getPackageManager();
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull VirusAdapter.ViewHolder holder, int position) {
        if (lstData.get(position) != null)
            holder.binData(lstData.get(position));
    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_appname)
        TextView tvAppName;
        @BindView(R.id.tv_pkgname)
        TextView tvPkgName;
        @BindView(R.id.im_iconApp)
        RoundedImageView imIconApp;
        @BindView(R.id.tv_virus_name)
        TextView tvVirusName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void binData(TaskInfo mTaskInfo) {
            if (!TextUtils.isEmpty(mTaskInfo.getTitle()))
                tvAppName.setText(mTaskInfo.getTitle());
            if (!TextUtils.isEmpty(mTaskInfo.getPackageName()))
                tvPkgName.setText(mTaskInfo.getPackageName());
            if (!TextUtils.isEmpty(mTaskInfo.getVirusName()))
                tvVirusName.setText(mTaskInfo.getVirusName());
            imIconApp.setImageDrawable(mTaskInfo.getAppinfo().loadIcon(packageManager));
        }

        @OnClick({R.id.tv_ignore, R.id.tv_uninstall})
        void click(View mView) {
            if (mClickButtonListener != null)
                switch (mView.getId()) {
                    case R.id.tv_uninstall:
                        mClickButtonListener.onClickUninstall(getAdapterPosition());
                        break;
                    case R.id.tv_ignore:
                        mClickButtonListener.onClickIgnore(getAdapterPosition());
                        break;
                }
        }
    }
}
