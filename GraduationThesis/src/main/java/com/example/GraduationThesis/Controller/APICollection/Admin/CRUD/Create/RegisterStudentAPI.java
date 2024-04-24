package com.example.GraduationThesis.Controller.APICollection.Admin.CRUD.Create;

import com.example.GraduationThesis.Model.PayLoad.Student.SignUpStudent.StudenRequest;
import com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Create.AdminServiceCreateAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class RegisterStudentAPI {

    @Autowired
    @Qualifier("RegisterStudentImplementation")
    private AdminServiceCreateAPI adminServiceCreateAPI;

    @PostMapping("/registerStudent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerStudent(@RequestBody StudenRequest studenRequest) {
        return adminServiceCreateAPI.registerStudent(studenRequest);
    }
}

