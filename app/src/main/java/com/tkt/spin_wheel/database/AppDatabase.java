package com.tkt.spin_wheel.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.tkt.spin_wheel.ui.spin.model.WheelModel;


@Database(entities = {WheelModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "wheel.db";
    public static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context){
        if (instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class , DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract WheelDAO wheelDAO();
}
