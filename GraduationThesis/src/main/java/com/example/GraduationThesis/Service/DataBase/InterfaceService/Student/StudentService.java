package com.example.GraduationThesis.Service.DataBase.InterfaceService.Student;

import com.example.GraduationThesis.Model.Enitity.Student.Student;

import java.util.List;

public interface StudentService {
    Student saveStudent(Student student);

    List<Student> getAllStudents();

    Student getStudentById(Long id);

    List<Student> getStudentsByPartensnumberphone(String phoneNumber);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);


    Student findByEmail(String email);


    boolean existsByNumberphone(String numberphone);

    Student findBynumberPhone(String numberphone);

    Student findStudentById(Long id);


    void deleteById(Long ID);

}