package com.tkt.spin_wheel.ui.spin;

import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.tkt.spin_wheel.R;
import com.tkt.spin_wheel.base.BaseActivity;
import com.tkt.spin_wheel.database.AppDatabase;
import com.tkt.spin_wheel.databinding.ActivityAddWheelBinding;
import com.tkt.spin_wheel.dialog.add_wheel.IClickDialogWheelColor;
import com.tkt.spin_wheel.dialog.add_wheel.WheelColorDialog;
import com.tkt.spin_wheel.dialog.delete.DeleteDialog;
import com.tkt.spin_wheel.dialog.delete.IClickDialogDelete;
import com.tkt.spin_wheel.dialog.discard.DiscardDialog;
import com.tkt.spin_wheel.dialog.discard.IClickDialogDiscard;
import com.tkt.spin_wheel.dialog.edit_color.EditColorDialog;
import com.tkt.spin_wheel.dialog.edit_color.EditColorDialogAdd;
import com.tkt.spin_wheel.dialog.edit_color.IClickDialogEditColor;
import com.tkt.spin_wheel.ui.home.HomeActivity;
import com.tkt.spin_wheel.ui.spin.adapter.IonClickSectionListener;
import com.tkt.spin_wheel.ui.spin.adapter.SectionAdapter;
import com.tkt.spin_wheel.ui.spin.model.ColorEdit;
import com.tkt.spin_wheel.ui.spin.model.WheelModel;

import java.util.ArrayList;
import java.util.List;

public class AddWheelActivity extends BaseActivity<ActivityAddWheelBinding> {
    WheelModel wheelColorSelected;
    List<String> itemText = new ArrayList<>();
    List<String> itemTextEdit;
    List<Integer> colorEdit;
    public static List<ColorEdit> colorAll;
    int numberOfItems;
    int idColor,idColorSelect;
    public static int idColorEdit;
    public static String textEdit;
    IonClickSectionListener ionClickSectionListener;
    @Override
    public ActivityAddWheelBinding getBinding() {
        return ActivityAddWheelBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        initData();
        idColor = getIntent().getIntExtra("NEW_WHEEL_COLOR_ID",1);
        Toast.makeText(this, "color: "+idColor, Toast.LENGTH_SHORT).show();
        binding.header.tvTitle.setText(R.string.add_wheel);
        switch (idColor){
            case 1:
                wheelColorSelected = AppDatabase.getInstance(this).wheelDAO().findByName("Standard__");
                binding.tvTypeColor.setText(R.string.standard);
                break;
            case 2:
                wheelColorSelected = AppDatabase.getInstance(this).wheelDAO().findByName("Six_Color_");
                binding.tvTypeColor.setText(R.string.six_color);
                break;
            case 3:
                wheelColorSelected = AppDatabase.getInstance(this).wheelDAO().findByName("Vintage__");
                binding.tvTypeColor.setText(R.string.vintage);
                break;
            case 4:
                wheelColorSelected = AppDatabase.getInstance(this).wheelDAO().findByName("Two_Color_");
                binding.tvTypeColor.setText(R.string.two_color);
                break;
            case 5:
                wheelColorSelected = AppDatabase.getInstance(this).wheelDAO().findByName("Pastel__");
                binding.tvTypeColor.setText(R.string.pastel);
                break;
        }
        numberOfItems = wheelColorSelected.getNumberOfItems();
        for (int i=0;i<numberOfItems;i++){
            itemText.add("Option "+i);
        }
        wheelColorSelected.setItemTexts(itemText);
        colorEdit = wheelColorSelected.getItemColor();
        itemTextEdit = wheelColorSelected.getItemTexts();

        binding.wheel.setNumberOfItems(wheelColorSelected.getNumberOfItems());
        binding.wheel.setRepeatOption(wheelColorSelected.getRepeatOption());
        binding.wheel.setTextSizeItem(wheelColorSelected.getFontSize());

        binding.wheel.setColors(colorEdit);
        binding.wheel.setTextItems(itemTextEdit);

        int max = (int) (24/wheelColorSelected.getNumberOfItems());
        binding.sbRepeat.setMax(max);

        binding.tvFontSizeEdit.setText(String.valueOf(wheelColorSelected.getFontSize()));
        binding.tvRepeatOptionEdit.setText(String.valueOf(wheelColorSelected.getRepeatOption()));
        binding.tvSpinTimeEdit.setText(String.valueOf(wheelColorSelected.getSpinSpeed()));

        binding.sbFontSize.setProgress(wheelColorSelected.getFontSize());
        binding.sbRepeat.setProgress(wheelColorSelected.getRepeatOption());
        binding.sbSpinTime.setProgress(wheelColorSelected.getSpinSpeed());


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rcvSection.setLayoutManager(layoutManager);
        ionClickSectionListener = new IonClickSectionListener() {
            @Override
            public void delete(int i) {
                wheelColorSelected.getItemTexts().remove(i);
                int minusNum = wheelColorSelected.getNumberOfItems();
                wheelColorSelected.setNumberOfItems(minusNum-1);
                binding.wheel.setNumberOfItems(wheelColorSelected.getNumberOfItems());

                binding.wheel.setTextItems(wheelColorSelected.getItemTexts());
                if (wheelColorSelected.getNumberOfItems()>0){
                    int max = (int) (24/wheelColorSelected.getNumberOfItems());
                    binding.sbRepeat.setMax(max);

                    if (wheelColorSelected.getRepeatOption()>max){
                        wheelColorSelected.setRepeatOption(max);
                        binding.wheel.setRepeatOption(max);
                    }
                }
                SectionAdapter sectionAdapter = new SectionAdapter(getBaseContext(),wheelColorSelected.getItemTexts(),wheelColorSelected.getItemColor(),ionClickSectionListener);
                binding.rcvSection.setAdapter(sectionAdapter);
                //Toast.makeText(AddWheelActivity.this, "Edit success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void editText(int i, String str) {
                if (str.equals("")){
                    Toast.makeText(AddWheelActivity.this, "Fail! Empty section name!", Toast.LENGTH_SHORT).show();
                    str = "Option";
                }
                wheelColorSelected.getItemTexts().set(i,str);
                binding.wheel.setTextItems(wheelColorSelected.getItemTexts());
                SectionAdapter sectionAdapter = new SectionAdapter(getBaseContext(),wheelColorSelected.getItemTexts(),wheelColorSelected.getItemColor(),ionClickSectionListener);
                binding.rcvSection.setAdapter(sectionAdapter);
                //Toast.makeText(AddWheelActivity.this, "Edit success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void editColor(int postion) {
                editColorSection(postion);
                Toast.makeText(AddWheelActivity.this, "edit Color", Toast.LENGTH_SHORT).show();
            }
        };
        SectionAdapter sectionAdapter = new SectionAdapter(this,wheelColorSelected.getItemTexts(),wheelColorSelected.getItemColor(),ionClickSectionListener);
        binding.rcvSection.setAdapter(sectionAdapter);
    }

    @Override
    public void bindView() {
        binding.header.ivBack.setOnClickListener(view -> onBackPressed());
        binding.sbSpinTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.tvSpinTimeEdit.setText(String.valueOf(i));
                wheelColorSelected.setSpinSpeed(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.sbFontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.tvFontSizeEdit.setText(String.valueOf(i));
                binding.wheel.setTextSizeItem(i);
                wheelColorSelected.setFontSize(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.sbRepeat.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.tvRepeatOptionEdit.setText(String.valueOf(i));
                binding.wheel.setRepeatOption(i);
                wheelColorSelected.setRepeatOption(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.btnWheelColor.setOnClickListener(view -> addColor());
        binding.ivAddSection.setOnClickListener(view -> {
            if (wheelColorSelected.getNumberOfItems()>=24){
                Toast.makeText(this, "maximum of section: 24. ", Toast.LENGTH_SHORT).show();
                return;
            }
            wheelColorSelected.getItemTexts().add("Option"+wheelColorSelected.getNumberOfItems());
            binding.wheel.setTextItems(wheelColorSelected.getItemTexts());

            int plusNum = wheelColorSelected.getNumberOfItems();
            wheelColorSelected.setNumberOfItems(plusNum+1);
            binding.wheel.setNumberOfItems(plusNum+1);

            int max = (int) (24/wheelColorSelected.getNumberOfItems());
            binding.sbRepeat.setMax(max);

            if (wheelColorSelected.getRepeatOption()>max){
                wheelColorSelected.setRepeatOption(max);
                binding.wheel.setRepeatOption(max);
            }
            SectionAdapter sectionAdapter = new SectionAdapter(this,wheelColorSelected.getItemTexts(),wheelColorSelected.getItemColor(),ionClickSectionListener);
            binding.rcvSection.setAdapter(sectionAdapter);

        });
        binding.header.ivGone.setOnClickListener(view -> {
            if (checkData()){
                wheelColorSelected.setActive(true);
                wheelColorSelected.setName(binding.edtContent.getText().toString().trim());
                //AppDatabase.getInstance(this).wheelDAO().insertAll(wheelColorSelected);
                AppDatabase.getInstance(this).wheelDAO().insertAll(new WheelModel(
                        wheelColorSelected.getName(),
                        wheelColorSelected.getNumberOfItems(),
                        idColor,
                        wheelColorSelected.getFontSize(),
                        wheelColorSelected.getSpinSpeed(),
                        wheelColorSelected.getRepeatOption(),
                        0,0,
                        wheelColorSelected.getItemTexts(),
                        wheelColorSelected.getItemColor(), true
                ));
                startNextActivity(HomeActivity.class,null);
                finishAffinity();
            }
        });
    }
    private void disCardChanges(){
        DiscardDialog dialog = new DiscardDialog(this,false);
        dialog.init(new IClickDialogDiscard() {
            @Override
            public void keep() {
                dialog.dismiss();
            }

            @Override
            public void discard() {
                finish();
                dialog.dismiss();
            }
        });
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void editColorSection(int position){
        idColorEdit = wheelColorSelected.getItemColor().get(position%wheelColorSelected.getItemColor().size());
        textEdit = wheelColorSelected.getItemTexts().get(position);
        EditColorDialogAdd editColorDialog = new EditColorDialogAdd(AddWheelActivity.this,true);
        editColorDialog.init(new IClickDialogEditColor() {
            @Override
            public void cancel() {
                editColorDialog.dismiss();
            }

            @Override
            public void okay() {
                textEdit = editColorDialog.binding.edtContent.getText().toString().trim();
                if (textEdit==null){
                    Toast.makeText(AddWheelActivity.this, "empty name section", Toast.LENGTH_SHORT).show();
                    return;
                }
                wheelColorSelected.getItemTexts().set(position,textEdit);
                binding.wheel.setTextItems(wheelColorSelected.getItemTexts());

                wheelColorSelected.getItemColor().set(position%wheelColorSelected.getItemColor().size(),idColorEdit);
                binding.wheel.setColors(wheelColorSelected.getItemColor());

                SectionAdapter sectionAdapter = new SectionAdapter(getBaseContext(),wheelColorSelected.getItemTexts(),wheelColorSelected.getItemColor(),ionClickSectionListener);
                binding.rcvSection.setAdapter(sectionAdapter);
                editColorDialog.dismiss();
            }

//            @Override
//            public void editText(String str) {
//                textEdit = str;
//            }

            @Override
            public void editColor(int i) {
                idColorEdit = i;
            }
        });
        try {
            editColorDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addColor() {
        WheelColorDialog wheelColorDialog = new WheelColorDialog(this, true);
        wheelColorDialog.binding.tvTitle.setText(R.string.choose_wheel_color);
        wheelColorDialog.binding.rdoStandard.setImageResource(R.drawable.img_wheel_color_unselect);
        wheelColorDialog.binding.rdoSixColor.setImageResource(R.drawable.img_wheel_color_unselect);
        wheelColorDialog.binding.rdoVintage.setImageResource(R.drawable.img_wheel_color_unselect);
        wheelColorDialog.binding.rdoTwoColor.setImageResource(R.drawable.img_wheel_color_unselect);
        wheelColorDialog.binding.rdoPastel.setImageResource(R.drawable.img_wheel_color_unselect);
        idColorSelect = idColor;
        switch (idColor){
            case 1:
                wheelColorDialog.binding.rdoStandard.setImageResource(R.drawable.img_wheel_color_select);
                break;
            case 2:
                wheelColorDialog.binding.rdoSixColor.setImageResource(R.drawable.img_wheel_color_select);
                break;
            case 3:
                wheelColorDialog.binding.rdoVintage.setImageResource(R.drawable.img_wheel_color_select);
                break;
            case 4:
                wheelColorDialog.binding.rdoTwoColor.setImageResource(R.drawable.img_wheel_color_select);
                break;
            case 5:
                wheelColorDialog.binding.rdoPastel.setImageResource(R.drawable.img_wheel_color_select);
                break;
        }
        wheelColorDialog.init(new IClickDialogWheelColor() {
            @Override
            public void cancel() {
                wheelColorDialog.dismiss();
            }

            @Override
            public void okay() {
                idColor = idColorSelect;
                switch (idColor){
                    case 1:
                        colorEdit = AppDatabase.getInstance(getBaseContext()).wheelDAO().findByName("Standard__").getItemColor();
                        binding.tvTypeColor.setText(R.string.standard);
                        break;
                    case 2:
                        colorEdit = AppDatabase.getInstance(getBaseContext()).wheelDAO().findByName("Six_Color_").getItemColor();
                        binding.tvTypeColor.setText(R.string.six_color);
                        break;
                    case 3:
                        colorEdit = AppDatabase.getInstance(getBaseContext()).wheelDAO().findByName("Vintage__").getItemColor();
                        binding.tvTypeColor.setText(R.string.vintage);
                        break;
                    case 4:
                        colorEdit = AppDatabase.getInstance(getBaseContext()).wheelDAO().findByName("Two_Color_").getItemColor();
                        binding.tvTypeColor.setText(R.string.two_color);
                        break;
                    case 5:
                        colorEdit = AppDatabase.getInstance(getBaseContext()).wheelDAO().findByName("Pastel__").getItemColor();
                        binding.tvTypeColor.setText(R.string.pastel);
                        break;
                }
                wheelColorSelected.setItemColor(colorEdit);
                binding.wheel.setColors(colorEdit);
                SectionAdapter sectionAdapter = new SectionAdapter(getBaseContext(),wheelColorSelected.getItemTexts(),wheelColorSelected.getItemColor(),ionClickSectionListener);
                binding.rcvSection.setAdapter(sectionAdapter);
                wheelColorDialog.dismiss();
                //finish();
            }

            @Override
            public int color(int i) {
                idColorSelect = i;
                wheelColorDialog.binding.rdoStandard.setImageResource(R.drawable.img_wheel_color_unselect);
                wheelColorDialog.binding.rdoSixColor.setImageResource(R.drawable.img_wheel_color_unselect);
                wheelColorDialog.binding.rdoVintage.setImageResource(R.drawable.img_wheel_color_unselect);
                wheelColorDialog.binding.rdoTwoColor.setImageResource(R.drawable.img_wheel_color_unselect);
                wheelColorDialog.binding.rdoPastel.setImageResource(R.drawable.img_wheel_color_unselect);
                switch (i){
                    case 1:
                        wheelColorDialog.binding.rdoStandard.setImageResource(R.drawable.img_wheel_color_select);
                        break;
                    case 2:
                        wheelColorDialog.binding.rdoSixColor.setImageResource(R.drawable.img_wheel_color_select);
                        break;
                    case 3:
                        wheelColorDialog.binding.rdoVintage.setImageResource(R.drawable.img_wheel_color_select);
                        break;
                    case 4:
                        wheelColorDialog.binding.rdoTwoColor.setImageResource(R.drawable.img_wheel_color_select);
                        break;
                    case 5:
                        wheelColorDialog.binding.rdoPastel.setImageResource(R.drawable.img_wheel_color_select);
                        break;
                }
                return i;
            }
        });

        try {
            wheelColorDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkData(){
        if (binding.edtContent.getText().toString().trim().equals("")) {
            Toast.makeText(this, "name wheel", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (wheelColorSelected.getItemTexts().size()<2) {
            Toast.makeText(this, "create at least 2 section", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void initData() {
        colorAll = new ArrayList<>();
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_0),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_1),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_2),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_3),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_4),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_5),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_6),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_7),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_8),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_9),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_10),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_11),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_12),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_13),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_14),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_15),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_16),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_17),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_18),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_19),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_20),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_21),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_22),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_23),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_24),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_25),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_26),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_27),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_28),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_29),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_30),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_31),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_32),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_33),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_34),false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_35),false));
        Log.d("spinCheck","color: "+colorAll.toString());
    }


    @Override
    public void onBackPressed() {
        disCardChanges();
    }
}