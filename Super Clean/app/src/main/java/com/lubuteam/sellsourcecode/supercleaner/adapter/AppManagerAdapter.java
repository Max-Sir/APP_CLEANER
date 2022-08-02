package com.lubuteam.sellsourcecode.supercleaner.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.model.GroupItemAppManager;
import com.lubuteam.sellsourcecode.supercleaner.utils.Toolbox;
import com.lubuteam.sellsourcecode.supercleaner.widget.AnimatedExpandableListView;

import java.io.File;
import java.util.List;

public class AppManagerAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    private LayoutInflater inflater;
    private PackageManager mPackageManager;
    private List<GroupItemAppManager> items;
    private OnClickItemListener mOnClickItemListener;

    public AppManagerAdapter(Context context, List<GroupItemAppManager> items,
                             OnClickItemListener onClickItemListener) {
        inflater = LayoutInflater.from(context);
        this.items = items;
        mOnClickItemListener = onClickItemListener;
        mPackageManager = context.getPackageManager();
    }

    @Override
    public ApplicationInfo getChild(int groupPosition, int childPosition) {
        return items.get(groupPosition).getItems().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getRealChildView(final int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildHolder holder;
        ApplicationInfo item = getChild(groupPosition, childPosition);
        if (convertView == null) {
            holder = new ChildHolder();
            convertView = inflater.inflate(R.layout.item_app_manager, parent, false);
            holder.tvName = convertView.findViewById(R.id.tvName);
            holder.tvName.setSelected(true);
            holder.tvSize = convertView.findViewById(R.id.tvSize);
            holder.imgIconApp = convertView.findViewById(R.id.imgIconApp);
            holder.btnUninstall = convertView.findViewById(R.id.btnUninstall);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }

        holder.tvName.setText(item.loadLabel(mPackageManager));
        File file = new File(item.publicSourceDir);
        holder.tvSize.setText(Toolbox.formatSize(file.length()));

        holder.imgIconApp.setImageDrawable(item.loadIcon(mPackageManager));
        if (items.get(groupPosition).getType() == GroupItemAppManager.TYPE_USER_APPS) {
            holder.btnUninstall.setVisibility(View.VISIBLE);
        } else {
            holder.btnUninstall.setVisibility(View.GONE);
        }
        holder.btnUninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickItemListener.onUninstallApp(groupPosition, childPosition);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickItemListener.onClickItem(groupPosition, childPosition);
            }
        });
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return items.get(groupPosition).getItems().size();
    }

    @Override
    public GroupItemAppManager getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupHolder holder;
        GroupItemAppManager item = getGroup(groupPosition);
        if (convertView == null) {
            holder = new GroupHolder();
            convertView = inflater.inflate(R.layout.item_header_app_manager, parent, false);
            holder.tvNameHeaderAppManager = convertView.findViewById(R.id.tvNameHeaderAppManager);
            holder.tvHeaderSizeAppManager = convertView.findViewById(R.id.tvHeaderSizeAppManager);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        holder.tvNameHeaderAppManager.setText(item.getTitle());
        holder.tvHeaderSizeAppManager.setText(String.valueOf(item.getTotal()));
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public interface OnClickItemListener {
        void onUninstallApp(int groupPosition, int childPosition);

        void onClickItem(int groupPosition, int childPosition);
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }

    private static class ChildHolder {
        private TextView tvName;
        private TextView tvSize;
        private ImageView imgIconApp;
        private TextView btnUninstall;
    }

    private static class GroupHolder {
        private TextView tvNameHeaderAppManager;
        private TextView tvHeaderSizeAppManager;
    }
}
