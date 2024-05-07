package com.example.GraduationThesis.Controller.APICollection.AdminAndUser.CRUD.Update;

import com.example.GraduationThesis.Model.PayLoad.User.UpdateUser.UpdateUserRequest;
import com.example.GraduationThesis.Service.API.InterfaceService.AdminAndUser.CRUD.Update.UserServiceUpdateAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/public")
public class UserUpdateAPI {
    @Autowired
    @Qualifier("UserUpdateImplementation")
    private UserServiceUpdateAPI userServiceUpdateAPI;

    @PutMapping("/updateUser")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> userUpdate( @RequestBody UpdateUserRequest updateUserRequest) {
        return userServiceUpdateAPI.userUpdate(updateUserRequest);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();

        // Sort errors by the order of the fields in the request
        ex.getBindingResult().getFieldErrors().stream()
                .sorted(Comparator.comparing(FieldError::getField))
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return errors;
    }
}

