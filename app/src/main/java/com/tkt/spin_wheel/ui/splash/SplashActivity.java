package com.tkt.spin_wheel.ui.splash;

import android.os.Handler;

import com.tkt.spin_wheel.R;
import com.tkt.spin_wheel.base.BaseActivity;
import com.tkt.spin_wheel.database.AppDatabase;
import com.tkt.spin_wheel.database.WheelDAO;
import com.tkt.spin_wheel.ui.language.LanguageStartActivity;
import com.tkt.spin_wheel.ui.spin.model.WheelModel;
import com.tkt.spin_wheel.util.SharePrefUtils;
import com.tkt.spin_wheel.databinding.ActivitySplashBinding;

import java.util.Arrays;
import java.util.List;


public class SplashActivity extends BaseActivity<ActivitySplashBinding> {


    @Override
    public ActivitySplashBinding getBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

        SharePrefUtils.increaseCountOpenApp(this);
        initDatabase();
        new Handler().postDelayed(() -> {
            startNextActivity(LanguageStartActivity.class, null);
            finishAffinity();
        }, 3000);

    }

    @Override
    public void bindView() {

    }

    @Override
    public void onBackPressed() {
    }
    private void initDatabase(){
        if (SharePrefUtils.getCountOpenApp(this)!=1) return;
        WheelDAO wheelDAO = AppDatabase.getInstance(this).wheelDAO();
        List<Integer> standard = Arrays.asList(
                getBaseContext().getResources().getColor(R.color.standard_1),
                getBaseContext().getResources().getColor(R.color.standard_2),
                getBaseContext().getResources().getColor(R.color.standard_3),
                getBaseContext().getResources().getColor(R.color.standard_4),
                getBaseContext().getResources().getColor(R.color.standard_5),
                getBaseContext().getResources().getColor(R.color.standard_6),
                getBaseContext().getResources().getColor(R.color.standard_7),
                getBaseContext().getResources().getColor(R.color.standard_8),
                getBaseContext().getResources().getColor(R.color.standard_9),
                getBaseContext().getResources().getColor(R.color.standard_10),
                getBaseContext().getResources().getColor(R.color.standard_11),
                getBaseContext().getResources().getColor(R.color.standard_12));
        List<Integer> vintage = Arrays.asList(
                getBaseContext().getResources().getColor(R.color.vintage_1),
                getBaseContext().getResources().getColor(R.color.vintage_2),
                getBaseContext().getResources().getColor(R.color.vintage_3),
                getBaseContext().getResources().getColor(R.color.vintage_4),
                getBaseContext().getResources().getColor(R.color.vintage_5),
                getBaseContext().getResources().getColor(R.color.vintage_6),
                getBaseContext().getResources().getColor(R.color.vintage_7),
                getBaseContext().getResources().getColor(R.color.vintage_8),
                getBaseContext().getResources().getColor(R.color.vintage_9),
                getBaseContext().getResources().getColor(R.color.vintage_10),
                getBaseContext().getResources().getColor(R.color.vintage_11),
                getBaseContext().getResources().getColor(R.color.vintage_12));
        List<Integer> sixColor = Arrays.asList(
                getBaseContext().getResources().getColor(R.color.color_1),
                getBaseContext().getResources().getColor(R.color.color_2),
                getBaseContext().getResources().getColor(R.color.color_3),
                getBaseContext().getResources().getColor(R.color.color_4),
                getBaseContext().getResources().getColor(R.color.color_5),
                getBaseContext().getResources().getColor(R.color.color_6)
        );
        List<Integer> twoColor = Arrays.asList(
                getBaseContext().getResources().getColor(R.color.no_color),
                getBaseContext().getResources().getColor(R.color.yes_color)
        );

        List<String> whatToEat = Arrays.asList("Pad Thai","Tacos","Dim Sum","Hamburger","Salad","Pasta","Sea Food","Tomyum","Hot dog","Sushi","Sashimi","Pho","Pizza");
        List<Integer> colorEat = Arrays.asList(getBaseContext().getResources().getColor(R.color.eat_color_1),
                getBaseContext().getResources().getColor(R.color.eat_color_2),
                getBaseContext().getResources().getColor(R.color.eat_color_3),
                getBaseContext().getResources().getColor(R.color.eat_color_4),
                getBaseContext().getResources().getColor(R.color.eat_color_5)
                );
        List<String> yesNo = Arrays.asList("NO","YES");
        List<Integer> yesNoColor = Arrays.asList(getBaseContext().getResources().getColor(R.color.no_color_home),
                getBaseContext().getResources().getColor(R.color.yes_color_home)
        );
        List<String> itemTexts = Arrays.asList("", "", "", "", "",
                "", "", "", "", "", "", "");
        List<String> afternoon = Arrays.asList("Yoga","Gardening","Watch TV","Read","Camping","Workout","Painting","Cleaning","Party","Dance","Cooking","Game");
        List<Integer> pastel = Arrays.asList(getBaseContext().getResources().getColor(R.color.pastel_1),
                getBaseContext().getResources().getColor(R.color.pastel_2),
                getBaseContext().getResources().getColor(R.color.pastel_3),
                getBaseContext().getResources().getColor(R.color.pastel_4),
                getBaseContext().getResources().getColor(R.color.pastel_5),
                getBaseContext().getResources().getColor(R.color.pastel_6),
                getBaseContext().getResources().getColor(R.color.pastel_7),
                getBaseContext().getResources().getColor(R.color.pastel_8),
                getBaseContext().getResources().getColor(R.color.pastel_9),
                getBaseContext().getResources().getColor(R.color.pastel_10),
                getBaseContext().getResources().getColor(R.color.pastel_11),
                getBaseContext().getResources().getColor(R.color.pastel_12));
        wheelDAO.insertAll(new WheelModel("Standard__",12,0,3,1,1,0,0,itemTexts,standard,false));
        wheelDAO.insertAll(new WheelModel("Six_Color_",6,0,3,1,2,0,0,itemTexts,sixColor,false));
        wheelDAO.insertAll(new WheelModel("Vintage__",12,0,3,1,1,0,0,itemTexts,vintage,false));
        wheelDAO.insertAll(new WheelModel("Two_Color_",2,0,3,1,6,0,0,itemTexts,twoColor,false));
        wheelDAO.insertAll(new WheelModel("Pastel__",12,0,3,1,1,0,0,itemTexts,pastel,false));

        wheelDAO.insertAll(new WheelModel("What to eat",13,0,8,1,1,0,0,whatToEat,colorEat,true));
        wheelDAO.insertAll(new WheelModel("Yes Or NO?",2,0,8,1,5,0,0,yesNo,yesNoColor,true));
        wheelDAO.insertAll(new WheelModel("Afternoon Activity",12,5,8,1,1,0,0,afternoon,pastel,true));
    }
}
