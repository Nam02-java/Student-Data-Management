package com.example.GraduationThesis.Controller.APICollection.Admin.CRUD.Delete;

import com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Delete.AdminServiceDeleteAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/user")
public class DeleteUserByNumberphoneAPI {

    @Autowired
    @Qualifier("DeleteUserByNumberphoneImplementation")
    private AdminServiceDeleteAPI adminServiceDeleteAPI;


    @DeleteMapping("/{numberphone}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUserByNumberphone(@PathVariable String numberphone) {
        return adminServiceDeleteAPI.deleteUserByNumberphone(numberphone);
    }
}
