package com.example.GraduationThesis.Controller.APICollection.Admin.CRUD.Update.Student;

import com.example.GraduationThesis.Model.PayLoad.Student.UpdateStudent.UpdateStudentRequest;
import com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Update.AdminServiceUpdateAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class UpdateStudentAPI {

    @Autowired
    @Qualifier("UpdateStudentByIdImplementation")
    private AdminServiceUpdateAPI adminServiceUpdateAPI;

    @PutMapping("/updateStudentByID")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStudentByID(@RequestBody UpdateStudentRequest updateStudentRequest) {
        return adminServiceUpdateAPI.updateStudentByID(updateStudentRequest);
    }
}
