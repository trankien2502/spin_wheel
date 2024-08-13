package com.tkt.spin_wheel.ui.number;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.tkt.spin_wheel.R;
import com.tkt.spin_wheel.base.BaseActivity;
import com.tkt.spin_wheel.databinding.ActivityLuckyNumerBinding;
import com.tkt.spin_wheel.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LuckyNumerActivity extends BaseActivity<ActivityLuckyNumerBinding> {
    boolean isProcessing = false;
    List<Integer> existNumber, luckyNumber;
    int minNumber, maxNumber, quantity, count=0;
    boolean isRepeat = false;
    ConstraintLayout[] listClNumber = null;
    TextView[] listTvNumber = null;
    @Override
    public ActivityLuckyNumerBinding getBinding() {
        return ActivityLuckyNumerBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        initData();
        binding.header.tvTitle.setText(R.string.lucky_number);
        binding.header.ivGone.setVisibility(View.INVISIBLE);
    }
    @Override
    public void bindView() {
        binding.header.ivBack.setOnClickListener(view -> onBackPressed());
        binding.ivRepetition.setOnClickListener(view -> {
            if (isProcessing){
                Toast.makeText(this, R.string.please_wait, Toast.LENGTH_SHORT).show();
                return;
            }
            if (isRepeat){
                isRepeat = false;
                binding.ivRepetition.setImageResource(R.drawable.img_number_repetition_unselect);
            }else {
                isRepeat = true;
                binding.ivRepetition.setImageResource(R.drawable.img_number_repetition_select);
            }
            Log.d("luckyCheck","repeat "  +isRepeat);
        });
        binding.clGo.setOnClickListener(view -> {
            if (isProcessing) {
                Toast.makeText(this, R.string.please_wait, Toast.LENGTH_SHORT).show();
                return;
            }
            if (!checkDataEntry()) return;
            minNumber = StringUtils.getNumbeText(binding.edtFrom);
            maxNumber = StringUtils.getNumbeText(binding.edtTo);
            quantity = StringUtils.getNumbeText(binding.tvNumber);
            if (!checkDataValid()) return;
            binding.num13.setVisibility(View.GONE);
            binding.num46.setVisibility(View.GONE);
            binding.num79.setVisibility(View.GONE);
            binding.num10.setVisibility(View.GONE);
            binding.tvCoppy.setVisibility(View.GONE);
            for (int i=0;i<10;i++){
                listClNumber[i].setVisibility(View.GONE);
            }
            binding.tvCoppy.setVisibility(View.GONE);
            binding.tvGo.setVisibility(View.GONE);
            binding.ivCount.setVisibility(View.VISIBLE);
            createAnimationNumberCount();
            new Handler().postDelayed(()->{
                binding.ivCount.setImageResource(R.drawable.img_count_two);
                createAnimationNumberCount();
                new Handler().postDelayed(()->{
                    createAnimationNumberCount();
                    binding.ivCount.setImageResource(R.drawable.img_count_one);
                    new Handler().postDelayed(()->{
                        binding.ivCount.setImageResource(R.drawable.img_count_three);
                        binding.ivCount.setVisibility(View.GONE);
                        showLuckyNumber();
                        Toast.makeText(this, "Counted!", Toast.LENGTH_SHORT).show();
                    },1000);
                },1000);
            },1000);
        });
        binding.ivMinus.setOnClickListener(view -> {
            quantity = StringUtils.getNumbeText(binding.tvNumber);
            if(quantity==2){
                binding.ivMinus.setImageResource(R.drawable.img_minus_unselect);
                quantity--;
                binding.tvNumber.setText(String.valueOf(quantity));
            } else if (quantity==1) {
                binding.ivMinus.setImageResource(R.drawable.img_minus_unselect);
            } else {
                if (quantity==10) binding.ivPlus.setImageResource(R.drawable.img_plus_select);
                binding.ivMinus.setImageResource(R.drawable.img_minus_select);
                quantity--;
                binding.tvNumber.setText(String.valueOf(quantity));
            }
        });
        binding.ivPlus.setOnClickListener(view -> {
            quantity = StringUtils.getNumbeText(binding.tvNumber);
            if(quantity==9){
                binding.ivPlus.setImageResource(R.drawable.img_plus_unselect);
                quantity++;
                binding.tvNumber.setText(String.valueOf(quantity));
            } else if (quantity==10) {
                binding.ivPlus.setImageResource(R.drawable.img_plus_unselect);
            } else {
                if (quantity==1) binding.ivMinus.setImageResource(R.drawable.img_minus_select);
                binding.ivPlus.setImageResource(R.drawable.img_plus_select);
                quantity++;
                binding.tvNumber.setText(String.valueOf(quantity));
            }
        });
        binding.tvCoppy.setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", luckyNumber.toString());
            clipboard.setPrimaryClip(clip);

            Toast.makeText(this, "Copy success", Toast.LENGTH_SHORT).show();
        });
        binding.edtFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    int value = Integer.parseInt(editable.toString());
                    if (value > 99999) {
                        binding.edtFrom.removeTextChangedListener(this);
                        binding.edtFrom.setText("99999");
                        binding.edtFrom.setSelection(binding.edtFrom.getText().length());
                        binding.edtFrom.addTextChangedListener(this);
                    }
                } catch (NumberFormatException e) {
                    Log.e("luckyCheck","fail");
                }
            }
        });
        binding.edtTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    int value = Integer.parseInt(editable.toString());
                    if (value > 99999) {
                        binding.edtTo.removeTextChangedListener(this);
                        binding.edtTo.setText("99999");
                        binding.edtTo.setSelection(binding.edtTo.getText().length());
                        binding.edtTo.addTextChangedListener(this);
                    }
                } catch (NumberFormatException e) {
                    Log.e("luckyCheck","fail");
                }
            }
        });
    }
    private boolean checkDataEntry(){
        if (binding.edtFrom.getText().toString().equals("")||binding.edtTo.getText().toString().equals("")||binding.tvNumber.getText().toString().equals("")){
            Toast.makeText(this, getString(R.string.please_enter_range), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }
    private boolean checkDataValid(){
        if (minNumber>maxNumber){
            Toast.makeText(this, getString(R.string.the_from_number_must_be_less_than_the_to_number), Toast.LENGTH_SHORT).show();
            return false;
        }
        if ((maxNumber-minNumber+1<quantity)&&!isRepeat){
            Toast.makeText(this, getString(R.string.numbers_quantity_must_be_less_than_the_range_provided_when_the_repetition_is_unchecked), Toast.LENGTH_SHORT).show();
            return false;
        }
        return  true;
    }
    private void showLuckyNumber(){
        getListLuckyNumber();
        count = 0;
        binding.num13.setVisibility(View.VISIBLE);
        if (luckyNumber.size()>3) binding.num46.setVisibility(View.VISIBLE);
        if (luckyNumber.size()>6) binding.num79.setVisibility(View.VISIBLE);
        if (luckyNumber.size()>9) binding.num10.setVisibility(View.VISIBLE);
        binding.tvCoppy.setVisibility(View.VISIBLE);
        for (int i=0;i<luckyNumber.size();i++){
            listClNumber[i].setVisibility(View.VISIBLE);
            listTvNumber[i].setText(String.valueOf(luckyNumber.get(i)));
        }
    }
    private void getListLuckyNumber (){
        existNumber = new ArrayList<>();
        luckyNumber = new ArrayList<>();
        Random random = new Random();
        do {
            int luckyNum = minNumber + random.nextInt(maxNumber-minNumber+1);
            if (isRepeat) {
                luckyNumber.add(luckyNum);
                count++;
            }else {
                if (!luckyNumber.contains(luckyNum)){
                    luckyNumber.add(luckyNum);
                    count++;
                    Log.d("luckyCheck","luckyList :"+luckyNumber);
                    Log.d("luckyCheck","existNumber :"+existNumber);
                    Log.d("luckyCheck","count :"+count);
                } else {
                    existNumber.add(luckyNum);
                }
            }
        } while (count<quantity);
    }
    private void createAnimationNumberCount(){
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                binding.ivCount,
                PropertyValuesHolder.ofFloat("scaleX", 1.0f, 2.0f),
                PropertyValuesHolder.ofFloat("scaleY", 1.0f, 2.0f));
        scaleDown.setDuration(1000);
        scaleDown.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleDown.start();
    }
    private void initData() {
        listClNumber = new ConstraintLayout[]{
                binding.clNumber1,binding.clNumber2,binding.clNumber3,binding.clNumber4,binding.clNumber5,
                binding.clNumber6,binding.clNumber7,binding.clNumber8,binding.clNumber9,binding.clNumber10
        };
        listTvNumber = new TextView[]{
                binding.tvNumber1,binding.tvNumber2,binding.tvNumber3,binding.tvNumber4,binding.tvNumber5,
                binding.tvNumber6,binding.tvNumber7,binding.tvNumber8,binding.tvNumber9,binding.tvNumber10
        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}