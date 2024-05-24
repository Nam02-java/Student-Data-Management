package com.example.GraduationThesis.Model.PayLoad.Student.SignUpStudent;

import com.example.GraduationThesis.Model.PayLoad.Student.SignUpStudent.Conduct.ConductPayload;
import com.example.GraduationThesis.Model.PayLoad.Student.SignUpStudent.Scores.ScorePayload;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class StudenRequest {

    @Size(min = 2, max = 50, message = "Student name must have 2 to 50 characters")
    @NotNull(message = "Student name is not empty or null")
    @NotBlank(message = "Student name is not empty or null")
    private String username;

    @NotBlank(message = "Class name is not empty or null")
    private String classname;

    @Email
    @NotBlank(message = "Email is not empty or null")
    private String email;

    @NotBlank(message = "Date of birth is not empty or null")
    private String dateOfBirth;

    @Size(min = 10, max = 11, message = "Student Number phone must have 10 to 11 digits")
    @Pattern(regexp = "\\d+", message = "Student Number phone must contain only digits")
    @NotNull(message = "Student Number phone is not empty or null")
    @NotBlank(message = "Student Number phone is not empty or null")
    private String numberphone;

    @NotBlank(message = "Address is not empty or null")
    private String address;

    @NotBlank(message = "Position is not empty or null")
    private String position;

    @Size(min = 2, max = 50, message = "Teacher Name must have 2 to 50 characters")
    @NotBlank(message = "Teacher Name not empty or null")
    private String teachername;

    @Size(min = 2, max = 50, message = "Partents Name must have 2 to 50 characters")
    @NotBlank(message = "Partents Name not empty or null")
    private String partentsname;

    @Size(min = 10, max = 11, message = "Partens Number phone must have 10 to 11 digits")
    @Pattern(regexp = "\\d+", message = "Partens Number phone must contain only digits")
    @NotNull(message = "Partens Number phone is not empty or null")
    @NotBlank(message = "Partens Number phone is not empty or null")
    private String partensnumberphone;

    private List<ScorePayload> scorePayloads;

    private ConductPayload conductPayload;
}