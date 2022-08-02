package com.lubuteam.sellsourcecode.supercleaner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.utils.Config;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.ViewHolder> {

    private Config.FUNCTION[] lstFunction;
    private Config.TYPE_DISPLAY_ADAPTER typeDisplay;
    private Context mContext;
    private ClickItemListener mClickItemListener;


    public interface ClickItemListener {
        void itemSelected(Config.FUNCTION mFunction);
    }

    public FunctionAdapter(Config.FUNCTION[] lstFunction, Config.TYPE_DISPLAY_ADAPTER typeDisplay) {
        this.lstFunction = lstFunction;
        this.typeDisplay = typeDisplay;
    }

    public void setmClickItemListener(ClickItemListener mClickItemListener) {
        this.mClickItemListener = mClickItemListener;
    }

    @NonNull
    @Override
    public FunctionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
        View mView = null;
        if (typeDisplay == Config.TYPE_DISPLAY_ADAPTER.HORIZOLTAL) {
            mView = mLayoutInflater.inflate(R.layout.item_function_horizontal, parent, false);
        } else if (typeDisplay == Config.TYPE_DISPLAY_ADAPTER.VERTICAL) {
            mView = mLayoutInflater.inflate(R.layout.item_function_vertical, parent, false);
        } else if (typeDisplay == Config.TYPE_DISPLAY_ADAPTER.SUGGEST) {
            mView = mLayoutInflater.inflate(R.layout.item_function_suggest, parent, false);
        }
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull FunctionAdapter.ViewHolder holder, int position) {
        holder.binData(lstFunction[position]);
    }

    @Override
    public int getItemCount() {
        return lstFunction.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.im_icon)
        ImageView imIcon;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @Nullable
        @BindView(R.id.tv_description)
        TextView tvDescrtion;
//        @Nullable
//        @BindView(R.id.tv_action)
//        TextView tvAction;
//        @Nullable
//        @BindView(R.id.view_suggest_left)
//        RoundedImageView imSguuestLeft;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void binData(Config.FUNCTION mFunction) {
            if (mFunction != null) {
                imIcon.setImageResource(mFunction.icon);
                tvTitle.setText(mContext.getString(mFunction.title));
                if (tvDescrtion != null)
                    tvDescrtion.setText(mContext.getString(mFunction.descrition));
//                if (tvAction != null) { /*TH item list suggest*/
//                    tvAction.setTextColor(mContext.getResources().getColor(mFunction.color));
//                    tvAction.setText(mContext.getString(mFunction.action));
//                    imSguuestLeft.setImageResource(mFunction.background);
//                    imIcon.setVisibility(View.GONE);
//                }
            }

            itemView.setOnClickListener(v -> {
                if (mClickItemListener != null)
                    mClickItemListener.itemSelected(mFunction);
            });
        }
    }
}
