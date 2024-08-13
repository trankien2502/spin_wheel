package com.tkt.spin_wheel.dialog.edit_color;


import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.tkt.spin_wheel.R;
import com.tkt.spin_wheel.base.BaseDialog;
import com.tkt.spin_wheel.databinding.DialogEditBinding;
import com.tkt.spin_wheel.databinding.DialogRatingAppBinding;
import com.tkt.spin_wheel.ui.spin.AddWheelActivity;
import com.tkt.spin_wheel.ui.spin.EditWheelActivity;
import com.tkt.spin_wheel.ui.spin.adapter.ColorEditAdapter;
import com.tkt.spin_wheel.ui.spin.model.ColorEdit;
import com.tkt.spin_wheel.util.SPUtils;

import java.util.ArrayList;
import java.util.List;


public class EditColorDialog extends BaseDialog<DialogEditBinding> {

    private IClickDialogEditColor iClickDialogEditColor;
    private final Context context;
    private List<ColorEdit> colorAll;
    public EditColorDialog(@NonNull Context context, Boolean cancelAble) {
        super(context, cancelAble);
        this.context = context;
    }

    @Override
    protected DialogEditBinding setBinding() {
        return DialogEditBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        String nameSection = EditWheelActivity.textEdit;
        binding.edtContent.setText(nameSection);
        colorAll = EditWheelActivity.colorAll;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,6);
        binding.rcvColor.setLayoutManager(gridLayoutManager);

        ColorEditAdapter colorEditAdapter = new ColorEditAdapter(context,colorAll,this::selectColor);
        try{
            colorEditAdapter.setCheck(EditWheelActivity.idColorEdit);
        }catch (Exception e){
            Log.d("spinCheck","is not edit wheel");
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
