package com.tkt.spin_wheel.ui.spin.model;

public class ColorEdit {
    int idColor;
    boolean isSelect;

    public ColorEdit(int idColor, boolean isSelect) {
        this.idColor = idColor;
        this.isSelect = isSelect;
    }

    public int getIdColor() {
        return idColor;
    }

    public void setIdColor(int idColor) {
        this.idColor = idColor;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
