package com.tkt.spin_wheel.ui.number;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tkt.spin_wheel.databinding.ItemNumberBinding;

import java.util.ArrayList;
import java.util.List;

public class LuckyNumberAdapter extends RecyclerView.Adapter<LuckyNumberAdapter.NumberViewHolder> {
    List<Integer> list;
    Context context;

    public LuckyNumberAdapter( Context context,List<Integer> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNumberBinding itemNumberBinding = ItemNumberBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new NumberViewHolder(itemNumberBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder holder, int position) {
        int number = list.get(position);
        holder.itemNumberBinding.tvNumber.setText(String.valueOf(number));
    }

    @Override
    public int getItemCount() {
        if (list!=null)
                return list.size();
        else return 0;
    }

    public static class NumberViewHolder extends RecyclerView.ViewHolder{
        ItemNumberBinding itemNumberBinding;
        public NumberViewHolder(ItemNumberBinding itemNumberBinding) {
            super(itemNumberBinding.getRoot());
            this.itemNumberBinding = itemNumberBinding;
        }
    }
}
