package com.schoolt1.springbootsecurityjwt.service;

import com.schoolt1.springbootsecurityjwt.model.User;


public interface UserService {
    User save(User user);

    User create(User user);

    User getCurrentUser();

    void getAdmin();
}
