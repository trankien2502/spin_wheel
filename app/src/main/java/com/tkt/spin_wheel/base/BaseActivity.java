package com.tkt.spin_wheel.base;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.viewbinding.ViewBinding;


import com.tkt.spin_wheel.R;
import com.tkt.spin_wheel.ui.intro.IntroActivity;
import com.tkt.spin_wheel.util.SystemUtil;


import java.util.Objects;

public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity {

    public VB binding;

    public abstract VB getBinding();

    public abstract void initView();

    public abstract void bindView();

    //public abstract void onBack();

    Animation animation;
    AlertDialog alertDialog;
    MediaPlayer mediaPlayerBackground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SystemUtil.setLocale(this);
        super.onCreate(savedInstanceState);
        binding = getBinding();
        setContentView(binding.getRoot());

        animation = AnimationUtils.loadAnimation(this, R.anim.onclick);
        //make fully Android Transparent Status bar
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        // Thiết lập màu trong suốt cho thanh điều hướng
        getWindow().setNavigationBarColor(Color.TRANSPARENT);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
//        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                onBack();
//            }
//        });
        if (!(this instanceof IntroActivity)) {
            // Padding bottom with the value of status bar
            binding.getRoot().setPadding(
                    binding.getRoot().getPaddingLeft(),
                    binding.getRoot().getPaddingTop() + getStatusBarHeight(),
                    binding.getRoot().getPaddingRight(),
                    binding.getRoot().getPaddingBottom()
            );
        }
        hideNavigation();
        createLoadingDialog();
        initView();
        bindView();
    }
    private void createLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_loading, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        alertDialog = builder.create();

    }
//    public void showLoadingDialog(){
//        alertDialog.show();
//    }
//    public void dismissLoadingDialog(){
//        alertDialog.dismiss();
//    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void startNextActivity(Class activity, Bundle bundle) {
        Intent intent = new Intent(this, activity);
        if (bundle == null) {
            bundle = new Bundle();
        }
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.in_right, R.anim.out_left);
    }

    public void animateTextView(TextView textView) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(textView, "scaleX", 1.15f);
        scaleX.setDuration(500);
        scaleX.setRepeatCount(ValueAnimator.INFINITE);
        scaleX.setRepeatMode(ValueAnimator.REVERSE);

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(textView, "scaleY", 1.15f);
        scaleY.setDuration(500);
        scaleY.setRepeatCount(ValueAnimator.INFINITE);
        scaleY.setRepeatMode(ValueAnimator.REVERSE);

        ObjectAnimator scaleXBack = ObjectAnimator.ofFloat(textView, "scaleX", 1f);
        scaleXBack.setDuration(500);
        scaleXBack.setRepeatCount(ValueAnimator.INFINITE);
        scaleXBack.setRepeatMode(ValueAnimator.REVERSE);

        ObjectAnimator scaleYBack = ObjectAnimator.ofFloat(textView, "scaleY", 1f);
        scaleYBack.setDuration(500);
        scaleYBack.setRepeatCount(ValueAnimator.INFINITE);
        scaleYBack.setRepeatMode(ValueAnimator.REVERSE);
        // Create an AnimatorSet to play the animations sequentially
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleX).with(scaleY);
        animatorSet.play(scaleXBack).with(scaleYBack).after(scaleX);

        animatorSet.start();
    }
//    public void stopBackgroundSound() {
//        if (mediaPlayerBackground != null) {
//            SPUtils.setInt(getBaseContext(),SPUtils.SOUND_POSITION,mediaPlayerBackground.getCurrentPosition());
//            mediaPlayerBackground.release();
//            mediaPlayerBackground = null;
//        }
//    }
//    public void playBackgroundSound() {
//        if (mediaPlayerBackground != null) {
//            mediaPlayerBackground.release();
//        }
//        mediaPlayerBackground = MediaPlayer.create(this, R.raw.background_sound);
//        int position = SPUtils.getInt(this,SPUtils.SOUND_POSITION,0);
//        mediaPlayerBackground.seekTo(position);
//        mediaPlayerBackground.start();
//        mediaPlayerBackground.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                mediaPlayer.seekTo(0);
//                mediaPlayer.start();
//            }
//        });
//    }
    @Override
    protected void onResume() {
        super.onResume();
        //tắt ads resume all
//        if (RemoteConfig.remote_resume) {
//            AppOpenManager.getInstance().enableAppResumeWithActivity(getClass());
//        } else {
//            AppOpenManager.getInstance().disableAppResumeWithActivity(getClass());
//        }
    }

    public void finishThisActivity() {
        finish();
        overridePendingTransition(R.anim.in_left, R.anim.out_right);
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    public void hideStatusBarAndNavigation(){
        WindowInsetsControllerCompat windowInsetsController;
        if (Build.VERSION.SDK_INT >= 30) {
            windowInsetsController = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        } else {
            windowInsetsController = new WindowInsetsControllerCompat(getWindow(), binding.getRoot());
        }

        if (windowInsetsController == null) {
            return;
        }
        windowInsetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars());
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars());
    }

    public void hideNavigation() {
        WindowInsetsControllerCompat windowInsetsController;
        if (Build.VERSION.SDK_INT >= 30) {
            windowInsetsController = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        } else {
            windowInsetsController = new WindowInsetsControllerCompat(getWindow(), binding.getRoot());
        }

        if (windowInsetsController == null) {
            return;
        }
        windowInsetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars());
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(i -> {
            if (i == 0) {
                new Handler().postDelayed(() -> {
                    WindowInsetsControllerCompat windowInsetsController1;
                    if (Build.VERSION.SDK_INT >= 30) {
                        windowInsetsController1 = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
                    } else {
                        windowInsetsController1 = new WindowInsetsControllerCompat(getWindow(), binding.getRoot());
                    }
                    Objects.requireNonNull(windowInsetsController1).hide(WindowInsetsCompat.Type.navigationBars());
                }, 3000);
            }
        });
    }

}
