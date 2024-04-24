package com.example.GraduationThesis.Service.API.ServiceImplenments.Admin.CRUD.Read.Student;

import com.example.GraduationThesis.Model.Enitity.Student.Student;
import com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Read.AdminServiceReadAPI;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.Student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("QueryPositionInClassDataImplenmentation")
public class QueryPositionInClassDataImplenmentation implements AdminServiceReadAPI {

    @Autowired
    private StudentService studentService;


    @Override
    public List<Object> queryGeneralInformationData() {
        return null;
    }

    @Override
    public List<Object> queryPositionInClassData() {
        List<Student> students = studentService.getAllStudents();
        return students.stream()
                .map(student -> {
                    Map<String, Object> studentMap = new LinkedHashMap<>();
                    studentMap.put("ID", student.getId());
                    studentMap.put("Student Name", student.getUsername());
                    studentMap.put("Class Name", student.getClassname());
                    studentMap.put("Position", student.getPosition());
                    studentMap.put("Teacher Name", student.getTeachername());

                    return studentMap;
                })
                .collect(Collectors.toList());
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
