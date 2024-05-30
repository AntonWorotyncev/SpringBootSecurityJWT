package com.schoolt1.springbootsecurityjwt.service;

import com.schoolt1.springbootsecurityjwt.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    User save(User user);
    User create(User user);
//    User getByUsername(String username);
//    UserDetailsService userDetailsService();
    User getCurrentUser();
    void getAdmin();
}
