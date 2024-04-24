package com.example.GraduationThesis.Service.DataBase.ServiceImplements.User;

import com.example.GraduationThesis.Model.Enitity.Users.Roles.ERole;
import com.example.GraduationThesis.Model.Enitity.Users.Roles.Roles;
import com.example.GraduationThesis.Model.Repository.UserRepository.RoleRepository;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.User.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class RoleServiceImplement implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Optional<Roles> findByRoleName(ERole roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
