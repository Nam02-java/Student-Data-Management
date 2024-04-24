package com.example.GraduationThesis.Controller.APICollection.Admin.CRUD.Create;

import com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Create.AdminServiceCreateAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/adminAuthorization")
public class AdminAuthorizationAPI {

    @Autowired
    @Qualifier("AdminAuthorizationImplementation")
    private AdminServiceCreateAPI adminServiceCreateAPI;

    @PostMapping("/{numberphone}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adminAuthorization(@PathVariable String numberphone) {
        return adminServiceCreateAPI.adminAuthorization(numberphone);
    }
}
