package com.example.GraduationThesis.Service.API.ServiceImplenments.Admin.CRUD.Read.Student;

import com.example.GraduationThesis.Model.Enitity.Student.Subject.Subjects;
import com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Read.AdminServiceReadAPI;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.Student.StudentService;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.Student.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("QueryScoresForSubjectsDataImplenmentation")
public class QueryScoresForSubjectsDataImplenmentation implements AdminServiceReadAPI {
    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;


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
        return studentService.getAllStudents().stream()
                .map(student -> {
                    Map<String, Object> studentMap = new LinkedHashMap<>();
                    studentMap.put("ID", student.getId());
                    studentMap.put("Student Name", student.getUsername());

                    // Lấy các loại điểm từ class Scores
                    Map<String, String> scoresMap = new LinkedHashMap<>();
                    student.getScores().forEach(score -> {
                        Subjects subject = subjectService.getSubjectById(score.getSubject_ID());
                        if (subject != null) {
                            scoresMap.put(subject.getName() + " Score 15 Min", score.getScore15Min());
                            scoresMap.put(subject.getName() + " Score 1 Hour", score.getScore1Hour());
                            scoresMap.put(subject.getName() + " Score Mid Term", score.getScoreMidTerm());
                            scoresMap.put(subject.getName() + " Score_Final_Exam", score.getScoreFinalExam());
                            scoresMap.put(subject.getName() + " Score Overall", score.getScoreOverall());
                        }
                    });
                    studentMap.put("Scores", scoresMap);

                    return studentMap;
                })
                .collect(Collectors.toList());
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
