package com.tkt.spin_wheel.dialog.delete;

import android.content.Context;

import com.tkt.spin_wheel.base.BaseDialog;
import com.tkt.spin_wheel.databinding.DialogCameraAccessBinding;
import com.tkt.spin_wheel.databinding.DialogDeleteBinding;

public class DeleteDialog extends BaseDialog<DialogDeleteBinding> {
    IClickDialogDelete iBaseListener;
    Context context;

    public DeleteDialog(Context context, Boolean cancelAble) {
        super(context, cancelAble);
        this.context = context;
    }


    @Override
    protected DialogDeleteBinding setBinding() {
        return DialogDeleteBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void bindView() {
        binding.clCancel.setOnClickListener(view -> iBaseListener.cancel());

        binding.clOkay.setOnClickListener(view -> iBaseListener.okay());

    }

    public void init(IClickDialogDelete iBaseListener) {
        this.iBaseListener = iBaseListener;
    }

}
