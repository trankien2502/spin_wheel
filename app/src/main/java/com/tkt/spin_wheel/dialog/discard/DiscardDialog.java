package com.tkt.spin_wheel.dialog.discard;

import android.content.Context;

import com.tkt.spin_wheel.base.BaseDialog;
import com.tkt.spin_wheel.databinding.DialogDeleteBinding;
import com.tkt.spin_wheel.databinding.DialogDiscardBinding;

public class DiscardDialog extends BaseDialog<DialogDiscardBinding> {
    IClickDialogDiscard iBaseListener;
    Context context;

    public DiscardDialog(Context context, Boolean cancelAble) {
        super(context, cancelAble);
        this.context = context;
    }


    @Override
    protected DialogDiscardBinding setBinding() {
        return DialogDiscardBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void bindView() {
        binding.clKeep.setOnClickListener(view -> iBaseListener.keep());

        binding.clDiscard.setOnClickListener(view -> iBaseListener.discard());

    }

    public void init(IClickDialogDiscard iBaseListener) {
        this.iBaseListener = iBaseListener;
    }

}
