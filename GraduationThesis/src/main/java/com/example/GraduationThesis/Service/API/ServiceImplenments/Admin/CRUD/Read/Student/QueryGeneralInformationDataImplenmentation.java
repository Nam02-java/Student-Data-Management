package com.example.GraduationThesis.Service.API.ServiceImplenments.Admin.CRUD.Read.Student;

import com.example.GraduationThesis.Model.Enitity.Student.Student;
import com.example.GraduationThesis.Model.Enitity.Student.Subject.Scores;
import com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Read.AdminServiceReadAPI;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.Student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("QueryGeneralInformationDataImplenmentation")
public class QueryGeneralInformationDataImplenmentation implements AdminServiceReadAPI {
    @Autowired
    private StudentService studentService;

    @Override
    public List<Object> queryGeneralInformationData() {
        List<Student> students = studentService.getAllStudents();
        return students.stream()
                .map(student -> {
                    Map<String, Object> studentMap = new LinkedHashMap<>();
                    studentMap.put("ID", student.getId());
                    studentMap.put("Student Name", student.getUsername());
                    studentMap.put("Class Name", student.getClassname());
                    studentMap.put("Position", student.getPosition());
                    studentMap.put("Teacher Name", student.getTeachername());
                    studentMap.put("Address", student.getAddress());
                    studentMap.put("Number Phone", student.getNumberphone());

                    List<Double> overallScores = student.getScores().stream()
                            .map(Scores::getScoreOverall)
                            .filter(Objects::nonNull)
                            .map(Double::parseDouble)
                            .sorted()
                            .collect(Collectors.toList());

                    // calculate the average score of 13 subjects
                    double averageScore = overallScores.stream()
                            .mapToDouble(Double::doubleValue) // convert to double
                            .average()
                            .orElse(0.0);

                    studentMap.put("GPA", averageScore);

                    return studentMap;
                })
                .sorted(Comparator.comparingDouble(student -> (double) student.get("GPA")))
                .collect(Collectors.toList());
    }

    @Override
    public List<Object> queryPositionInClassData() {
        return null;
    }

    @Override
    public List<Object> queryScoresForSubjectsData() {
        return null;
    }

    @Override
    public List<Object> queryConductScoreData() {
        return null;
    }

    @Override
    public List<Object> queryPersonalInformationData() {
        return null;
    }

    @Override
    public List<Object> queryUsersData() {
        return null;
    }

}


