package com.tkt.spin_wheel.ui.spin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.tkt.spin_wheel.R;
import com.tkt.spin_wheel.base.BaseActivity;
import com.tkt.spin_wheel.database.AppDatabase;
import com.tkt.spin_wheel.databinding.ActivityAddWheelBinding;
import com.tkt.spin_wheel.dialog.add_wheel.IClickDialogWheelColor;
import com.tkt.spin_wheel.dialog.add_wheel.WheelColorDialog;
import com.tkt.spin_wheel.dialog.discard.DiscardDialog;
import com.tkt.spin_wheel.dialog.discard.IClickDialogDiscard;
import com.tkt.spin_wheel.dialog.edit_color.EditColorDialogAdd;
import com.tkt.spin_wheel.dialog.edit_color.IClickDialogEditColor;
import com.tkt.spin_wheel.ui.spin.adapter.IonClickSectionListener;
import com.tkt.spin_wheel.ui.spin.adapter.SectionAdapter;
import com.tkt.spin_wheel.ui.spin.model.ColorEdit;
import com.tkt.spin_wheel.ui.spin.model.WheelModel;
import com.tkt.spin_wheel.util.EventTracking;

import java.util.ArrayList;
import java.util.List;

public class AddWheelActivity extends BaseActivity<ActivityAddWheelBinding> {
    WheelModel wheelColorSelected;
    List<String> itemText = new ArrayList<>();
    List<String> itemTextEdit;
    List<Integer> colorEdit, colorDefault;
    public static List<ColorEdit> colorAll;
    int numberOfItems;
    int idColor, idColorSelect;
    public static int idColorEdit;
    public static String textEdit;
    IonClickSectionListener ionClickSectionListener;
    boolean isActionOnWheel = false;

    @Override
    public ActivityAddWheelBinding getBinding() {
        return ActivityAddWheelBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        initData();
        EventTracking.logEvent(this, "add_wheel_view");
        idColor = getIntent().getIntExtra("NEW_WHEEL_COLOR_ID", 1);
        binding.header.tvTitle.setText(R.string.add_wheel);
        switch (idColor) {
            case 1:
                wheelColorSelected = AppDatabase.getInstance(this).wheelDAO().findByName("Standar_d__");
                binding.tvTypeColor.setText(R.string.standard);
                break;
            case 2:
                wheelColorSelected = AppDatabase.getInstance(this).wheelDAO().findByName("Six_Colo_r_");
                binding.tvTypeColor.setText(R.string.six_color);
                break;
            case 3:
                wheelColorSelected = AppDatabase.getInstance(this).wheelDAO().findByName("Vintag_e__");
                binding.tvTypeColor.setText(R.string.vintage);
                break;
            case 4:
                wheelColorSelected = AppDatabase.getInstance(this).wheelDAO().findByName("Two_Colo_r_");
                binding.tvTypeColor.setText(R.string.two_color);
                break;
            case 5:
                wheelColorSelected = AppDatabase.getInstance(this).wheelDAO().findByName("Paste_l__");
                binding.tvTypeColor.setText(R.string.pastel);
                break;
        }
        numberOfItems = wheelColorSelected.getNumberOfItems();
//        for (int i=0;i<numberOfItems;i++){
//            colorEdit.add(wheelColorSelected.getItemColor().get(i%wheelColorSelected.getItemColor().size()));
//        }
        colorEdit = new ArrayList<>(wheelColorSelected.getItemColor());
        colorDefault = wheelColorSelected.getItemColor();
        itemTextEdit = new ArrayList<>(wheelColorSelected.getItemTexts());

        binding.wheel.setNumberOfItems(wheelColorSelected.getNumberOfItems());
        binding.wheel.setRepeatOption(wheelColorSelected.getRepeatOption());
        binding.wheel.setTextSizeItem(wheelColorSelected.getFontSize());

        binding.wheel.setColors(colorEdit);
        binding.wheel.setTextItems(itemTextEdit);
        if (numberOfItems == 0) binding.sbRepeat.setMax(1);
        else {
            int max = (int) (24 / numberOfItems);
            binding.sbRepeat.setMax(max - 1);
        }


        binding.tvFontSizeEdit.setText(String.valueOf(wheelColorSelected.getFontSize()));
        binding.tvRepeatOptionEdit.setText(String.valueOf(wheelColorSelected.getRepeatOption()));
        binding.tvSpinTimeEdit.setText(String.valueOf(wheelColorSelected.getSpinSpeed()));

        binding.sbFontSize.setProgress(wheelColorSelected.getFontSize() - 1);
        binding.sbRepeat.setProgress(wheelColorSelected.getRepeatOption() - 1);
        binding.sbSpinTime.setProgress(wheelColorSelected.getSpinSpeed() - 1);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rcvSection.setLayoutManager(layoutManager);
        ionClickSectionListener = new IonClickSectionListener() {
            @Override
            public void delete(int i) {
                isActionOnWheel = true;
                itemTextEdit.remove(i);
                if (colorEdit.size() != 0)
                    colorEdit.remove(i % colorEdit.size());
                numberOfItems--;
                binding.wheel.setNumberOfItems(numberOfItems);
                binding.wheel.setTextItems(itemTextEdit);
                binding.wheel.setColors(colorEdit);
                if (numberOfItems == 0) {
                    binding.clNoSection.setVisibility(View.VISIBLE);
                    binding.sbRepeat.setMax(1);
                    binding.sbRepeat.setClickable(false);
                } else {

                    int max = (int) (24 / numberOfItems);
                    binding.sbRepeat.setMax(max - 1);
                    binding.sbRepeat.setClickable(true);
                    if (wheelColorSelected.getRepeatOption() > max) {
                        wheelColorSelected.setRepeatOption(max);
                        binding.wheel.setRepeatOption(max);
                    }
                }
                SectionAdapter sectionAdapter = new SectionAdapter(getBaseContext(), itemTextEdit, colorEdit, ionClickSectionListener);
                binding.rcvSection.setAdapter(sectionAdapter);
            }

            @Override
            public void editText(int i, String str) {
                isActionOnWheel = true;
                if (str.equals("")) {
                    str = "\n";
                }
                itemTextEdit.set(i, str);
                binding.wheel.setTextItems(itemTextEdit);
                SectionAdapter sectionAdapter = new SectionAdapter(getBaseContext(), itemTextEdit, colorEdit, ionClickSectionListener);
                binding.rcvSection.setAdapter(sectionAdapter);
            }

            @Override
            public void editColor(int postion) {
                isActionOnWheel = true;
                editColorSection(postion);
            }
        };
        SectionAdapter sectionAdapter = new SectionAdapter(this, itemTextEdit, colorEdit, ionClickSectionListener);
        binding.rcvSection.setAdapter(sectionAdapter);
    }

    @Override
    public void bindView() {
        binding.edtContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    // Ẩn bàn phím
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    }
                    EventTracking.logEvent(getBaseContext(),"add_wheel_enter_name_click");
                    return true;
                }
                return false;
            }
        });
        binding.clPreview.setOnClickListener(view -> EventTracking.logEvent(this,"add_wheel_preview_click"));
        binding.ivPen.setOnClickListener(view -> {
            binding.edtContent.requestFocus();

            // Đưa con trỏ đến cuối nội dung
            binding.edtContent.setSelection(binding.edtContent.getText().length());
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(binding.edtContent, InputMethodManager.SHOW_IMPLICIT);
        });
        binding.header.ivBack.setOnClickListener(view -> {

            onBackPressed();
        });
        binding.sbSpinTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.tvSpinTimeEdit.setText(String.valueOf(i + 1));
                wheelColorSelected.setSpinSpeed(i + 1);
                isActionOnWheel = true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                EventTracking.logEvent(getBaseContext(),"add_wheel_spin_time_click");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.sbFontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.tvFontSizeEdit.setText(String.valueOf(i + 1));
                binding.wheel.setTextSizeItem(i + 1);
                wheelColorSelected.setFontSize(i + 1);
                isActionOnWheel = true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                EventTracking.logEvent(getBaseContext(),"add_wheel_font_size_click");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.sbRepeat.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (numberOfItems > 0) {
                    binding.tvRepeatOptionEdit.setText(String.valueOf(i + 1));
                    binding.wheel.setRepeatOption(i + 1);
                    wheelColorSelected.setRepeatOption(i + 1);
                    binding.sbRepeat.setClickable(false);
                } else {
                    binding.tvRepeatOptionEdit.setText(String.valueOf(0));
                    binding.sbRepeat.setProgress(0);
                    binding.sbRepeat.setClickable(false);
                }
                isActionOnWheel = true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                EventTracking.logEvent(getBaseContext(),"add_wheel_repeat_option_click");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (numberOfItems > 12) {
                    Toast.makeText(AddWheelActivity.this, getString(R.string.maximum_slice_is_24_repeat_option_is_not_exceed_1_now), Toast.LENGTH_SHORT).show();
                }
                if (numberOfItems == 0) {
                    Toast.makeText(AddWheelActivity.this, getString(R.string.add_some_slice_first), Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.btnWheelColor.setOnClickListener(view -> {
            EventTracking.logEvent(this,"add_wheel_color_click");
            addColor();
        });
        binding.ivAddSection.setOnClickListener(view -> {
            isActionOnWheel = true;
            EventTracking.logEvent(this,"add_wheel_add_option_click");
            if (numberOfItems >= 24) {
                Toast.makeText(this, getString(R.string.maximum_slice_is_24), Toast.LENGTH_SHORT).show();
                return;
            }
            numberOfItems++;
            itemTextEdit.add(0,"\n");
            colorEdit.add(colorDefault.get(numberOfItems % colorDefault.size()));
            binding.wheel.setTextItems(itemTextEdit);
            binding.wheel.setColors(colorEdit);
            wheelColorSelected.setNumberOfItems(numberOfItems);
            binding.wheel.setNumberOfItems(numberOfItems);

            int max = (int) (24 / numberOfItems);
            binding.sbRepeat.setMax(max - 1);
            binding.sbRepeat.setClickable(true);
            if (wheelColorSelected.getRepeatOption() > max) {
                wheelColorSelected.setRepeatOption(max);
                binding.wheel.setRepeatOption(max);
            }
            binding.clNoSection.setVisibility(View.GONE);
            SectionAdapter sectionAdapter = new SectionAdapter(this, itemTextEdit, colorEdit, ionClickSectionListener);
            binding.rcvSection.setAdapter(sectionAdapter);

        });
        binding.header.ivGone.setOnClickListener(view -> {
            EventTracking.logEvent(getBaseContext(),"add_wheel_done_click");
            if (checkData()) {
                //AppDatabase.getInstance(this).wheelDAO().insertAll(wheelColorSelected);
                Log.d("spincheck", "addwwhel idtype: " + idColor);
                long id = AppDatabase.getInstance(this).wheelDAO().insertAll(new WheelModel(
                        binding.edtContent.getText().toString().trim(),
                        numberOfItems,0,
                        idColor,
                        wheelColorSelected.getFontSize(),
                        wheelColorSelected.getSpinSpeed(),
                        wheelColorSelected.getRepeatOption(),
                        0, 0,
                        itemTextEdit,
                        colorEdit, true
                ));
                Intent intent = new Intent(this, SpinningWheelActivity.class);
                intent.putExtra("WHEEL_ID", (int) id);
                startActivity(intent);
                finish();
            }
        });
    }

    private void disCardChanges() {
        DiscardDialog dialog = new DiscardDialog(this, false);
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

    private void editColorSection(int position) {
        idColorEdit = colorEdit.get(position % colorEdit.size());
        textEdit = itemTextEdit.get(position);
        EditColorDialogAdd editColorDialog = new EditColorDialogAdd(AddWheelActivity.this, true);
        editColorDialog.init(new IClickDialogEditColor() {
            @Override
            public void cancel() {
                editColorDialog.dismiss();
            }

            @Override
            public void okay() {
                textEdit = editColorDialog.binding.edtContent.getText().toString().trim();
                if (textEdit.equals("")) {
                    textEdit = "\n";
                }
                itemTextEdit.set(position, textEdit);
                binding.wheel.setTextItems(itemTextEdit);

                colorEdit.set(position % colorEdit.size(), idColorEdit);
                binding.wheel.setColors(colorEdit);

                SectionAdapter sectionAdapter = new SectionAdapter(getBaseContext(), itemTextEdit, colorEdit, ionClickSectionListener);
                binding.rcvSection.setAdapter(sectionAdapter);
                editColorDialog.dismiss();
            }

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
        isActionOnWheel = true;
        WheelColorDialog wheelColorDialog = new WheelColorDialog(this, true);
        wheelColorDialog.binding.tvTitle.setText(R.string.choose_wheel_color);
        wheelColorDialog.binding.rdoStandard.setImageResource(R.drawable.img_wheel_color_unselect);
        wheelColorDialog.binding.rdoSixColor.setImageResource(R.drawable.img_wheel_color_unselect);
        wheelColorDialog.binding.rdoVintage.setImageResource(R.drawable.img_wheel_color_unselect);
        wheelColorDialog.binding.rdoTwoColor.setImageResource(R.drawable.img_wheel_color_unselect);
        wheelColorDialog.binding.rdoPastel.setImageResource(R.drawable.img_wheel_color_unselect);
        idColorSelect = idColor;
        switch (idColor) {
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
                switch (idColor) {
                    case 1:
                        colorDefault = AppDatabase.getInstance(getBaseContext()).wheelDAO().findByName("Standar_d__").getItemColor();
                        binding.tvTypeColor.setText(R.string.standard);
                        break;
                    case 2:
                        colorDefault = AppDatabase.getInstance(getBaseContext()).wheelDAO().findByName("Six_Colo_r_").getItemColor();
                        binding.tvTypeColor.setText(R.string.six_color);
                        break;
                    case 3:
                        colorDefault = AppDatabase.getInstance(getBaseContext()).wheelDAO().findByName("Vintag_e__").getItemColor();
                        binding.tvTypeColor.setText(R.string.vintage);
                        break;
                    case 4:
                        colorDefault = AppDatabase.getInstance(getBaseContext()).wheelDAO().findByName("Two_Colo_r_").getItemColor();
                        binding.tvTypeColor.setText(R.string.two_color);
                        break;
                    case 5:
                        colorDefault = AppDatabase.getInstance(getBaseContext()).wheelDAO().findByName("Paste_l__").getItemColor();
                        binding.tvTypeColor.setText(R.string.pastel);
                        break;
                }
                //colorEdit = new ArrayList<>(colorDefault);
                if (numberOfItems == 0) colorEdit = new ArrayList<>();
                else {
                    colorEdit = new ArrayList<>();
                    for (int i = 0; i < numberOfItems; i++) {
                        colorEdit.add(colorDefault.get(i % colorDefault.size()));
                    }
                }
                binding.wheel.setColors(colorEdit);
                SectionAdapter sectionAdapter = new SectionAdapter(getBaseContext(), itemTextEdit, colorEdit, ionClickSectionListener);
                binding.rcvSection.setAdapter(sectionAdapter);
                wheelColorDialog.dismiss();
            }

            @Override
            public int color(int i) {
                idColorSelect = i;
                wheelColorDialog.binding.rdoStandard.setImageResource(R.drawable.img_wheel_color_unselect);
                wheelColorDialog.binding.rdoSixColor.setImageResource(R.drawable.img_wheel_color_unselect);
                wheelColorDialog.binding.rdoVintage.setImageResource(R.drawable.img_wheel_color_unselect);
                wheelColorDialog.binding.rdoTwoColor.setImageResource(R.drawable.img_wheel_color_unselect);
                wheelColorDialog.binding.rdoPastel.setImageResource(R.drawable.img_wheel_color_unselect);
                switch (i) {
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

    private boolean checkData() {
        if (binding.edtContent.getText().toString().trim().equals("")) {
            binding.clNoNameWheel.setVisibility(View.VISIBLE);
            Toast.makeText(this, getString(R.string.enter_name_of_wheel), Toast.LENGTH_SHORT).show();
            return false;
        }
        binding.clNoNameWheel.setVisibility(View.GONE);
        if (itemTextEdit.size() < 2) {
            Toast.makeText(this, getString(R.string.add_at_least_2_slices_to_make_the_wheel), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initData() {
        colorAll = new ArrayList<>();
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_0), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_1), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_2), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_3), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_4), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_5), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_6), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_7), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_8), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_9), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_10), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_11), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_12), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_13), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_14), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_15), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_16), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_17), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_18), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_19), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_20), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_21), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_22), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_23), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_24), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_25), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_26), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_27), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_28), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_29), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_30), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_31), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_32), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_33), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_34), false));
        colorAll.add(new ColorEdit(getBaseContext().getResources().getColor(R.color.edit_color_35), false));
        Log.d("spinCheck", "color: " + colorAll.toString());
    }


    @Override
    public void onBackPressed() {
        EventTracking.logEvent(this,"add_wheel_back_click");
        if (!isActionOnWheel) finish();
        else disCardChanges();
    }
}