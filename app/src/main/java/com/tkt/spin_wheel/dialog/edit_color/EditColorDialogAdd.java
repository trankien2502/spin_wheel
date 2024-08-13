package com.tkt.spin_wheel.dialog.edit_color;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.tkt.spin_wheel.base.BaseDialog;
import com.tkt.spin_wheel.databinding.DialogEditBinding;
import com.tkt.spin_wheel.ui.spin.AddWheelActivity;
import com.tkt.spin_wheel.ui.spin.EditWheelActivity;
import com.tkt.spin_wheel.ui.spin.adapter.ColorEditAdapter;
import com.tkt.spin_wheel.ui.spin.model.ColorEdit;

import java.util.List;


public class EditColorDialogAdd extends BaseDialog<DialogEditBinding> {

    private IClickDialogEditColor iClickDialogEditColor;
    private final Context context;
    private List<ColorEdit> colorAll;
    public EditColorDialogAdd(@NonNull Context context, Boolean cancelAble) {
        super(context, cancelAble);
        this.context = context;
    }

    @Override
    protected DialogEditBinding setBinding() {
        return DialogEditBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        String nameSection = AddWheelActivity.textEdit;
        binding.edtContent.setText(nameSection);
        colorAll = AddWheelActivity.colorAll;
        if (colorAll==null) colorAll = EditWheelActivity.colorAll;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,6);
        binding.rcvColor.setLayoutManager(gridLayoutManager);

        ColorEditAdapter colorEditAdapter = new ColorEditAdapter(context,colorAll,this::selectColor);
        try{
            colorEditAdapter.setCheck(AddWheelActivity.idColorEdit);
        }catch (Exception e){
            Log.d("spinCheck","is not add wheel");
        }
        binding.rcvColor.setAdapter(colorEditAdapter);

    }

    private void selectColor(ColorEdit colorEdit) {
        iClickDialogEditColor.editColor(colorEdit.getIdColor());
    }



    @Override
    protected void bindView() {
        binding.clOkay.setOnClickListener(view -> iClickDialogEditColor.okay());
        binding.clCancel.setOnClickListener(view -> iClickDialogEditColor.cancel());
    }

    public void init(IClickDialogEditColor iClickDialogEditColor) {
        this.iClickDialogEditColor = iClickDialogEditColor;
    }



}
