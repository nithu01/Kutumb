package com.app.kutumb.Database.DataSource;

import com.app.kutumb.Database.ModelDB.AddressTable;

import java.util.List;

public class AddressTableRepositories implements IAddressDataSource {

    private IAddressDataSource iAddressDataSource;

    public AddressTableRepositories(IAddressDataSource iAddressDataSource) {
        this.iAddressDataSource = iAddressDataSource;
    }

    private static AddressTableRepositories instance;

    public static AddressTableRepositories getInstance(IAddressDataSource iAddressDataSource) {

        if(instance == null)
            instance = new AddressTableRepositories(iAddressDataSource);

        return instance;
    }

    @Override
    public List<AddressTable> getAddressItems() {
        return iAddressDataSource.getAddressItems();
    }

    @Override
    public List<AddressTable> getAddressItemById(int addressId) {
        return iAddressDataSource.getAddressItemById(addressId);
    }

    @Override
    public List<AddressTable> getAddress(String name) {
        return iAddressDataSource.getAddress(name);
    }

    @Override
    public int countAddressItems() {
        return iAddressDataSource.countAddressItems();
    }

    @Override
    public void emptyAddress() {
        iAddressDataSource.emptyAddress();
    }

    @Override
    public void insertToAddress(AddressTable... address) {
        iAddressDataSource.insertToAddress(address);
    }

    @Override
    public void updateAddress(AddressTable... address) {
        iAddressDataSource.updateAddress(address);
    }


    @Override
    public String nameAddress() {
        return iAddressDataSource.nameAddress();
    }


    @Override
    public void deleteAddressItem(AddressTable address) {
        iAddressDataSource.deleteAddressItem(address);
    }
}
