package com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Update;

import com.example.GraduationThesis.Model.PayLoad.Student.UpdateStudent.UpdateStudentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface AdminServiceUpdateAPI {

    ResponseEntity<?> updateStudentByID(@RequestBody UpdateStudentRequest updateStudentRequest);

    ResponseEntity<?> removeAdminPermissions(@PathVariable String numberphone);

}
