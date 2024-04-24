package com.example.GraduationThesis.Controller.APICollection.Admin.CRUD.Delete;

import com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Delete.AdminServiceDeleteAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/student")
public class DeleteStudentByIdAPI {

    @Autowired
    @Qualifier("DeleteStudentByIdImplementation")
    private AdminServiceDeleteAPI adminServiceDeleteAPI;


    @DeleteMapping("/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteStudentById(@PathVariable Long studentId) {
        return adminServiceDeleteAPI.deleteStudentById(studentId);
    }
}
