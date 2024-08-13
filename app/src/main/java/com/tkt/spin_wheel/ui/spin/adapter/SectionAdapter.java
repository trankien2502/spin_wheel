package com.tkt.spin_wheel.ui.spin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tkt.spin_wheel.R;
import com.tkt.spin_wheel.databinding.ItemSectionBinding;
import com.tkt.spin_wheel.ui.spin.model.WheelModel;

import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionViewHolder> {

    Context context;
    List<String> itemTexts;
    List<Integer> color;
    IonClickSectionListener ionClickSectionListener;


    public SectionAdapter(Context context, List<String> itemTexts, List<Integer> color, IonClickSectionListener iOnClickSectionListener) {
        this.context = context;
        this.itemTexts = itemTexts;
        this.color = color;
        this.ionClickSectionListener = iOnClickSectionListener;
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSectionBinding itemSectionBinding =ItemSectionBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new SectionViewHolder(itemSectionBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String itemSectionName = itemTexts.get(position);
        int colorSection = color.get(position%color.size());
        holder.itemSectionBinding.ivColorEdit.setBackgroundColor(colorSection);
        holder.itemSectionBinding.edtSectionName.setText(itemSectionName);

        holder.itemSectionBinding.ivDeleteSection.setOnClickListener(view -> ionClickSectionListener.delete(position));
        holder.itemSectionBinding.ivColorEdit.setOnClickListener(view -> ionClickSectionListener.editColor(position));
        holder.itemSectionBinding.edtSectionName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    // Ẩn bàn phím
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    }
                    ionClickSectionListener.editText(position,holder.itemSectionBinding.edtSectionName.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (itemTexts!=null) return itemTexts.size();
        return 0;
    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder{
        ItemSectionBinding itemSectionBinding;
        public SectionViewHolder(ItemSectionBinding itemSectionBinding) {
            super(itemSectionBinding.getRoot());
            this.itemSectionBinding = itemSectionBinding;
        }
    }
}
