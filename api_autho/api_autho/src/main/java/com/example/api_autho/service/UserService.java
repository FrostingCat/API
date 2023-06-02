package com.example.api_autho.service;

import com.example.api_autho.model.User;

import java.util.List;

public interface UserService {
    void register(User user);

    List<User> getAll();

    User findByUsername(String username);

    User findByEmail(String email);

    User findById(Long id);

    boolean checkEmail(String email);
}
