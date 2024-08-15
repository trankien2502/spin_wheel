package com.tkt.spin_wheel.ui.spin.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tkt.spin_wheel.databinding.ItemColorBinding;
import com.tkt.spin_wheel.ui.language.model.LanguageModel;
import com.tkt.spin_wheel.ui.spin.model.ColorEdit;

import java.util.List;
import java.util.Locale;

public class ColorEditAdapter extends RecyclerView.Adapter<ColorEditAdapter.ColorEditViewHolder> {
    Context context;
    List<ColorEdit> listColor;
    IOnClickColorEditListener iOnClickColorEditListener;
    public interface IOnClickColorEditListener{
        void iOnClickColorEdit(ColorEdit i);
    }

    public ColorEditAdapter(Context context, List<ColorEdit> listColor, IOnClickColorEditListener iOnClickColorEditListener) {
        this.context = context;
        this.listColor = listColor;
        this.iOnClickColorEditListener = iOnClickColorEditListener;
    }

    @NonNull
    @Override
    public ColorEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemColorBinding itemColorBinding = ItemColorBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ColorEditViewHolder(itemColorBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorEditViewHolder holder, int position) {
        ColorEdit colorEdit = listColor.get(position);
        assert colorEdit != null;
        int colorId = colorEdit.getIdColor();
        holder.itemColorBinding.ivColorEdit.setBackgroundColor(colorId);
        holder.itemColorBinding.itemColor.setOnClickListener(view -> {
            setCheck(colorId);
            iOnClickColorEditListener.iOnClickColorEdit(colorEdit);
            notifyDataSetChanged();
        });
        if (colorEdit.isSelect()) {
            holder.itemColorBinding.ivBorderColorSelect.setVisibility(View.VISIBLE);
        } else {
            holder.itemColorBinding.ivBorderColorSelect.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if (listColor!=null) return listColor.size();
        return 0;
    }

    public static class ColorEditViewHolder extends RecyclerView.ViewHolder{
        ItemColorBinding itemColorBinding;
        public ColorEditViewHolder(ItemColorBinding itemColorBinding) {
            super(itemColorBinding.getRoot());
            this.itemColorBinding = itemColorBinding;
        }
    }
    public void setCheck(int id) {
        for (ColorEdit item : listColor) {
            item.setSelect(id == item.getIdColor());
        }
        notifyDataSetChanged();
    }
}
