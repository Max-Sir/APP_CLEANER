package com.lubuteam.sellsourcecode.supercleaner.dialog;


public abstract class BuilderDialog2 {

    protected String title;
    protected boolean cancelable = true;
    protected boolean canOntouchOutside = true;

    protected DialogActionListener.DismissDialogListener dismissDialogListener;

    public BuilderDialog2 setTitle(String title) {
        this.title = title;
        return this;
    }

    public BuilderDialog2 setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public BuilderDialog2 setCanOntouchOutside(boolean canOntouchOutside) {
        this.canOntouchOutside = canOntouchOutside;
        return this;
    }

    public BuilderDialog2 onDismissListener(DialogActionListener.DismissDialogListener dismissDialogListener) {
        this.dismissDialogListener = dismissDialogListener;
        return this;
    }

    public abstract BaseDialog2 build();

    public interface DialogActionListener {

        interface DismissDialogListener {
            void onDismissDialogListner();
        }

    }
}
