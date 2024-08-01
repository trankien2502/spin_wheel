package com.tkt.spin_wheel.ui.language;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;


import com.tkt.spin_wheel.base.BaseActivity;
import com.tkt.spin_wheel.ui.home.HomeActivity;
import com.tkt.spin_wheel.ui.language.adapter.LanguageStartAdapter;
import com.tkt.spin_wheel.ui.language.model.LanguageModel;
import com.tkt.spin_wheel.util.SPUtils;
import com.tkt.spin_wheel.util.SystemUtil;
import com.tkt.spin_wheel.R;
import com.tkt.spin_wheel.databinding.ActivityLanguageBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageActivity extends BaseActivity<ActivityLanguageBinding> {

    List<LanguageModel> listLanguage;
    String codeLang;
    String nameLang;

    @Override
    public ActivityLanguageBinding getBinding() {
        return ActivityLanguageBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        initData();
        codeLang = Locale.getDefault().getLanguage();
        binding.viewTop.ivGone.setVisibility(View.VISIBLE);
        binding.viewTop.tvTitle.setText(getString(R.string.language));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        LanguageStartAdapter languageAdapter = new LanguageStartAdapter(listLanguage, languageModel -> {
            codeLang = languageModel.getCode();
            nameLang = languageModel.getName();
        }, this);


        languageAdapter.setCheck(SystemUtil.getPreLanguage(getBaseContext()));

        binding.rcvLang.setLayoutManager(linearLayoutManager);
        binding.rcvLang.setAdapter(languageAdapter);
    }

    @Override
    public void bindView() {
        binding.viewTop.ivGone.setOnClickListener(view -> {
            SystemUtil.saveLocale(getBaseContext(), codeLang);
            SPUtils.setString(this,SPUtils.LANGUAGE,nameLang);
            startNextActivity(HomeActivity.class, null);
            finishAffinity();
        });
        binding.viewTop.ivBack.setOnClickListener(v -> onBackPressed());
    }

    private void initData() {
        listLanguage = new ArrayList<>();
        String lang = Locale.getDefault().getLanguage();
        listLanguage.add(new LanguageModel("English", "en", false));
        listLanguage.add(new LanguageModel("China", "zh", false));
        listLanguage.add(new LanguageModel("French", "fr", false));
        listLanguage.add(new LanguageModel("German", "de", false));
        listLanguage.add(new LanguageModel("Hindi", "hi", false));
        listLanguage.add(new LanguageModel("Indonesia", "in", false));
        listLanguage.add(new LanguageModel("Portuguese", "pt", false));
        listLanguage.add(new LanguageModel("Spanish", "es", false));

        for (int i = 0; i < listLanguage.size(); i++) {
            if (listLanguage.get(i).getCode().equals(lang)) {
                listLanguage.add(0, listLanguage.get(i));
                listLanguage.remove(i + 1);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishThisActivity();
    }
}
