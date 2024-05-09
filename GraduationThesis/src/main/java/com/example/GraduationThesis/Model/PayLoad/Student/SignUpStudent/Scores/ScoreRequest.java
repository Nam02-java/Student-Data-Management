package com.example.GraduationThesis.Model.PayLoad.Student.SignUpStudent.Scores;

import lombok.Data;

import java.util.List;

@Data
public class ScoreRequest {
    private String subjectName;

    private List<String> scores;
}
