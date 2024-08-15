package com.tkt.spin_wheel.dialog.winner;

import android.content.Context;

import com.tkt.spin_wheel.base.BaseDialog;
import com.tkt.spin_wheel.databinding.DialogDeleteBinding;
import com.tkt.spin_wheel.databinding.DialogWinnerBinding;
import com.tkt.spin_wheel.ui.spin.SpinningWheelActivity;

public class WinnerDialog extends BaseDialog<DialogWinnerBinding> {
    IClickDialogWinner iBaseListener;
    Context context;

    public WinnerDialog(Context context, Boolean cancelAble) {
        super(context, cancelAble);
        this.context = context;
    }


    @Override
    protected DialogWinnerBinding setBinding() {
        return DialogWinnerBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        binding.ivColorWin.setBackgroundColor(SpinningWheelActivity.winningColor);
        binding.tvWin.setText(SpinningWheelActivity.winningItem);
    }

    @Override
    protected void bindView() {
        binding.clHide.setOnClickListener(view -> iBaseListener.hide());

        binding.clOkay.setOnClickListener(view -> iBaseListener.okay());

    }

    public void init(IClickDialogWinner iBaseListener) {
        this.iBaseListener = iBaseListener;
    }

}
