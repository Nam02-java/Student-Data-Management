package com.example.GraduationThesis.Service.API.ServiceImplenments.Admin.CRUD.Delete;

import com.example.GraduationThesis.Model.Enitity.Student.Student;
import com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Delete.AdminServiceDeleteAPI;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.Student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service("DeleteStudentByIdImplementation")
public class DeleteStudentByIdImplementation implements AdminServiceDeleteAPI {

    @Autowired
    private StudentService studentService;

    @Override
    public ResponseEntity<?> deleteStudentById(@PathVariable Long studentId) {
        Student student = studentService.findStudentById(studentId);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
        }

        /**
         * this method will delete all information of student (information , scores and conduct)
         * don't need create deleteSubjects , deleteConduct in services
         */
        studentService.deleteById(student.getId());

        return ResponseEntity.ok("Student deleted successfully");
    }

    @Override
    public ResponseEntity<?> deleteUserByNumberphone(String numberphone) {
        return null;
    }
}
