package com.example.GraduationThesis.Model.PayLoad.Student.UpdateStudent;

import lombok.Data;

import java.util.List;
@Data

public class ConductRequestTest {
    private String school_year;
    private List<String> conduct;
}

