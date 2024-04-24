package com.example.GraduationThesis.Model.Repository.UserRepository;

import com.example.GraduationThesis.Model.Enitity.GlobalConfig.JWT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface JWTRepository extends JpaRepository<JWT, Long> {

    List<JWT> findAll();

}
