package com.example.GraduationThesis.Service.DataBase.ServiceImplements.Student;

import com.example.GraduationThesis.Model.Enitity.Student.Student;
import com.example.GraduationThesis.Model.Repository.StudentRepository.StudentRepository;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.Student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class StudentServiceImplenment implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Student> getStudentsByPartensnumberphone(String numberphone) {
        return studentRepository.findByPartensnumberphone(numberphone);
    }


    public boolean existsByUserName(String userName) {
        return studentRepository.existsByUsername(userName);
    }

    @Override
    public boolean existsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByNumberphone(String numberphone) {
        return studentRepository.existsByNumberphone(numberphone);
    }

    @Override
    public Student findStudentById(Long id) {
        return studentRepository.findStudentById(id);
    }

    @Override
    public void deleteById(Long ID) {
        studentRepository.deleteById(ID);
    }
}