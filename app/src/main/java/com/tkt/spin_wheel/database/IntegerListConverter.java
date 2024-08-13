package com.tkt.spin_wheel.database;

import android.os.Build;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IntegerListConverter {
    @TypeConverter
    public String fromList(List<Integer> list) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return list.stream().map(String::valueOf).collect(Collectors.joining(","));
        } else {
            return null;
        }
    }

    @TypeConverter
    public List<Integer> fromString(String value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Arrays.stream(value.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        } else return null;
    }
}

