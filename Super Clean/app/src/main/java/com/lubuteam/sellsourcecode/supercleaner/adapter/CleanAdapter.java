package com.lubuteam.sellsourcecode.supercleaner.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.model.ChildItem;
import com.lubuteam.sellsourcecode.supercleaner.model.GroupItem;
import com.lubuteam.sellsourcecode.supercleaner.widget.AnimatedExpandableListView;
import com.testapp.duplicatefileremover.utilts.Utils;

import java.util.List;

public class CleanAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

    private final LayoutInflater inflater;
    private final Context mContext;
    private final List<GroupItem> items;
    private final OnGroupClickListener mOnGroupClickListener;

    private final Drawable icAndroid;
    private final Drawable icFileDownload;
    private final Drawable icDescription;

    public CleanAdapter(Context context, List<GroupItem> items, OnGroupClickListener onGroupClickListener) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        this.items = items;
        mOnGroupClickListener = onGroupClickListener;

        icAndroid = ContextCompat.getDrawable(mContext, R.drawable.ic_android);
        icFileDownload = ContextCompat.getDrawable(mContext, R.drawable.ic_file_download);
        icDescription = ContextCompat.getDrawable(mContext, R.drawable.ic_description);
    }

    @Override
    public ChildItem getChild(int groupPosition, int childPosition) {
        return items.get(groupPosition).getItems().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getRealChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildHolder holder;
        ChildItem item = getChild(groupPosition, childPosition);
        if (convertView == null) {
            holder = new ChildHolder();
            convertView = inflater.inflate(R.layout.item_junk, parent, false);
            holder.tvName = convertView.findViewById(R.id.tvName);
            holder.tvName.setSelected(true);
            holder.tvSize = convertView.findViewById(R.id.tvSize);
            holder.imgIconApp = convertView.findViewById(R.id.imgIconApp);
            holder.viewIconFile = convertView.findViewById(R.id.viewIconFile);
            holder.imgFileApk = convertView.findViewById(R.id.imgFileApk);
            holder.ckItem = convertView.findViewById(R.id.ckItem);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }

        holder.tvName.setText(item.getApplicationName());
        holder.tvSize.setText(Utils.formatSize(item.getCacheSize()));
        holder.ckItem.setChecked(item.isCheck());
        holder.ckItem.setOnClickListener(v -> mOnGroupClickListener.onSelectItem(groupPosition, childPosition, holder.ckItem.isChecked()));
        switch (item.getType()) {
            case ChildItem.TYPE_APKS:
                holder.viewIconFile.setVisibility(View.VISIBLE);
//                holder.imgIconApp.setVisibility(View.GONE);
                holder.imgFileApk.setVisibility(View.VISIBLE);
                holder.imgIconApp.setVisibility(View.INVISIBLE);
//                holder.imgFileApk.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_android));
                holder.imgFileApk.setImageDrawable(icAndroid);
                holder.ckItem.setVisibility(View.VISIBLE);
                break;
            case ChildItem.TYPE_DOWNLOAD_FILE:
                holder.viewIconFile.setVisibility(View.VISIBLE);
//                holder.imgIconApp.setVisibility(View.GONE);
                holder.imgFileApk.setVisibility(View.VISIBLE);
                holder.imgIconApp.setVisibility(View.INVISIBLE);
//                holder.imgFileApk.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_file_download));
                holder.imgFileApk.setImageDrawable(icFileDownload);
                holder.ckItem.setVisibility(View.VISIBLE);
                break;
            case ChildItem.TYPE_LARGE_FILES:
                holder.viewIconFile.setVisibility(View.VISIBLE);
//                holder.imgIconApp.setVisibility(View.GONE);
                holder.imgFileApk.setVisibility(View.VISIBLE);
                holder.imgIconApp.setVisibility(View.INVISIBLE);
//                holder.imgFileApk.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_description));
                holder.imgFileApk.setImageDrawable(icDescription);
                holder.ckItem.setVisibility(View.VISIBLE);
                break;
            case ChildItem.TYPE_CACHE:
                holder.viewIconFile.setVisibility(View.VISIBLE);
                holder.imgFileApk.setVisibility(View.INVISIBLE);
                holder.imgIconApp.setVisibility(View.VISIBLE);
                holder.imgIconApp.setImageDrawable(item.getApplicationIcon());
                holder.ckItem.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return items.get(groupPosition).getItems().size();
    }

    @Override
    public GroupItem getGroup(int groupPosition) {
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
        GroupItem item = getGroup(groupPosition);
        if (convertView == null) {
            holder = new GroupHolder();
            convertView = inflater.inflate(R.layout.item_junk_header, parent, false);
            holder.tvHeaderSize = convertView.findViewById(R.id.tvHeaderSize);
            holder.tvName = convertView.findViewById(R.id.item_header_name);
            holder.ckHeader = convertView.findViewById(R.id.ckHeader);
            convertView.setOnClickListener(v -> mOnGroupClickListener.onGroupClick(groupPosition));
            holder.ckHeader.setOnClickListener(v -> mOnGroupClickListener.onSelectItemHeader(groupPosition, holder.ckHeader.isChecked()));
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }

        holder.tvName.setText(item.getTitle());
        holder.tvHeaderSize.setText(Utils.formatSize(item.getTotal()));
        holder.ckHeader.setChecked(item.isCheck());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public interface OnGroupClickListener {
        void onGroupClick(int groupPosition);

        void onSelectItemHeader(int position, boolean isCheck);

        void onSelectItem(int groupPosition, int childPosition, boolean isCheck);
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }

    private static class ChildHolder {
        private TextView tvName;
        private TextView tvSize;
        private ImageView imgIconApp;
        private RelativeLayout viewIconFile;
        private ImageView imgFileApk;
        private CheckBox ckItem;
    }

    private static class GroupHolder {
        private TextView tvName;
        private TextView tvHeaderSize;
        private CheckBox ckHeader;
    }
}
