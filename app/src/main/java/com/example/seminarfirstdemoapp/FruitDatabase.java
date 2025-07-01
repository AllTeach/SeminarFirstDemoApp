package com.example.seminarfirstdemoapp;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
@Database(entities = {FruitItem.class}, version = 1)
public abstract class FruitDatabase  extends RoomDatabase {
    private static volatile FruitDatabase INSTANCE;

    public abstract FruitItemDao fruitItemDao();

    public static FruitDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FruitDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    FruitDatabase.class, "fruit_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

