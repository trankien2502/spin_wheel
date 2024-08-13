package com.tkt.spin_wheel.database;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.List;

public class StringListConverter {
    @TypeConverter
    public String fromList(List<String> list) {
        return String.join(",", list);
    }

    @TypeConverter
    public List<String> fromString(String value) {
        return Arrays.asList(value.split(","));
    }
}
