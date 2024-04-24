package com.example.GraduationThesis.Model.Enitity.GlobalConfig;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "global_config")
@Data
public class JWT {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "jwt_secret", unique = false, nullable = false)
    private String jwtSecret;

    @Column(name = "jwt_expiration", unique = false, nullable = false)
    private long jwtExpiration;

}
