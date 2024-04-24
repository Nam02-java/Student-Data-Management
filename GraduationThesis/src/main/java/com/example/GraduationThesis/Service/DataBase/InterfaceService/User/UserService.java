package com.example.GraduationThesis.Service.DataBase.InterfaceService.User;

import com.example.GraduationThesis.Model.Enitity.Users.Users;

import java.util.List;

public interface UserService {
    Users getUserById(Long userId);

    Users findByUsername(String username);

    Users findByEmail(String email);

    Users findBynumberPhone(String numberphone);

    List<Users> getAllUsers();

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsBynumberPhone(String numberphone);

    boolean hasAdminRole(String username);

    Users save(Users users);

    void deleteById(Long ID);

    boolean checkAdminRole(Long userId);

}