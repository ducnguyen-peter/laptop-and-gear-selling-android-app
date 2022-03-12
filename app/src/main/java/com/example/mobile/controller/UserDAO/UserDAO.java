package com.example.mobile.controller.UserDAO;

import android.widget.EditText;

import com.example.mobile.model.user.User;

public interface UserDAO {
    void addUser(User user);
    void updateUser(String identity, String[] columns, String[] values);
    boolean checkLogin(EditText txtIdentity, EditText txtPassword);
    User getUser(EditText txtInput);
    boolean checkExistedUser(EditText txtInput);
}
