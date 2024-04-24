package com.example.GraduationThesis.Model.Enitity.Users;

import com.example.GraduationThesis.Model.Enitity.Users.Roles.Roles;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "numberphone", unique = true, nullable = false)
    private String numberPhone;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    /**
     * User_Roles
     * FetchType.LAZY
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "User_ID"), inverseJoinColumns = @JoinColumn(name = "Role_ID"))
    private Set<Roles> listRoles = new HashSet<>();
}
