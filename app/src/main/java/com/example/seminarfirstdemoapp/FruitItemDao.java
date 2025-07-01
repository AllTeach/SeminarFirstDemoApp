package com.example.seminarfirstdemoapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface FruitItemDao {
    @Insert
    void insertFruit(FruitItem fruit);

    @Query("SELECT * FROM FruitItem")
    List<FruitItem> getAllFruits();

    @Delete
    void deleteFruit(FruitItem fruitItem);
}