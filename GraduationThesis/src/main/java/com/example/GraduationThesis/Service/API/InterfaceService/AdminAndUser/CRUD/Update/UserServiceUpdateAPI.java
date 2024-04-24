package com.example.GraduationThesis.Service.API.InterfaceService.AdminAndUser.CRUD.Update;

import com.example.GraduationThesis.Model.PayLoad.Student.UpdateStudent.UpdateStudentRequest;
import com.example.GraduationThesis.Model.PayLoad.User.UpdateUser.UpdateUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserServiceUpdateAPI {

    ResponseEntity<?> userUpdate(@RequestBody UpdateUserRequest updateUserRequest);


}
