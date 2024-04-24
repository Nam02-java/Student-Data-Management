package com.example.GraduationThesis.Model.PayLoad.Student.SignUpStudent;

import com.example.GraduationThesis.Model.PayLoad.Student.SignUpStudent.Conduct.ConductPayload;
import com.example.GraduationThesis.Model.PayLoad.Student.SignUpStudent.Scores.ScorePayload;
import lombok.Data;

@Data
public class StudenRequest {

    private String username;
    private String classname;
    private String email;
    private String dateOfBirth;
    private String numberphone;
    private String address;
    private String position;
    private String teachername;
    private String partentsname;
    private String partensnumberphone;

    private ScorePayload scorePayload;

    private ConductPayload conductPayload;
}