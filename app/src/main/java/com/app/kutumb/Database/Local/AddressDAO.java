package com.app.kutumb.Database.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.app.kutumb.Database.ModelDB.AddressTable;

import java.util.List;

@Dao
public interface AddressDAO {

    @Query("SELECT * FROM AddressTable")
    List<AddressTable> getAddressItems();

    @Query("SELECT * FROM AddressTable WHERE id=:id")
    List<AddressTable> getAddressItemById(int id);

    @Query("SELECT * FROM AddressTable WHERE address_name=:name")
    List<AddressTable> getAddress(String name);

    @Query("SELECT COUNT(*) from AddressTable")
    int countAddressItems();

    @Query("DELETE FROM AddressTable")
    void emptyAddress();

    @Query("SELECT address_name from AddressTable Limit 1")
    String nameAddress();

    @Insert
    void insertToAddress(AddressTable... address);

    @Update
    void updateAddress(AddressTable... address);

    @Delete
    void deleteAddressItem(AddressTable address);
}
