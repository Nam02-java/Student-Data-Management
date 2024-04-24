package com.example.GraduationThesis.Service.DataBase.ServiceImplements.User;

import com.example.GraduationThesis.Model.Enitity.Users.Roles.ERole;
import com.example.GraduationThesis.Model.Enitity.Users.Users;
import com.example.GraduationThesis.Model.Repository.UserRepository.UserRepository;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImplement implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public Users getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public Users findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Users findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Users findBynumberPhone(String numberphone) {
        return userRepository.findBynumberPhone(numberphone);
    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }


    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsBynumberPhone(String numberphone) {
        return userRepository.existsBynumberPhone(numberphone);
    }


    @Override
    public boolean hasAdminRole(String username) {
        Users user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getListRoles().stream()
                    .anyMatch(role -> role.getRoleName() == ERole.ROLE_ADMIN);
        }
        return false;
    }

    @Override
    public Users save(Users users) {
        return userRepository.save(users);
    }

    @Override
    public void deleteById(Long ID) {
        userRepository.deleteById(ID);
    }

    @Override
    public boolean checkAdminRole(Long userId) {

        Users user = userRepository.findById(userId).orElse(null);

        // check if the user exists and has the admin role
        return user != null && user.getListRoles() != null &&
                user.getListRoles().stream().anyMatch(role -> role.getRoleName().equals("ROLE_ADMIN"));
    }
}
