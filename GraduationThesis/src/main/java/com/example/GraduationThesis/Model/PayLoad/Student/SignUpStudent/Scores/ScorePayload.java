package com.example.GraduationThesis.Model.PayLoad.Student.SignUpStudent.Scores;

import lombok.Data;

import java.util.List;

@Data
public class ScorePayload {

    private List<ScoreRequest> scores;
}
