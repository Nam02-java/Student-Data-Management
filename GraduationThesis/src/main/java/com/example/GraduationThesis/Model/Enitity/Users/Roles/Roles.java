package com.example.GraduationThesis.Model.Enitity.Users.Roles;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Role_ID")
    private int roleId;

    @Column(name = "Role_Name")
    @Enumerated(EnumType.STRING)
    private ERole roleName;
}

