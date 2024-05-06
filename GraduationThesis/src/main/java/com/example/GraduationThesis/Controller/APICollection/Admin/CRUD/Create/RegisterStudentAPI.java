package com.example.GraduationThesis.Controller.APICollection.Admin.CRUD.Create;

import com.example.GraduationThesis.Model.PayLoad.Student.SignUpStudent.StudenRequest;
import com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Create.AdminServiceCreateAPI;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/admin")
public class RegisterStudentAPI {

    @Autowired
    @Qualifier("RegisterStudentImplementation")
    private AdminServiceCreateAPI adminServiceCreateAPI;

    @PostMapping("/registerStudent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerStudent(@Valid @RequestBody StudenRequest studenRequest) {
        return adminServiceCreateAPI.registerStudent(studenRequest);
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

