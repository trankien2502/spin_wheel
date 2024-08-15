package com.tkt.spin_wheel.ui.home.adapter;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.tkt.spin_wheel.R;
import com.tkt.spin_wheel.databinding.ItemWheelBinding;
import com.tkt.spin_wheel.ui.spin.model.WheelModel;
import com.tkt.spin_wheel.util.SPUtils;

import java.util.List;

public class WheelAdapter extends RecyclerView.Adapter<WheelAdapter.WheelViewHolder> {
    List<WheelModel> wheelModelList;
    Context context;
    IOnClickWheelItem iOnClickWheelItem;
    IOnClickMenuItem iOnClickMenuItem;

    public interface IOnClickMenuItem {
        void onClickMenu(WheelModel wheelModel, int menuItemId);
    }

    public interface IOnClickWheelItem {
        void onClickWheelItem(WheelModel wheelModel);
    }

    public WheelAdapter(Context context, List<WheelModel> wheelModelList, IOnClickWheelItem iOnClickWheelItem, IOnClickMenuItem iOnClickMenuItem) {
        this.wheelModelList = wheelModelList;
        this.context = context;
        this.iOnClickWheelItem = iOnClickWheelItem;
        this.iOnClickMenuItem = iOnClickMenuItem;
    }

    public void setWheelModelList(List<WheelModel> wheelModelList) {
        this.wheelModelList = wheelModelList;
    }

    @NonNull
    @Override
    public WheelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWheelBinding itemWheelBinding = ItemWheelBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WheelViewHolder(itemWheelBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull WheelViewHolder holder, int position) {
        WheelModel wheelModel = wheelModelList.get(position);
        if (wheelModel.getLastUsed() != 0) {
            String used;
            long elapsedTime = System.currentTimeMillis() - wheelModel.getLastUsed();
            long days = elapsedTime / (1000 * 60 * 60 * 24);
            long seconds = (elapsedTime / 1000) % 60;
            long minutes = (elapsedTime / (1000 * 60)) % 60;
            long hours = (elapsedTime / (1000 * 60 * 60)) % 24;
            used = "Just used";
            if (seconds != 0) used = seconds + " " + "seconds";
            if (minutes != 0) used = minutes + " " + "minutes";
            if (hours != 0) used = hours + " " + "hours";
            if (days != 0) used = days + " " + "days";
            holder.itemWheelBinding.tvUsed.setText(used);
        } else {
            holder.itemWheelBinding.tvUsed.setText(String.valueOf(0));
        }

        holder.itemWheelBinding.tvName.setText(wheelModel.getName());
        holder.itemWheelBinding.tvSpin.setText(String.valueOf(wheelModel.getSpin()));

        holder.itemWheelBinding.wheel.setNumberOfItems(wheelModel.getNumberOfItems());
        holder.itemWheelBinding.wheel.setColors(wheelModel.getItemColor());
        holder.itemWheelBinding.wheel.setTextItems(wheelModel.getItemTexts());
        holder.itemWheelBinding.wheel.setRepeatOption(wheelModel.getRepeatOption());
        holder.itemWheelBinding.wheel.setTextSizeItem(1);
        holder.itemWheelBinding.clWheel.setOnClickListener(view -> iOnClickWheelItem.onClickWheelItem(wheelModel));
        holder.itemWheelBinding.clWheelInformation.setOnClickListener(view -> iOnClickWheelItem.onClickWheelItem(wheelModel));
        holder.itemWheelBinding.ivMenuItem.setOnClickListener(view -> {
//            PopupMenu popupMenu = new PopupMenu(context, holder.itemWheelBinding.ivMenuItem);
//            popupMenu.getMenuInflater().inflate(R.menu.menu_wheel, popupMenu.getMenu());
//            //popupMenu.getMenu().
//            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    if (iOnClickMenuItem != null) {
//                        iOnClickMenuItem.onClickMenu(wheelModel, item.getItemId());
//                    }
//                    return true;
//                }
//            });
//            popupMenu.show();
            LayoutInflater inflater = null;

            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

            View popupView = inflater.inflate(R.layout.popup_more, null);


            int width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

            //popupWindow.showAsDropDown(view, 0, 0); // or use showAtLocation

            popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int popupHeight = popupView.getMeasuredHeight();

            // Get the location of the view on the screen
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            int viewX = location[0];
            int viewY = location[1];

            // Get the screen height
            DisplayMetrics displayMetrics = new DisplayMetrics();
            displayMetrics = context.getResources().getDisplayMetrics();
            int screenHeight = displayMetrics.heightPixels;

            if ((viewY + popupHeight) > screenHeight) {
                // Show the popup above the view if it would go off screen below
                popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, viewX, viewY - popupHeight);
            } else {
                // Otherwise, show it below the view
                popupWindow.showAsDropDown(view, 0, 0);
            }

            popupView.findViewById(R.id.clEdit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iOnClickMenuItem.onClickMenu(wheelModel, SPUtils.MORE_EDIT_ID);
                    popupWindow.dismiss();
                }
            });
            popupView.findViewById(R.id.clDup).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iOnClickMenuItem.onClickMenu(wheelModel, SPUtils.MORE_DUPLICATE_ID);
                    popupWindow.dismiss();
                }
            });
            popupView.findViewById(R.id.clTop).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iOnClickMenuItem.onClickMenu(wheelModel, SPUtils.MORE_TOP_ID);
                    popupWindow.dismiss();
                }
            });
            popupView.findViewById(R.id.clDel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iOnClickMenuItem.onClickMenu(wheelModel, SPUtils.MORE_DELETE_ID);
                    popupWindow.dismiss();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        if (wheelModelList != null) return wheelModelList.size();
        return 0;
    }

    public static class WheelViewHolder extends RecyclerView.ViewHolder {
        ItemWheelBinding itemWheelBinding;

        public WheelViewHolder(ItemWheelBinding itemWheelBinding) {
            super(itemWheelBinding.getRoot());
            this.itemWheelBinding = itemWheelBinding;
        }
    }
}
