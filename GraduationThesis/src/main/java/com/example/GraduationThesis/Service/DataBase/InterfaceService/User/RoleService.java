package com.example.GraduationThesis.Service.DataBase.InterfaceService.User;

import com.example.GraduationThesis.Model.Enitity.Users.Roles.ERole;
import com.example.GraduationThesis.Model.Enitity.Users.Roles.Roles;

import java.util.Optional;

public interface RoleService {

    Optional<Roles> findByRoleName(ERole roleName);

}
