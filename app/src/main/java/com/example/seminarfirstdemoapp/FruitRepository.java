package com.example.seminarfirstdemoapp;

import android.content.Context;
import java.util.List;

public class FruitRepository {
    private final FruitItemDao dao;

    public FruitRepository(Context context) {
        dao = FruitDatabase.getDatabase(context).fruitItemDao();
    }

    public interface FruitsCallback {
        void onResult(List<FruitItem> fruits);
    }

    public void getAllFruits(FruitsCallback callback) {
        new Thread(() -> {
            List<FruitItem> fruits = dao.getAllFruits();
            // You must call callback on main thread from Activity!
            if (callback != null) callback.onResult(fruits);
        }).start();
    }

    public void insertFruit(FruitItem fruit, Runnable onDone) {
        new Thread(() -> {
            dao.insertFruit(fruit);
            if (onDone != null) onDone.run();
        }).start();
    }

    public void deleteFruit(FruitItem fruit, Runnable onDone) {
        new Thread(() -> {
            dao.deleteFruit(fruit);
            if (onDone != null) onDone.run();
        }).start();
    }
}