package com.tkt.spin_wheel.ui.coin;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.tkt.spin_wheel.R;
import com.tkt.spin_wheel.base.BaseActivity;
import com.tkt.spin_wheel.databinding.ActivityCoinFlipBinding;

import java.util.Random;

public class CoinFlipActivity extends BaseActivity<ActivityCoinFlipBinding> {

    Flip3dAnimation  flipToBack = null;
    Flip3dAnimation flipToFront = null;
    int countFlip = 0,random =0;
    boolean isFlip = false;
    @Override
    public ActivityCoinFlipBinding getBinding() {
        return ActivityCoinFlipBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        binding.header.tvTitle.setText(R.string.coin_flip);
        binding.header.ivGone.setVisibility(View.INVISIBLE);
        binding.tvCoinBack.setText("0");
        binding.tvCoinHead.setText("0");
    }

    @Override
    public void bindView() {
        binding.header.ivBack.setOnClickListener(view -> onBackPressed());
        binding.clCoinMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFlip) return;
                countFlip = 0;
                isFlip = true;
                binding.tvStart.setVisibility(View.GONE);
                startFlipMoveCoinUp();
                startFlipAnimation();

            }
        });
    }
    private void startFlipAnimation() {
        final float centerX = binding.ivCoinHead.getWidth() / 2.0f;
        final float centerY = binding.ivCoinHead.getHeight() / 2.0f;
        random = new Random().nextInt(2)+10;
        flipToBack = new Flip3dAnimation(0, 180, centerX, centerY, binding.ivCoinHead, binding.ivCoinBack);
        flipToBack.setDuration(100);
        flipToBack.setFillAfter(true);
        flipToBack.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                countFlip++;
                binding.clCoin.setRotation(0);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (countFlip==random) {
                    binding.clCoin.setRotation(180);
                    new Handler().postDelayed(()->{startFlipMoveCoinDownAndCount();},100);
                    return;
                }
                flipToFront = new Flip3dAnimation(180, 360, centerX, centerY, binding.ivCoinHead, binding.ivCoinBack);
                flipToFront.setDuration(100);
                flipToFront.setFillAfter(true);
                flipToFront.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        countFlip++;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (countFlip==random){
                            new Handler().postDelayed(CoinFlipActivity.this::startFlipMoveCoinDownAndCount,100);
                            return;
                        }
                        binding.clCoin.startAnimation(flipToBack);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                binding.clCoin.startAnimation(flipToFront);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        //flip.setRepeatCount(Animation.INFINITE);

        binding.clCoin.startAnimation(flipToBack);

    }
    private void startFlipMoveCoinUp(){
        int parentHeight = binding.clCoinMoveParent.getHeight();
        int clCoinHeight = binding.clCoinMove.getHeight();

        ObjectAnimator moveUp = ObjectAnimator.ofFloat(binding.clCoinMove, "translationY", clCoinHeight - parentHeight);
        moveUp.setDuration(1000);
        moveUp.setInterpolator(new LinearInterpolator());
        moveUp.start();

    }
    private void startFlipMoveCoinDownAndCount(){
        int countCoinBack ;
        int countCoinHead ;
        try {
             countCoinBack = Integer.parseInt((String) binding.tvCoinBack.getText());
             countCoinHead = Integer.parseInt((String) binding.tvCoinHead.getText());
        } catch (Exception e){
             countCoinBack = 0;
             countCoinHead = 0;
        }
        if (random%2==1){
            countCoinBack++;
            binding.tvCoinBack.setText(String.valueOf(countCoinBack));
        } else {
            countCoinHead++;
            binding.tvCoinHead.setText(String.valueOf(countCoinHead));
        }
        ObjectAnimator moveUp = ObjectAnimator.ofFloat(binding.clCoinMove, "translationY", 0f);
        moveUp.setDuration(200);
        moveUp.setInterpolator(new LinearInterpolator());
        moveUp.start();
        new Handler().postDelayed(()->{isFlip=false;},200);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}