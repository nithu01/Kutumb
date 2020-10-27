package com.app.kutumb.Database.ModelDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class AddressTable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "address_name")
    public String address_name;

    @ColumnInfo(name = "first_name")
    public String first_name;

    @ColumnInfo(name = "last_name")
    public String last_name;

    @ColumnInfo(name = "pincode")
    public String pincode;

    @ColumnInfo(name = "address_line_one")
    public String address_line_one;

    @ColumnInfo(name = "address_line_two")
    public String address_line_two;

    @ColumnInfo(name = "city")
    public String city;

    @ColumnInfo(name = "state")
    public String state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddressname() {
        return address_name;
    }

    public void setAddressname(String addressname) {
        this.address_name = addressname;
    }

    public String getAddress_line_one() {
        return address_line_one;
    }

    public String getAddress_line_two() {
        return address_line_two;
    }

    public String getCity() {
        return city;
    }

    public String getFirstname() {
        return first_name;
    }

    public String getLastname() {
        return last_name;
    }

    public String getPincode() {
        return pincode;
    }

    public String getState() {
        return state;
    }

    public void setAddress_line_one(String address_line_one) {
        this.address_line_one = address_line_one;
    }

    public void setAddress_line_two(String address_line_two) {
        this.address_line_two = address_line_two;
    }

    public void setFirstname(String firstname) {
        this.first_name = firstname;
    }

    public void setLastname(String lastname) {
        this.last_name = lastname;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public void setState(String state) {
        this.state = state;
    }
}
