package com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Delete;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface AdminServiceDeleteAPI {
    ResponseEntity<?> deleteStudentById(@PathVariable Long studentId);
    ResponseEntity<?> deleteUserByNumberphone(@PathVariable String numberphone);

}
