package com.app.kutumb.Database.DataSource;


import com.app.kutumb.Database.ModelDB.AddressTable;

import java.util.List;

public interface IAddressDataSource {

    List<AddressTable> getAddressItems();
    List<AddressTable> getAddressItemById(int addressId);
    int countAddressItems();
    void emptyAddress();
    void insertToAddress(AddressTable... address);
    void updateAddress(AddressTable... address);
    void deleteAddressItem(AddressTable address);
    String nameAddress();
    List<AddressTable>getAddress(String name);
}
