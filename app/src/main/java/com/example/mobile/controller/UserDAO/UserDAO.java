package com.example.mobile.controller.UserDAO;

import com.example.mobile.model.User;

public interface UserDAO {
    User checkCredentials(String username, String password);
}
