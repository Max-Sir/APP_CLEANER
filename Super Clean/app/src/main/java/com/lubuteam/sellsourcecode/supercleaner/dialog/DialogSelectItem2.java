package com.lubuteam.sellsourcecode.supercleaner.dialog;

import android.view.LayoutInflater;
import android.widget.TextView;

import com.lubuteam.sellsourcecode.supercleaner.databinding.DialogSelectItem2Binding;

import java.util.Arrays;


public class DialogSelectItem2 extends BaseDialog2<DialogSelectItem2Binding, DialogSelectItem2.ExtendBuilder2> {

    private final ExtendBuilder2 extendBuilder;
    private SelectItemAdapter2 selectItemAdapter;

    public DialogSelectItem2(ExtendBuilder2 builder) {
        super(builder);
        this.extendBuilder = builder;
    }

    @Override
    protected DialogSelectItem2Binding getViewBinding() {
        return DialogSelectItem2Binding.inflate(LayoutInflater.from(getContext()));
    }

    @Override
    protected TextView getTitle() {
        return binding.tvTitle;
    }

    @Override
    protected void initView() {
        super.initView();
        selectItemAdapter = new SelectItemAdapter2(getContext(), Arrays.asList(extendBuilder.lstData));
        binding.rcvData.setAdapter(selectItemAdapter);
    }

    @Override
    protected void initControl() {
        selectItemAdapter.setOnItemClickListener(position -> {
            if (extendBuilder.itemClickListener != null) {
                extendBuilder.itemClickListener.onItemClickListener(position);
            }
            dismiss();
        });

        binding.imClose.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onDestroy() {
        if (extendBuilder.dismissDialogListener != null) {
            extendBuilder.dismissDialogListener.onDismissDialogListner();
        }
        super.onDestroy();
    }

    public static class ExtendBuilder2 extends BuilderDialog2 {

        private SelectModel2[] lstData;
        private ItemClickListener itemClickListener;

        public interface ItemClickListener {
            void onItemClickListener(int position);
        }

        @Override
        public BaseDialog2 build() {
            return new DialogSelectItem2(this);
        }

        public ExtendBuilder2 setLstData(SelectModel2[] lstData) {
            this.lstData = lstData;
            return this;
        }

        public ExtendBuilder2 setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
            return this;
        }
    }

}
