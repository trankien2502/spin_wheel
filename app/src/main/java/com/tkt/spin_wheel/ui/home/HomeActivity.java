package com.tkt.spin_wheel.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.tkt.spin_wheel.R;
import com.tkt.spin_wheel.base.BaseActivity;
import com.tkt.spin_wheel.database.AppDatabase;
import com.tkt.spin_wheel.database.WheelDAO;
import com.tkt.spin_wheel.databinding.ActivityHomeBinding;
import com.tkt.spin_wheel.dialog.add_wheel.IClickDialogWheelColor;
import com.tkt.spin_wheel.dialog.add_wheel.WheelColorDialog;
import com.tkt.spin_wheel.dialog.delete.DeleteDialog;
import com.tkt.spin_wheel.dialog.delete.IClickDialogDelete;
import com.tkt.spin_wheel.dialog.exit.ExitAppDialog;
import com.tkt.spin_wheel.dialog.exit.IClickDialogExit;
import com.tkt.spin_wheel.dialog.rate.IClickDialogRate;
import com.tkt.spin_wheel.dialog.rate.RatingDialog;
import com.tkt.spin_wheel.ui.coin.CoinFlipActivity;
import com.tkt.spin_wheel.ui.home.adapter.WheelAdapter;
import com.tkt.spin_wheel.ui.number.LuckyNumerActivity;
import com.tkt.spin_wheel.ui.setting.SettingActivity;
import com.tkt.spin_wheel.ui.spin.AddWheelActivity;
import com.tkt.spin_wheel.ui.spin.EditWheelActivity;
import com.tkt.spin_wheel.ui.spin.SpinningWheelActivity;
import com.tkt.spin_wheel.ui.spin.model.WheelModel;
import com.tkt.spin_wheel.util.EventTracking;
import com.tkt.spin_wheel.util.SPUtils;
import com.tkt.spin_wheel.util.SharePrefUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> {
    ArrayList<String> exitRate = new ArrayList<String>(Arrays.asList("2", "4", "6", "8", "10"));
    int newWheelColorId = 1;
    WheelAdapter adapter;
    List<WheelModel> wheelModelList;
    WheelDAO wheelDAO;

    @Override
    public ActivityHomeBinding getBinding() {
        return ActivityHomeBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        EventTracking.logEvent(this, "home_view");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rcvWheel.setLayoutManager(layoutManager);
        wheelDAO = AppDatabase.getInstance(this).wheelDAO();
        wheelModelList = AppDatabase.getInstance(this).wheelDAO().getAllWheelModel(true);
        if (wheelModelList.size()==0){
            binding.clNoSpin.setVisibility(View.VISIBLE);
        }else {
            binding.clNoSpin.setVisibility(View.GONE);
        }
        adapter = new WheelAdapter(this, wheelModelList, this::goToSpinningWheel, this::goToMenuItem);
        binding.rcvWheel.setAdapter(adapter);
    }

    @Override
    public void bindView() {
        binding.ivSetting.setOnClickListener(view -> {
            EventTracking.logEvent(this, "home_view");
            startNextActivity(SettingActivity.class, null);
        });
        binding.clAddWheel.setOnClickListener(view -> {
            EventTracking.logEvent(this, "home_add_wheel_click");
            addWheel();
        });
        binding.clCoinFlip.setOnClickListener(view -> {
            EventTracking.logEvent(this, "home_coin_flip_click");
            resultLauncher.launch(new Intent(this, CoinFlipActivity.class));
        });
        binding.clLuckyNumber.setOnClickListener(view -> {
            EventTracking.logEvent(this, "home_number_click");
            resultLauncher.launch(new Intent(this, LuckyNumerActivity.class));
        });
    }
    @SuppressLint("NonConstantResourceId")
    private void goToMenuItem(WheelModel wheelModel, int i) {
        EventTracking.logEvent(this, "home_avalabile_wheel_more_click");
        if (i == SPUtils.MORE_EDIT_ID) {
            EventTracking.logEvent(this, "home_avalabile_wheel_edit_click");
            Intent intent = new Intent(this, EditWheelActivity.class);
            intent.putExtra("WHEEL_EDIT_ID",wheelModel.getId());
            startActivity(intent);
        } else if (i == SPUtils.MORE_DUPLICATE_ID) {
            EventTracking.logEvent(this, "home_avalabile_wheel_duplicate_click");
            String name = wheelModel.getName();
            WheelModel dup_wheel_1 = wheelDAO.findByName(name+"(1)");
            WheelModel dup_wheel_2= wheelDAO.findByName(name+"(2)");
            WheelModel dup_wheel_3 = wheelDAO.findByName(name+"(3)");
            WheelModel dup_wheel_4 = wheelDAO.findByName(name+"(4)");
            WheelModel dup_wheel_5 = wheelDAO.findByName(name+"(5)");
            if (dup_wheel_5==null)wheelModel.setName(name+"(5)");
            if (dup_wheel_4==null)wheelModel.setName(name+"(4)");
            if (dup_wheel_3==null)wheelModel.setName(name+"(3)");
            if (dup_wheel_2==null)wheelModel.setName(name+"(2)");
            if (dup_wheel_1==null)wheelModel.setName(name+"(1)");
            WheelModel wheel_duplicated = new WheelModel(
                    wheelModel.getName(),
                    wheelModel.getNumberOfItems(),
                    wheelModel.getPriotity(),
                    wheelModel.getTypeColor(),
                    wheelModel.getFontSize(),wheelModel.getSpinSpeed(),
                    wheelModel.getRepeatOption(),0,0,wheelModel.getItemTexts(),wheelModel.getItemColor(),wheelModel.isActive()
            );
            if (!Objects.equals(wheel_duplicated.getName(), name)) {
                wheelDAO.insertAll(wheel_duplicated);
                wheelModelList = AppDatabase.getInstance(this).wheelDAO().getAllWheelModel(true);
                if (wheelModelList.size()==0){
                    binding.clNoSpin.setVisibility(View.VISIBLE);
                }else {
                    binding.clNoSpin.setVisibility(View.GONE);
                }
                adapter = new WheelAdapter(this, wheelModelList, this::goToSpinningWheel, this::goToMenuItem);
                binding.rcvWheel.setAdapter(adapter);
                Toast.makeText(this, getString(R.string.duplicate_success), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, getString(R.string.this_wheel_has_duplicated_out_of_maximum_5), Toast.LENGTH_SHORT).show();
            }
        } else if (i == SPUtils.MORE_TOP_ID) {
            EventTracking.logEvent(this, "home_avalabile_wheel_top_click");
            wheelModelList = AppDatabase.getInstance(this).wheelDAO().getAllWheelModel(true);
            for (int x = 0;x<wheelModelList.size();x++){
                WheelModel updatePriorityWheel = AppDatabase.getInstance(this).wheelDAO().findById(wheelModelList.get(x).getId());
                if (updatePriorityWheel.getPriotity()!=0){
                    updatePriorityWheel.setPriotity(updatePriorityWheel.getPriotity()-1);
                    wheelDAO.update(updatePriorityWheel);
                }
            }
            WheelModel updatePriorityWheel = AppDatabase.getInstance(this).wheelDAO().findById(wheelModel.getId());
            updatePriorityWheel.setPriotity(3);
            wheelDAO.update(updatePriorityWheel);

            wheelModelList = AppDatabase.getInstance(this).wheelDAO().getAllWheelModel(true);
            if (wheelModelList.size()==0){
                binding.clNoSpin.setVisibility(View.VISIBLE);
            }else {
                binding.clNoSpin.setVisibility(View.GONE);
            }
            adapter = new WheelAdapter(this, wheelModelList, this::goToSpinningWheel, this::goToMenuItem);
            binding.rcvWheel.setAdapter(adapter);
            Toast.makeText(this, getString(R.string.top_wheel_success), Toast.LENGTH_SHORT).show();
        } else if (i == SPUtils.MORE_DELETE_ID) {
            EventTracking.logEvent(this, "home_avalabile_wheel_delete_click");
            deleteWheel(wheelModel.getId());
        }

    }

    private void goToSpinningWheel(WheelModel wheelModel) {
        EventTracking.logEvent(this, "home_avalabile_wheel_click");
        Intent intent = new Intent(this, SpinningWheelActivity.class);
        intent.putExtra("WHEEL_ID", wheelModel.getId());
        startActivity(intent);


    }
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            //ads
            Log.d("activity_check", "home");
        }
    });

    private void rateApp() {
        RatingDialog ratingDialog = new RatingDialog(HomeActivity.this, true);
        ratingDialog.init(new IClickDialogRate() {
            @Override
            public void send() {
                //binding.rlRate.setVisibility(View.GONE);
                ratingDialog.dismiss();
                String uriText = "mailto:" + SharePrefUtils.email + "?subject=" + "Review for " + SharePrefUtils.subject + "&body=" + SharePrefUtils.subject + "\nRate : " + ratingDialog.getRating() + "\nContent: ";
                Uri uri = Uri.parse(uriText);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(uri);
                try {
                    finishAffinity();
                    startActivity(Intent.createChooser(sendIntent, getString(R.string.Send_Email)));
                    SharePrefUtils.forceRated(HomeActivity.this);
                    int star = SPUtils.getInt(HomeActivity.this, SPUtils.RATE_STAR, 0);
                    EventTracking.logEvent(HomeActivity.this, "rate_submit", "rate_star" + star, String.valueOf(star));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(HomeActivity.this, getString(R.string.There_is_no), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void rate() {
                ReviewManager manager = ReviewManagerFactory.create(HomeActivity.this);
                Task<ReviewInfo> request = manager.requestReviewFlow();
                request.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ReviewInfo reviewInfo = task.getResult();
                        Task<Void> flow = manager.launchReviewFlow(HomeActivity.this, reviewInfo);
                        flow.addOnSuccessListener(result -> {
                            //binding.rlRate.setVisibility(View.GONE);
                            int star = SPUtils.getInt(HomeActivity.this, SPUtils.RATE_STAR, 0);
                            EventTracking.logEvent(HomeActivity.this, "rate_submit", "rate_star" + star, String.valueOf(star));
                            SharePrefUtils.forceRated(HomeActivity.this);
                            ratingDialog.dismiss();
                            finishAffinity();
                        });
                    } else {
                        ratingDialog.dismiss();
                    }
                });
            }

            @Override
            public void later() {
                EventTracking.logEvent(HomeActivity.this, "rate_not_now");
                ratingDialog.dismiss();
                finishAffinity();
            }

        });
        ratingDialog.show();
        EventTracking.logEvent(this, "rate_show");
    }
    private void deleteWheel(int i){
        DeleteDialog dialog = new DeleteDialog(this,true);
        dialog.init(new IClickDialogDelete() {
            @Override
            public void cancel() {
                dialog.dismiss();
            }

            @Override
            public void okay() {
                WheelModel deleteWheelModel = AppDatabase.getInstance(getBaseContext()).wheelDAO().findById(i);
                AppDatabase.getInstance(getBaseContext()).wheelDAO().delete(deleteWheelModel);

                List<WheelModel> wheelModelList = AppDatabase.getInstance(getBaseContext()).wheelDAO().getAllWheelModel(true);
                if (wheelModelList.size()==0){
                    binding.clNoSpin.setVisibility(View.VISIBLE);
                }else {
                    binding.clNoSpin.setVisibility(View.GONE);
                }
                adapter.setWheelModelList(wheelModelList);
                binding.rcvWheel.setAdapter(adapter);
                Toast.makeText(HomeActivity.this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addWheel() {
        newWheelColorId = 1;
        WheelColorDialog wheelColorDialog = new WheelColorDialog(this, true);
        wheelColorDialog.binding.tvTitle.setText(R.string.choose_wheel_color);
        wheelColorDialog.init(new IClickDialogWheelColor() {
            @Override
            public void cancel() {
                wheelColorDialog.dismiss();
            }

            @Override
            public void okay() {
                EventTracking.logEvent(getBaseContext(), "wheel_color_select_click");
                wheelColorDialog.dismiss();
                Intent intent = new Intent(HomeActivity.this, AddWheelActivity.class);
                intent.putExtra("NEW_WHEEL_COLOR_ID", newWheelColorId);
                startActivity(intent);
                //finish();
            }

            @Override
            public int color(int i) {
                newWheelColorId = i;
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


    private void exitApp() {
        ExitAppDialog exitAppDialog = new ExitAppDialog(this, true);
        exitAppDialog.init(new IClickDialogExit() {
            @Override
            public void cancel() {
                exitAppDialog.dismiss();
            }

            @Override
            public void quit() {
                exitAppDialog.dismiss();
                finishAffinity();
            }
        });

        try {
            exitAppDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (!SharePrefUtils.isRated(this)) {
            if (exitRate.contains(String.valueOf(SharePrefUtils.getCountOpenApp(this)))) {
                rateApp();
            } else {
                exitApp();
            }
        } else {
            exitApp();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        List<WheelModel> wheelModelList = AppDatabase.getInstance(getBaseContext()).wheelDAO().getAllWheelModel(true);
        if (wheelModelList.size()==0){
            binding.clNoSpin.setVisibility(View.VISIBLE);
        }else {
            binding.clNoSpin.setVisibility(View.GONE);
        }
        adapter.setWheelModelList(wheelModelList);
        binding.rcvWheel.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        wheelModelList = AppDatabase.getInstance(this).wheelDAO().getAllWheelModel(true);
        if (wheelModelList.size()==0){
            binding.clNoSpin.setVisibility(View.VISIBLE);
        }else {
            binding.clNoSpin.setVisibility(View.GONE);
        }
        adapter = new WheelAdapter(this, wheelModelList, this::goToSpinningWheel, this::goToMenuItem);
        binding.rcvWheel.setAdapter(adapter);
    }
}
