package com.example.GraduationThesis.Model.Repository.UserRepository;

import com.example.GraduationThesis.Model.Enitity.Users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);

    Users findByEmail(String email);

    Users findBynumberPhone(String numberphone);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsBynumberPhone(String numberphone);

    void deleteById(Long ID);
}