package com.security.applock.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.security.applock.R;
import com.security.applock.databinding.LayoutMenuFunctionBinding;

public class MenuFunction extends LinearLayout {

    private LayoutMenuFunctionBinding binding;
    private ItemClickListener itemClickListener;

    private ActionListener actionListener;

    public interface ActionListener {
        void onCheckedListener(boolean isChecked);
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public interface ItemClickListener {
        void OnItemClickListener();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public MenuFunction(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        binding = LayoutMenuFunctionBinding.inflate(LayoutInflater.from(context), this, true);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        TypedArray attrArr = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.MenuFunction, 0, 0);
        String title = attrArr.getString(R.styleable.MenuFunction_inf_title);
        if (!TextUtils.isEmpty(title))
            binding.tvTitle.setText(title);

        String description = attrArr.getString(R.styleable.MenuFunction_inf_description);
        if (!TextUtils.isEmpty(description)) {
            binding.tvDescripsion.setText(description);
            binding.tvDescripsion.setVisibility(VISIBLE);
        }


        Drawable icon = attrArr.getDrawable(R.styleable.MenuFunction_inf_icon);
        if (icon != null) {
            binding.imIcon.setImageDrawable(icon);
        }

        Drawable icon2 = attrArr.getDrawable(R.styleable.MenuFunction_inf_icon_2);
        if (icon2 != null) {
            binding.imIcon2.setImageDrawable(icon2);
            binding.imIcon2.setVisibility(VISIBLE);
            binding.imIcon.setVisibility(GONE);
        }

        Drawable iconSub = attrArr.getDrawable(R.styleable.MenuFunction_inf_icon_sub);
        if (iconSub != null) {
            binding.imIconSub.setImageDrawable(iconSub);
            binding.imIconSub.setVisibility(VISIBLE);
        }

        boolean isShowIconSub = attrArr.getBoolean(R.styleable.MenuFunction_inf_show_icon_subcription, false);
        binding.imIconSub.setVisibility(isShowIconSub ? VISIBLE : GONE);

        boolean isShowIcon = attrArr.getBoolean(R.styleable.MenuFunction_inf_show_icon, true);
        binding.llIcon.setVisibility(isShowIcon ? VISIBLE : GONE);

        boolean isShowSwitchCheck = attrArr.getBoolean(R.styleable.MenuFunction_inf_show_switch_check, false);
        binding.swSelect.setVisibility(isShowSwitchCheck ? VISIBLE : GONE);

        initControl();
    }

    private void initControl() {
        binding.container.setOnClickListener(v -> {
            if (itemClickListener != null)
                itemClickListener.OnItemClickListener();
        });

//        binding.swSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (actionListener != null)
//                actionListener.onCheckedListener(isChecked);
//        });
        binding.swSelect.setOnClickListener(v -> {
            if (actionListener != null)
                actionListener.onCheckedListener(binding.swSelect.isChecked());
        });
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title))
            binding.tvTitle.setText(title);
    }

    public void setDesciption(String desciption) {
        if (!TextUtils.isEmpty(desciption))
            binding.tvDescripsion.setText(desciption);
    }

    public void setTintIcon2(int color) {
        binding.imIcon2.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    public void setIconSub(int icon) {
        binding.imIconSub.setImageResource(icon);
        binding.imIconSub.setVisibility(VISIBLE);
    }

    public void setSwChecked(boolean isChecked) {
        binding.swSelect.setChecked(isChecked);
    }

    public boolean getSwChecked() {
        return binding.swSelect.isChecked();
    }

    public void setSwitchEnable(boolean isEnable) {
        setEnabled(isEnable);
        setAlpha(isEnable ? 1.0f : 0.3f);
        binding.swSelect.setEnabled(isEnable);
    }


}
