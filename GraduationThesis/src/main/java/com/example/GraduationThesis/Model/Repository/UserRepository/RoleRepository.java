package com.example.GraduationThesis.Model.Repository.UserRepository;

import com.example.GraduationThesis.Model.Enitity.Users.Roles.ERole;
import com.example.GraduationThesis.Model.Enitity.Users.Roles.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer> {
    Optional<Roles> findByRoleName(ERole roleName);

}
