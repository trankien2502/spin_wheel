package com.tkt.spin_wheel.ui.setting;

import android.view.View;

import com.tkt.spin_wheel.R;
import com.tkt.spin_wheel.base.BaseActivity;
import com.tkt.spin_wheel.ui.policy.PolicyActivity;
import com.tkt.spin_wheel.util.EventTracking;
import com.tkt.spin_wheel.databinding.ActivityAboutBinding;

public class AboutActivity extends BaseActivity<ActivityAboutBinding> {

    @Override
    public ActivityAboutBinding getBinding() {
        return ActivityAboutBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        EventTracking.logEvent(AboutActivity.this,"view_about_app");
        binding.clHeader.ivGone.setVisibility(View.INVISIBLE);
        binding.clHeader.tvTitle.setText(R.string.about);
    }

    @Override
    public void bindView() {
        binding.privacy.setOnClickListener(view -> {
            startNextActivity(PolicyActivity.class,null);
        });
        binding.clHeader.ivBack.setOnClickListener(view -> onBackPressed());
    }
}