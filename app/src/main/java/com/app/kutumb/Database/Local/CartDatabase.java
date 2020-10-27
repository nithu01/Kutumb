package com.app.kutumb.Database.Local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.app.kutumb.Config.Constant;
import com.app.kutumb.Database.ModelDB.AddressTable;
import com.app.kutumb.Database.ModelDB.Cart;

@Database(entities = {Cart.class, AddressTable.class}, version = 1)
public abstract class CartDatabase extends RoomDatabase {

    public abstract CartDAO cartDAO();
    public abstract AddressDAO addressTableDao();
    private static CartDatabase instance;

    public static CartDatabase getInstance(Context context) {
        if(instance == null)
            instance = Room.databaseBuilder(context, CartDatabase.class, Constant.DATABASE)
                    .allowMainThreadQueries()
                    .build();
        return instance;
    }
}
