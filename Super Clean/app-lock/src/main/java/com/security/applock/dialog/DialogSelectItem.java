package com.security.applock.dialog;

import android.view.LayoutInflater;
import android.widget.TextView;

import com.security.applock.adapter.SelectItemAdapter;
import com.security.applock.databinding.DialogSelectItemBinding;
import com.security.applock.model.SelectModel;

import java.util.Arrays;
import java.util.HashMap;

public class DialogSelectItem extends BaseDialog<DialogSelectItemBinding, DialogSelectItem.ExtendBuilder> {

    public static final String ITEM_SAVE = "item save";
    private final ExtendBuilder extendBuilder;
    private SelectItemAdapter selectItemAdapter;

    public DialogSelectItem(DialogSelectItem.ExtendBuilder builder) {
        super(builder);
        this.extendBuilder = builder;
    }

    @Override
    protected DialogSelectItemBinding getViewBinding() {
        return DialogSelectItemBinding.inflate(LayoutInflater.from(getContext()));
    }

    @Override
    protected TextView getTitle() {
        return binding.tvTitle;
    }

    @Override
    protected TextView getPositiveButton() {
        return binding.tvPositive;
    }

    @Override
    protected void initView() {
        super.initView();
        selectItemAdapter = new SelectItemAdapter(getContext(), Arrays.asList(extendBuilder.lstData));
        selectItemAdapter.setItemSelected(extendBuilder.idDefault);
        binding.rcvData.setAdapter(selectItemAdapter);
    }

    @Override
    protected void initControl() {
        selectItemAdapter.setOnItemClickListener(position -> {
            if (extendBuilder.itemClickListener != null)
                extendBuilder.itemClickListener.onItemClickListener(position);
        });

        binding.imClose.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onDestroy() {
        if (extendBuilder.dismissDialogListener != null)
            extendBuilder.dismissDialogListener.onDismissDialogListner();
        super.onDestroy();
    }

    @Override
    protected void handleClickPositiveButton(HashMap<String, Object> datas) {
        datas.put(ITEM_SAVE, selectItemAdapter.getItemSelected());
        super.handleClickPositiveButton(datas);
    }

    public static class ExtendBuilder extends BuilderDialog {

        private SelectModel[] lstData;
        private int idDefault;
        private ItemClickListener itemClickListener;

        public interface ItemClickListener {
            void onItemClickListener(int position);
        }

        @Override
        public BaseDialog build() {
            return new DialogSelectItem(this);
        }

        public ExtendBuilder setLstData(SelectModel[] lstData) {
            this.lstData = lstData;
            return this;
        }

        public ExtendBuilder setIdDefault(int idDefault) {
            this.idDefault = idDefault;
            return this;
        }

        public ExtendBuilder setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
            return this;
        }
    }
}
