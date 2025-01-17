package com.tkt.spin_wheel.ui.spin.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.tkt.spin_wheel.database.IntegerListConverter;
import com.tkt.spin_wheel.database.StringListConverter;

import java.util.List;

@Entity(tableName = "wheel")
public class WheelModel {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int numberOfItems;
    private int fontSize;
    private int repeatOption;
    private int spinSpeed;
    @TypeConverters(StringListConverter.class)
    public List<String> itemTexts;
    @TypeConverters(IntegerListConverter.class)
    public List<Integer> itemColor;
    private int spin;
    private long lastUsed;
    int typeColor;
    int priotity;
    private boolean isActive;

    public WheelModel() {
    }

    public WheelModel(int id,String name, int fontSize, int spinSpeed, int spin, long lastUsed) {
        this.name = name;
        this.id = id;
        this.fontSize = fontSize;
        this.spinSpeed = spinSpeed;
        this.spin = spin;
        this.lastUsed = lastUsed;
    }

    public WheelModel(int id, String name,int numberOfItems,int priotity,int typeColor, int fontSize, int spinSpeed,int repeatOption, int spin, long lastUsed, List<String> itemTexts, List<Integer> itemColor, boolean isActive) {
        this.id = id;
        this.name = name;
        this.repeatOption = repeatOption;
        this.numberOfItems = numberOfItems;
        this.fontSize = fontSize;
        this.spinSpeed = spinSpeed;
        this.itemTexts = itemTexts;
        this.itemColor = itemColor;
        this.typeColor = typeColor;
        this.priotity = priotity;
        this.spin = spin;
        this.lastUsed = lastUsed;
        this.isActive = isActive;
    }
    public WheelModel(String name,int numberOfItems,int priotity,int typeColor, int fontSize,int spinSpeed,int repeatOption, int spin, long lastUsed,  List<String> itemTexts, List<Integer> itemColor, boolean isActive) {
        this.name = name;
        this.numberOfItems = numberOfItems;
        this.repeatOption = repeatOption;
        this.fontSize = fontSize;
        this.spinSpeed = spinSpeed;
        this.itemTexts = itemTexts;
        this.typeColor = typeColor;
        this.itemColor = itemColor;
        this.spin = spin;
        this.priotity = priotity;
        this.lastUsed = lastUsed;
        this.isActive = isActive;
    }

    public long getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(long lastUsed) {
        this.lastUsed = lastUsed;
    }

    public int getPriotity() {
        return priotity;
    }

    public void setPriotity(int priotity) {
        this.priotity = priotity;
    }

    public int getTypeColor() {
        return typeColor;
    }

    public void setTypeColor(int typeColor) {
        this.typeColor = typeColor;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getRepeatOption() {
        return repeatOption;
    }

    public void setRepeatOption(int repeatOption) {
        this.repeatOption = repeatOption;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public int getId() {
        return id;
    }

    public int getSpin() {
        return spin;
    }

    public void setSpin(int spin) {
        this.spin = spin;
    }



    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getSpinSpeed() {
        return spinSpeed;
    }

    public void setSpinSpeed(int spinSpeed) {
        this.spinSpeed = spinSpeed;
    }

    public List<String> getItemTexts() {
        return itemTexts;
    }

    public void setItemTexts(List<String> itemTexts) {
        this.itemTexts = itemTexts;
    }

    public List<Integer> getItemColor() {
        return itemColor;
    }

    public void setItemColor(List<Integer> itemColor) {
        this.itemColor = itemColor;
    }
}
