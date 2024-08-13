package com.tkt.spin_wheel.ui.spin;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.tkt.spin_wheel.R;
import com.tkt.spin_wheel.base.BaseActivity;
import com.tkt.spin_wheel.database.AppDatabase;
import com.tkt.spin_wheel.databinding.ActivitySpinningWheelBinding;
import com.tkt.spin_wheel.ui.spin.model.WheelModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpinningWheelActivity extends BaseActivity<ActivitySpinningWheelBinding> {
    boolean isSpinning = false;
    float degrees, plusDegree;
    Runnable runnableSpinning;
    Handler handler = new Handler();
    int numberOfItems, targetIndex,countLight=0,countLightCheck;
    boolean isLight = true;
    float sweepAngle, speedSpin, totalAngle, currentIndex;

    String winningItem;
    WheelModel wheelSpinning;
    List<Integer> colors;
    List<String> itemTexts;
    List<String> test = new ArrayList<>();

    @Override
    public ActivitySpinningWheelBinding getBinding() {
        return ActivitySpinningWheelBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        int name = getIntent().getIntExtra("WHEEL_ID",0);
        wheelSpinning = AppDatabase.getInstance(this).wheelDAO().findById(name);
        binding.wheel.setNumberOfItems(wheelSpinning.getNumberOfItems());
        numberOfItems = wheelSpinning.getNumberOfItems();
        speedSpin = wheelSpinning.getSpinSpeed();
        colors = wheelSpinning.getItemColor();
        itemTexts = wheelSpinning.getItemTexts();
        binding.wheel.setRepeatOption(wheelSpinning.getRepeatOption());
        binding.wheel.setColors(colors);
        binding.wheel.setTextSizeItem(wheelSpinning.getFontSize());
        binding.wheel.setTextItems(itemTexts);
        sweepAngle = 360f/(wheelSpinning.getNumberOfItems()*wheelSpinning.getRepeatOption());
        currentIndex = 270f/sweepAngle;
        targetIndex = (int) (currentIndex);
        winningItem = itemTexts.get(targetIndex%itemTexts.size());
        binding.tvNameOption.setText(winningItem);
    }

    @Override
    public void bindView() {
        binding.ivBack.setOnClickListener(view -> onBackPressed());
        binding.ivEdit.setOnClickListener(view -> {
            Intent intent= new Intent(this, EditWheelActivity.class);
            intent.putExtra("WHEEL_EDIT_ID",wheelSpinning.getId());
            resultLauncher.launch(intent);
        });
        binding.ivSound.setOnClickListener(view -> {
        });
        binding.clSpin.setOnClickListener(view -> startSpinning());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            //ads
            Log.d("activity_check", "spinningwheel");
        }
    });

    private void startSpinning() {
        if (isSpinning) return;
        Random random = new Random();
        isSpinning = true;
        plusDegree = 90+random.nextInt(60);
        totalAngle = 0;
        countLight=0;
        runnableSpinning = new Runnable() {
            @Override
            public void run() {
                if (plusDegree <= 0.51) {
                    plusDegree = 0;
                    isSpinning = false;
                    currentIndex = currentIndex-totalAngle/sweepAngle;
                    while (currentIndex<0) currentIndex+=numberOfItems;
                    Log.d("spinCheck", "currentIndex" + currentIndex);
                    Log.d("spinCheck", "sweepAngle" + sweepAngle);
                    targetIndex = (int) (currentIndex);
                    winningItem = itemTexts.get(targetIndex%itemTexts.size());
                    Toast.makeText(SpinningWheelActivity.this, "win: "+winningItem, Toast.LENGTH_SHORT).show();
                    handler.removeCallbacks(this);
                } else {
                    countLight++;
                    plusDegree = plusDegree * (0.98f-(speedSpin)/100);
                    if (plusDegree>90) countLightCheck = 2;
                    else if (plusDegree>60)  countLightCheck = 3;
                    else if (plusDegree>30) countLightCheck = 4;
                    else if (plusDegree>10) countLightCheck = 7;
                    else countLightCheck = 10;
                    Log.d("spinCheck", "plusDegree" + plusDegree);
                    RotateAnimation rotate = new RotateAnimation(degrees, degrees + plusDegree,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(50);
                    rotate.setFillAfter(true);
                    rotate.setInterpolator(new LinearInterpolator());
                    binding.wheel.startAnimation(rotate);
                    degrees += plusDegree;
                    totalAngle += plusDegree;
                    Log.d("spinCheck", "total degree" + totalAngle);
                    if (degrees >= 360) {
                        degrees = degrees % 360;
                    }
                    float thisIndex = currentIndex-totalAngle/sweepAngle;
                    while (thisIndex<0) thisIndex+=numberOfItems;
                    Log.d("spinCheck", "currentIndex" + thisIndex);
                    Log.d("spinCheck", "sweepAngle" + sweepAngle);
                    targetIndex = (int) (thisIndex);
                    winningItem = itemTexts.get(targetIndex%itemTexts.size());
                    binding.tvNameOption.setText(winningItem);
                    if (isLight){
                        binding.ivSpinWheel.setImageResource(R.drawable.img_spin_wheel_2);
                    } else {
                        binding.ivSpinWheel.setImageResource(R.drawable.img_spin_wheel_1);
                    }
                    if (countLight>=countLightCheck){
                        isLight = !isLight;
                        countLight=0;
                    }
                    handler.postDelayed(this, 50);
                }
            }
        };
        handler.post(runnableSpinning);
    }
}