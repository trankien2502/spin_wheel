package com.tkt.spin_wheel.ui.splash;

import android.os.Handler;
import android.util.Log;

import com.tkt.spin_wheel.R;
import com.tkt.spin_wheel.base.BaseActivity;
import com.tkt.spin_wheel.database.AppDatabase;
import com.tkt.spin_wheel.database.WheelDAO;
import com.tkt.spin_wheel.databinding.ActivitySplashBinding;
import com.tkt.spin_wheel.ui.language.LanguageStartActivity;
import com.tkt.spin_wheel.ui.spin.model.WheelModel;
import com.tkt.spin_wheel.util.EventTracking;
import com.tkt.spin_wheel.util.SharePrefUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class SplashActivity extends BaseActivity<ActivitySplashBinding> {


    @Override
    public ActivitySplashBinding getBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        EventTracking.logEvent(this,"splash_open");
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

    private void initDatabase() {
        if (SharePrefUtils.getCountOpenApp(this) != 1) return;
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

        List<String> whatToEat = Arrays.asList("\uD83E\uDD90 Pad Thai","\uD83C\uDF2E Tacos","\uD83E\uDD5F Dim Sum","\uD83C\uDF54 Hamburger",
                "\uD83E\uDD57 Salad","\uD83C\uDF5D Pasta","\uD83E\uDD80 Seafood"," \uD83E\uDD58 Tomyum","\uD83C\uDF2D Hot dog","\uD83C\uDF63 Sushi","\uD83C\uDF65Sashimi","\uD83C\uDF5C Pho","\uD83C\uDF55 Pizza");
        List<Integer> colorEat = Arrays.asList(
                getBaseContext().getResources().getColor(R.color.eat_color_1),
                getBaseContext().getResources().getColor(R.color.eat_color_2),
                getBaseContext().getResources().getColor(R.color.eat_color_3),
                getBaseContext().getResources().getColor(R.color.eat_color_4),
                getBaseContext().getResources().getColor(R.color.eat_color_5),
                getBaseContext().getResources().getColor(R.color.eat_color_1),
                getBaseContext().getResources().getColor(R.color.eat_color_2),
                getBaseContext().getResources().getColor(R.color.eat_color_3),
                getBaseContext().getResources().getColor(R.color.eat_color_4),
                getBaseContext().getResources().getColor(R.color.eat_color_5),
                getBaseContext().getResources().getColor(R.color.eat_color_1),
                getBaseContext().getResources().getColor(R.color.eat_color_2),
                getBaseContext().getResources().getColor(R.color.eat_color_3)
        );
        List<String> yesNo = Arrays.asList("NO","YES");
        List<Integer> yesNoColor = Arrays.asList(getBaseContext().getResources().getColor(R.color.no_color_home),
                getBaseContext().getResources().getColor(R.color.yes_color_home)
        );
        List<String> afternoon = Arrays.asList("\uD83E\uDDD8 Yoga","\uD83C\uDFE1 Gardening","\uD83D\uDCFA Watch TV","\uD83D\uDCD6 Read","\uD83C\uDFD5\uFE0F Camping","\uD83C\uDFCB\uFE0F Workout",
                " \uD83C\uDFA8 Painting","\uD83E\uDDF9 Cleaning","\uD83E\uDEA9 Party","\uD83D\uDC83 Dance","\uD83C\uDF73 Cooking","\uD83C\uDFAE Game");
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

        List<String> itemTextSixColor = new ArrayList<>(Collections.nCopies(6, "\n"));
        List<String> itemTextTwelveColor = new ArrayList<>(Collections.nCopies(12, "\n"));
        List<String> itemTextTwoColor = new ArrayList<>(Collections.nCopies(2, "\n"));
        Log.d("spinCheck","size :"+ itemTextSixColor.size());

        wheelDAO.insertAll(new WheelModel("Standar_d__", 12,0, 1, 2, 2, 1, 0, 0, itemTextTwelveColor, standard, false));
        wheelDAO.insertAll(new WheelModel("Six_Colo_r_", 6,0, 2, 2, 2, 2, 0, 0, itemTextSixColor, sixColor, false));
        wheelDAO.insertAll(new WheelModel("Vintag_e__", 12,0, 3, 2, 2, 1, 0, 0, itemTextTwelveColor, vintage, false));
        wheelDAO.insertAll(new WheelModel("Two_Colo_r_", 2,0, 4, 2, 2, 6, 0, 0, itemTextTwoColor, twoColor, false));
        wheelDAO.insertAll(new WheelModel("Paste_l__", 12,0, 5, 2, 2, 1, 0, 0, itemTextTwelveColor, pastel, false));

        wheelDAO.insertAll(new WheelModel("What to eat", 13,3, 0, 3, 2, 1, 0, 0, whatToEat, colorEat, true));
        wheelDAO.insertAll(new WheelModel("Yes Or NO?", 2,2, 0, 3, 2, 5, 0, 0, yesNo, yesNoColor, true));
        wheelDAO.insertAll(new WheelModel("Afternoon Activity", 12,1, 5, 3, 2, 1, 0, 0, afternoon, pastel, true));
    }
}
