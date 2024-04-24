package com.example.GraduationThesis.Service.API.ServiceImplenments.Admin.CRUD.Read.Student;

import com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Read.AdminServiceReadAPI;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.Student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("QueryPersonalInformationDataImplenmentation")
public class QueryPersonalInformationDataImplenmentation implements AdminServiceReadAPI {

    @Autowired
    private StudentService studentService;


    @Override
    public List<Object> queryGeneralInformationData() {
        return null;
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
        return studentService.getAllStudents().stream()
                .map(student -> {
                    Map<String, Object> studentInfo = new LinkedHashMap<>();
                    studentInfo.put("ID", student.getId());
                    studentInfo.put("Student Name", student.getUsername());
                    studentInfo.put("Email", student.getEmail());
                    studentInfo.put("Birth of Date", student.getDateOfBirth());
                    studentInfo.put("Address", student.getAddress());
                    studentInfo.put("Number Phone", student.getNumberphone());
                    studentInfo.put("Parents Name", student.getPartentsname());
                    studentInfo.put("Parents Number Phone", student.getPartensnumberphone());
                    return studentInfo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Object> queryUsersData() {
        return null;
    }
}
