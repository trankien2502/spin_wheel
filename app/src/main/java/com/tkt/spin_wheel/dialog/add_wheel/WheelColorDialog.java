package com.tkt.spin_wheel.dialog.add_wheel;

import android.content.Context;

import com.tkt.spin_wheel.base.BaseDialog;
import com.tkt.spin_wheel.database.AppDatabase;
import com.tkt.spin_wheel.databinding.DialogAddWheelColorBinding;
import com.tkt.spin_wheel.ui.spin.model.WheelModel;

public class WheelColorDialog extends BaseDialog<DialogAddWheelColorBinding> {
    IClickDialogWheelColor iBaseListener;
    Context context;

    public WheelColorDialog(Context context, Boolean cancelAble) {
        super(context, cancelAble);
        this.context = context;
    }


    @Override
    protected DialogAddWheelColorBinding setBinding() {
        return DialogAddWheelColorBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        WheelModel standard = AppDatabase.getInstance(getContext()).wheelDAO().findByName("Standard__");
        binding.wheel1.setColors(standard.getItemColor());
        binding.wheel1.setNumberOfItems(standard.getNumberOfItems());
        binding.wheel1.setRepeatOption(standard.getRepeatOption());
        binding.wheel1.setTextItems(standard.getItemTexts());
        WheelModel sixColor = AppDatabase.getInstance(getContext()).wheelDAO().findByName("Six_Color_");
        binding.wheel2.setColors(sixColor.getItemColor());
        binding.wheel2.setNumberOfItems(sixColor.getNumberOfItems());
        binding.wheel2.setRepeatOption(sixColor.getRepeatOption());
        binding.wheel2.setTextItems(sixColor.getItemTexts());
        WheelModel vintage = AppDatabase.getInstance(getContext()).wheelDAO().findByName("Vintage__");
        binding.wheel3.setColors(vintage.getItemColor());
        binding.wheel3.setNumberOfItems(vintage.getNumberOfItems());
        binding.wheel3.setRepeatOption(vintage.getRepeatOption());
        binding.wheel3.setTextItems(vintage.getItemTexts());
        WheelModel twoColor = AppDatabase.getInstance(getContext()).wheelDAO().findByName("Two_Color_");
        binding.wheel4.setColors(twoColor.getItemColor());
        binding.wheel4.setNumberOfItems(twoColor.getNumberOfItems());
        binding.wheel4.setRepeatOption(twoColor.getRepeatOption());
        binding.wheel4.setTextItems(twoColor.getItemTexts());
        WheelModel pastel = AppDatabase.getInstance(getContext()).wheelDAO().findByName("Pastel__");
        binding.wheel5.setColors(pastel.getItemColor());
        binding.wheel5.setNumberOfItems(pastel.getNumberOfItems());
        binding.wheel5.setRepeatOption(pastel.getRepeatOption());
        binding.wheel5.setTextItems(pastel.getItemTexts());

    }

    @Override
    protected void bindView() {
        binding.clCancel.setOnClickListener(view -> iBaseListener.cancel());

        binding.clOkay.setOnClickListener(view -> iBaseListener.okay());
        binding.clWheelStandard.setOnClickListener(view -> iBaseListener.color(1));
        binding.clWheelSixColor.setOnClickListener(view -> iBaseListener.color(2));
        binding.clWheelVintage.setOnClickListener(view -> iBaseListener.color(3));
        binding.clWheelTwoColor.setOnClickListener(view -> iBaseListener.color(4));
        binding.clWheelPastel.setOnClickListener(view -> iBaseListener.color(5));
    }

    public void init(IClickDialogWheelColor iBaseListener) {
        this.iBaseListener = iBaseListener;
    }

}
