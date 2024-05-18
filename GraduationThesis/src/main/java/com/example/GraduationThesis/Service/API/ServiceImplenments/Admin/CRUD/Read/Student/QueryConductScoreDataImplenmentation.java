package com.example.GraduationThesis.Service.API.ServiceImplenments.Admin.CRUD.Read.Student;

import com.example.GraduationThesis.Model.Enitity.Student.Conduct.Conduct;
import com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Read.AdminServiceReadAPI;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.Student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("QueryConductScoreDataImplenmentation")
public class QueryConductScoreDataImplenmentation implements AdminServiceReadAPI {

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
    public List<Object> queryConductScoreData() {
        return studentService.getAllStudents().stream()
                .map(student -> {
                    Map<String, Object> studentMap = new LinkedHashMap<>();
                    studentMap.put("ID", student.getId());
                    studentMap.put("Student Name", student.getUsername());

                    // Lấy danh sách thông tin conduct
                    List<Map<String, String>> conductInfoList = new ArrayList<>();
                    for (Conduct conduct : student.getConducts()) {
                        Map<String, String> conductMap = new LinkedHashMap<>();
                        conductMap.put("School_Year", conduct.getSchool_year());
                        conductMap.put("Conduct", conduct.getConduct());
                        conductMap.put("Attendance_Score", conduct.getAttendance_Score());
                        conductInfoList.add(conductMap);
                    }
                    studentMap.put("Conducts", conductInfoList);

                    return studentMap;
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<Object> queryScoresForSubjectsData() {
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
