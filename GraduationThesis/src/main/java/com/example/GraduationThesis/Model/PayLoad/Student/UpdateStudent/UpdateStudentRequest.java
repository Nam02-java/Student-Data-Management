package com.example.GraduationThesis.Model.PayLoad.Student.UpdateStudent;

import com.example.GraduationThesis.Model.PayLoad.Student.SignUpStudent.Conduct.ConductPayload;
import com.example.GraduationThesis.Model.PayLoad.Student.SignUpStudent.Scores.ScorePayload;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UpdateStudentRequest {

    private Long userId;
    private Map<String, String> updates;

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

    private List<ScorePayload> scorePayloads;

    private ConductPayload conductPayload;
}
