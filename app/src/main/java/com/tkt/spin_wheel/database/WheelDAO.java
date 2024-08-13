package com.tkt.spin_wheel.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.tkt.spin_wheel.ui.spin.model.WheelModel;

import java.util.List;


@Dao
public interface WheelDAO {
    @Query("select * from wheel where isActive == (:active)")
    List<WheelModel> getAllWheelModel(boolean active);

    @Query("SELECT * FROM wheel WHERE name == (:wheelName)")
    WheelModel findByName(String wheelName);

    @Query("SELECT * FROM wheel WHERE id == (:id)")
    WheelModel findById(int id);


    @Query("DELETE FROM wheel")
    void deleteAllWheel();


    @Insert
    void insertAll(WheelModel... wheelModels);

    @Update
    void update(WheelModel wheelModel);

    @Delete
    void delete(WheelModel wheelModel);
}
