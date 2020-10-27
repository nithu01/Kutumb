package com.app.kutumb.Database.Local;

import com.app.kutumb.Database.DataSource.IAddressDataSource;
import com.app.kutumb.Database.ModelDB.AddressTable;

import java.util.List;

public class AddressDataSource implements IAddressDataSource {

    private AddressDAO addressTableDao;
    private static AddressDataSource instance;

    public AddressDataSource(AddressDAO addressTableDao) {
        this.addressTableDao = addressTableDao;
    }

    public static AddressDataSource getInstance(AddressDAO addressTableDao) {

        if(instance == null)
            instance = new AddressDataSource(addressTableDao);

        return instance;
    }

    @Override
    public List<AddressTable> getAddressItems() {
        return addressTableDao.getAddressItems();
    }

    @Override
    public List<AddressTable> getAddressItemById(int addressId) {
        return addressTableDao.getAddressItemById(addressId);
    }

    @Override
    public int countAddressItems() {
        return addressTableDao.countAddressItems();
    }

    @Override
    public void emptyAddress() {
        addressTableDao.emptyAddress();
    }

    @Override
    public void insertToAddress(AddressTable... address) {
        addressTableDao.insertToAddress(address);
    }

    @Override
    public String nameAddress() {
        return addressTableDao.nameAddress();
    }

    @Override
    public List<AddressTable> getAddress(String name) {
        return addressTableDao.getAddress(name);
    }


    @Override
    public void updateAddress(AddressTable... address) {
        addressTableDao.updateAddress(address);
    }

    @Override
    public void deleteAddressItem(AddressTable address) {
        addressTableDao.deleteAddressItem(address);
    }

}
