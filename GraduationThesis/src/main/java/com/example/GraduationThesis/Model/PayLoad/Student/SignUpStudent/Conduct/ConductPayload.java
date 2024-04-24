package com.example.GraduationThesis.Model.PayLoad.Student.SignUpStudent.Conduct;

import lombok.Data;

import java.util.List;

@Data
public class ConductPayload {
    private List<ConductRequest> conducts;
}
