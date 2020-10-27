package com.app.kutumb.Model;

/**
 * Created by aftab on 4/28/2018.
 */

public class UserData {

    private String id,email,phone,first_name,last_name;

    public UserData(String id, String email, String phone, String first_name, String last_name) {


        this.id=id;
        this.email=email;
        this.phone=phone;
        this.first_name=first_name;
        this.last_name=last_name;

    }

    public UserData() {

    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
