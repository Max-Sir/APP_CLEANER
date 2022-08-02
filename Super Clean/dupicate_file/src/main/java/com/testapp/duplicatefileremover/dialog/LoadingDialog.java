package com.testapp.duplicatefileremover.dialog;

import android.app.Dialog;
import android.content.Context;

import com.testapp.duplicatefileremover.R;

public class LoadingDialog extends Dialog {
    private Context mContext;

    public LoadingDialog(Context activity) {
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        this.mContext = activity;
        requestWindowFeature(1);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.layout_loading_dialog_dupicate);

    }
}