package com.example.GraduationThesis.Controller.APICollection.Admin.CRUD.Update.User;

import com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Update.AdminServiceUpdateAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Currently no class is using this API class
 */

@RestController
@RequestMapping("/api/v1/admin/removeAdminPermissions")
public class RemoveAdminPermissionsAPI {

    @Autowired
    @Qualifier("RemoveAdminPermissionsImplementation")
    private AdminServiceUpdateAPI adminServiceUpdateAPI;

    @PutMapping("/{numberphone}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeAdminPermissions(@PathVariable String numberphone) {
        return adminServiceUpdateAPI.removeAdminPermissions(numberphone);
    }
}
