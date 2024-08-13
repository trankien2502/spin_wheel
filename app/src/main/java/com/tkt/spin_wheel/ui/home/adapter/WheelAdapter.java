package com.tkt.spin_wheel.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.tkt.spin_wheel.R;
import com.tkt.spin_wheel.databinding.ItemWheelBinding;
import com.tkt.spin_wheel.ui.spin.model.WheelModel;

import java.util.List;

public class WheelAdapter extends RecyclerView.Adapter<WheelAdapter.WheelViewHolder> {
    List<WheelModel> wheelModelList;
    Context context;
    IOnClickWheelItem iOnClickWheelItem;
    IOnClickMenuItem iOnClickMenuItem;
    public interface IOnClickMenuItem{
        void onClickMenu(WheelModel wheelModel, int menuItemId);
    }
    public  interface IOnClickWheelItem {
        void onClickWheelItem(WheelModel wheelModel);
    }

    public WheelAdapter(Context context,List<WheelModel> wheelModelList,  IOnClickWheelItem iOnClickWheelItem, IOnClickMenuItem iOnClickMenuItem) {
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
        ItemWheelBinding itemWheelBinding = ItemWheelBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new WheelViewHolder(itemWheelBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull WheelViewHolder holder, int position) {
        WheelModel wheelModel = wheelModelList.get(position);
        holder.itemWheelBinding.tvName.setText(wheelModel.getName());
        holder.itemWheelBinding.tvSpin.setText(String.valueOf(wheelModel.getSpin()));
        holder.itemWheelBinding.tvUsed.setText(String.valueOf(wheelModel.getUsed()));
        holder.itemWheelBinding.wheel.setNumberOfItems(wheelModel.getNumberOfItems());
        holder.itemWheelBinding.wheel.setColors(wheelModel.getItemColor());
        holder.itemWheelBinding.wheel.setTextItems(wheelModel.getItemTexts());
        holder.itemWheelBinding.wheel.setRepeatOption(wheelModel.getRepeatOption());
        holder.itemWheelBinding.wheel.setTextSizeItem(4);
        holder.itemWheelBinding.clWheel.setOnClickListener(view ->  iOnClickWheelItem.onClickWheelItem(wheelModel));
        holder.itemWheelBinding.clWheelInformation.setOnClickListener(view ->  iOnClickWheelItem.onClickWheelItem(wheelModel));
        holder.itemWheelBinding.ivMenuItem.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.itemWheelBinding.ivMenuItem);
            popupMenu.getMenuInflater().inflate(R.menu.menu_wheel, popupMenu.getMenu());
            //popupMenu.getMenu().
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (iOnClickMenuItem != null) {
                        iOnClickMenuItem.onClickMenu(wheelModel, item.getItemId());
                    }
                    return true;
                }
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        if (wheelModelList!=null) return wheelModelList.size();
        return 0;
    }

    public static class WheelViewHolder extends RecyclerView.ViewHolder{
        ItemWheelBinding itemWheelBinding;
        public WheelViewHolder(ItemWheelBinding itemWheelBinding) {
            super(itemWheelBinding.getRoot());
            this.itemWheelBinding =itemWheelBinding;
        }
    }
}
