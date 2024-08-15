package com.tkt.spin_wheel.ui.spin;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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
import com.tkt.spin_wheel.dialog.winner.IClickDialogWinner;
import com.tkt.spin_wheel.dialog.winner.WinnerDialog;
import com.tkt.spin_wheel.ui.home.HomeActivity;
import com.tkt.spin_wheel.ui.spin.model.WheelModel;
import com.tkt.spin_wheel.util.EventTracking;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpinningWheelActivity extends BaseActivity<ActivitySpinningWheelBinding> {
    boolean isSpinning = false, isSpinningActivity = true, isReadyToShowWinner = false;
    float degrees, plusDegree;
    Runnable runnableSpinning;
    Handler handler = new Handler();
    int numberOfItems, speedSpin, targetIndex, countLight = 0, countLightCheck;
    boolean isLight = true;
    float sweepAngle, totalAngle, currentIndex;
    float minusAngle;
    public static String winningItem;
    public static int winningColor;
    WheelModel wheelSpinning;
    List<Integer> colors;
    List<String> itemTexts;
    List<Integer> colorAfterHide;
    List<String> itemTextAfterHide;
    int numberOfItemAfterHide;
    RotateAnimation rotate;

    boolean isSound = true;
    MediaPlayer spinningMedia = new MediaPlayer();
    MediaPlayer winnerMedia = new MediaPlayer();

    @Override
    public ActivitySpinningWheelBinding getBinding() {
        return ActivitySpinningWheelBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        EventTracking.logEvent(getBaseContext(),"play_wheel_view");
        int name = getIntent().getIntExtra("WHEEL_ID", 0);
        wheelSpinning = AppDatabase.getInstance(this).wheelDAO().findById(name);
        binding.tvNameWheel.setText(wheelSpinning.getName());

        //get inf of wheel
        numberOfItems = wheelSpinning.getNumberOfItems();
        speedSpin = wheelSpinning.getSpinSpeed();
        colors = wheelSpinning.getItemColor();
        itemTexts = wheelSpinning.getItemTexts();
        //set the copy inf to hide
        numberOfItemAfterHide = numberOfItems;
        colorAfterHide = new ArrayList<>(colors);
        itemTextAfterHide = new ArrayList<>(itemTexts);
        //set wheel
        binding.wheel.setNumberOfItems(numberOfItemAfterHide);
        binding.wheel.setRepeatOption(wheelSpinning.getRepeatOption());
        binding.wheel.setColors(colorAfterHide);
        binding.wheel.setTextSizeItem(wheelSpinning.getFontSize());
        binding.wheel.setTextItems(itemTextAfterHide);
        sweepAngle = 360f / (numberOfItemAfterHide * wheelSpinning.getRepeatOption());
        //default winner
        currentIndex = 270f / sweepAngle;
        targetIndex = (int) (currentIndex);

        winningItem = itemTextAfterHide.get(targetIndex % itemTextAfterHide.size());
        winningColor = colorAfterHide.get(targetIndex % colorAfterHide.size());
        binding.tvNameOption.setText(winningItem);
    }

    @Override
    public void bindView() {
        binding.ivBack.setOnClickListener(view -> onBackPressed());
        binding.ivEdit.setOnClickListener(view -> {
            EventTracking.logEvent(getBaseContext(),"play_wheel_edit_click");
            Intent intent = new Intent(this, EditWheelActivity.class);
            intent.putExtra("WHEEL_EDIT_ID", wheelSpinning.getId());
            resultLauncher.launch(intent);
        });
        binding.ivSound.setOnClickListener(view -> {
            EventTracking.logEvent(getBaseContext(),"play_wheel_mute_click");
            if (!isSound) {
                binding.ivSound.setImageResource(R.drawable.img_spin_sound);
                if (isSpinning) playSoundSpin();
            } else {
                binding.ivSound.setImageResource(R.drawable.img_spin_sound_off);
                if (isSpinning) stopSoundSpin();
            }
            isSound = !isSound;
        });
        binding.clSpin.setOnClickListener(view -> {
            startSpinning();
            EventTracking.logEvent(getBaseContext(),"play_wheel_spin_click");
        });
    }

    private void playSoundSpin() {
        if (spinningMedia != null) {
            spinningMedia.release();
        }
        spinningMedia = MediaPlayer.create(this, R.raw.spinning);
        spinningMedia.start();
    }

    private void playSoundWinner() {
        if (winnerMedia != null) {
            winnerMedia.release();
        }
        winnerMedia = MediaPlayer.create(this, R.raw.winner);
        winnerMedia.start();
    }

    private void stopSoundSpin() {
        if (spinningMedia != null) {
            spinningMedia.release();
            spinningMedia = null;
        }
    }

    private void stopSoundWinner() {
        if (winnerMedia != null) {
            winnerMedia.release();
            winnerMedia = null;
        }
    }

    @Override
    public void onBackPressed() {
        EventTracking.logEvent(getBaseContext(),"play_wheel_back_click");
        startNextActivity(HomeActivity.class, null);
        finishAffinity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSpinning && isSound) {
            playSoundSpin();
        }
        isSpinningActivity = true;
        if (isReadyToShowWinner && isSpinningActivity) showWinner();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopSoundSpin();

        stopSoundWinner();
        isSpinningActivity = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopSoundSpin();
        stopSoundWinner();
        isSpinningActivity = false;
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            //ads
            Log.d("spinCheck", "spinningwheel");
        }
    });

    private void startSpinning() {
        binding.clErrorSlice.setVisibility(View.GONE);
        if (isSpinning) {
            Toast.makeText(this, getString(R.string.please_wait), Toast.LENGTH_SHORT).show();
            return;
        }
        if (isSound) playSoundSpin();
        Random random = new Random();
        isSpinning = true;
        plusDegree = 60 + random.nextInt(120);
        totalAngle = 0;
        countLight = 0;
        int timeSpin = speedSpin * 30;
        minusAngle = plusDegree / timeSpin;
        sweepAngle = 360f / (numberOfItemAfterHide * wheelSpinning.getRepeatOption());
        runnableSpinning = new Runnable() {
            @Override
            public void run() {
                if (plusDegree <= 0.2) {
                    plusDegree = 0;
                    isSpinning = false;
                    isReadyToShowWinner = true;
                    stopSoundSpin();
                    if (isReadyToShowWinner && isSpinningActivity) showWinner();
                    handler.removeCallbacks(this);
                } else {
                    countLight++;
                    plusDegree = plusDegree - minusAngle * 5 / 4;
                    if (plusDegree > 90) countLightCheck = 1;
                    else if (plusDegree > 60) countLightCheck = 2;
                    else if (plusDegree > 30) countLightCheck = 3;
                    else if (plusDegree > 10) countLightCheck = 4;
                    else countLightCheck = 5;
                    rotate = new RotateAnimation(degrees, degrees + plusDegree,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(100);
                    rotate.setFillAfter(true);
                    rotate.setInterpolator(new LinearInterpolator());
                    binding.wheel.startAnimation(rotate);
                    degrees += plusDegree;
                    totalAngle += plusDegree;
                    if (degrees >= 360) {
                        degrees = degrees % 360;
                    }
                    float thisIndex = currentIndex - totalAngle / sweepAngle;
                    while (thisIndex < 0) thisIndex += numberOfItemAfterHide;
                    targetIndex = (int) (thisIndex);
                    winningItem = itemTextAfterHide.get(targetIndex % itemTextAfterHide.size());
                    winningColor = colorAfterHide.get(targetIndex % colorAfterHide.size());
//                    Log.d("spinCheck", "targetIndex: " + targetIndex);
//                    Log.d("spinCheck", "currentIndex: " + currentIndex);
                    binding.tvNameOption.setText(winningItem);
                    if (isLight) {
                        binding.ivSpinWheel.setImageResource(R.drawable.img_spin_wheel_2);
                    } else {
                        binding.ivSpinWheel.setImageResource(R.drawable.img_spin_wheel_1);
                    }
                    if (countLight >= countLightCheck) {
                        isLight = !isLight;
                        countLight = 0;
                    }
                    handler.postDelayed(this, 100);
                }
            }
        };
        handler.post(runnableSpinning);
    }

    private void showWinner() {
        if (isSound) playSoundWinner();
        wheelSpinning.setSpin(wheelSpinning.getSpin() + 1);
        long endTime = System.currentTimeMillis();
        wheelSpinning.setLastUsed(endTime);
        AppDatabase.getInstance(this).wheelDAO().update(wheelSpinning);
        currentIndex = currentIndex - totalAngle / sweepAngle;
        while (currentIndex < 0) currentIndex += numberOfItemAfterHide;
        targetIndex = (int) (currentIndex);
        isReadyToShowWinner = false;
        winningItem = itemTextAfterHide.get(targetIndex % itemTextAfterHide.size());
        winningColor = colorAfterHide.get(targetIndex % colorAfterHide.size());
        binding.tvNameOption.setText(winningItem);
        WinnerDialog dialog = new WinnerDialog(this, false);
        dialog.init(new IClickDialogWinner() {
            @Override
            public void hide() {
                //set wheel
                if (numberOfItemAfterHide > 2) {
                    numberOfItemAfterHide--;
                    colorAfterHide.remove(targetIndex % colorAfterHide.size());
                    itemTextAfterHide.remove(targetIndex % colorAfterHide.size());
                    binding.wheel.setNumberOfItems(numberOfItemAfterHide);
                    binding.wheel.setRepeatOption(wheelSpinning.getRepeatOption());
                    binding.wheel.setColors(colorAfterHide);
                    binding.wheel.setTextItems(itemTextAfterHide);

                    //default winner
                    binding.wheel.clearAnimation();
                    binding.wheel.setRotation(0);
                    degrees = 0;
                    sweepAngle = 360f / (numberOfItemAfterHide * wheelSpinning.getRepeatOption());
                    currentIndex = 270f / sweepAngle;
                    targetIndex = (int) (currentIndex);

                    winningItem = itemTextAfterHide.get(targetIndex % itemTextAfterHide.size());
                    winningColor = colorAfterHide.get(targetIndex % colorAfterHide.size());
                    binding.tvNameOption.setText(winningItem);
                } else {
                    binding.clErrorSlice.setVisibility(View.VISIBLE);
                }
                dialog.dismiss();
            }

            @Override
            public void okay() {
                dialog.dismiss();
            }
        });
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}