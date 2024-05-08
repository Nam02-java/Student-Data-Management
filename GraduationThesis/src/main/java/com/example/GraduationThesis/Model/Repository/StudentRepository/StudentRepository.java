package com.example.GraduationThesis.Model.Repository.StudentRepository;

import com.example.GraduationThesis.Model.Enitity.Student.Student;
import com.example.GraduationThesis.Model.Enitity.Users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByPartensnumberphone(String numberphone);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Student findByEmail(String email);

    boolean existsByNumberphone(String numberphone);

    Student findBynumberphone(String numberphone);


    Student findStudentById(Long id);

    void deleteById(Long ID);


}