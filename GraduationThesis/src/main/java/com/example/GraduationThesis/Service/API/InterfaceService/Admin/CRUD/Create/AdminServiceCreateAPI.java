package com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Create;

import com.example.GraduationThesis.Model.PayLoad.Student.SignUpStudent.StudenRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface AdminServiceCreateAPI {

    ResponseEntity<?> registerStudent(@RequestBody StudenRequest studenRequest);

    ResponseEntity<?> adminAuthorization(@PathVariable String numberphone);

}
