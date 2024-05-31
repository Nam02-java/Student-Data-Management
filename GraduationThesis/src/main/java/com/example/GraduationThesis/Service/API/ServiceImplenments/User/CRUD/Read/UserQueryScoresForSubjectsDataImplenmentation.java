package com.example.GraduationThesis.Service.API.ServiceImplenments.User.CRUD.Read;

import com.example.GraduationThesis.Controller.SringSecurity6.UserData.CustomUserDetails;
import com.example.GraduationThesis.Model.Enitity.Student.Student;
import com.example.GraduationThesis.Model.Enitity.Student.Subject.Scores;
import com.example.GraduationThesis.Model.Enitity.Student.Subject.Subjects;
import com.example.GraduationThesis.Service.API.InterfaceService.User.CRUD.Read.UserServiceReadAPI;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.Student.StudentService;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.Student.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("UserQueryScoresForSubjectsDataImplenmentation")
public class UserQueryScoresForSubjectsDataImplenmentation implements UserServiceReadAPI {

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    @Override
    public ResponseEntity<Object> queryGeneralInformationData() {
        return null;
    }

    @Override
    public ResponseEntity<Object> queryPositionInClassData() {
        return null;
    }

    @Override
    public ResponseEntity<Object> queryScoresForSubjectsData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String currentUserPhoneNumber = userDetails.getUser().getNumberPhone();

        List<Student> students = studentService.getStudentsByPartensnumberphone(currentUserPhoneNumber);

        if (students.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Numberphone of user not match with student's partens numberphone");
        }

        List<Object> responseData = students.stream()
                .map(student -> {
                    Map<String, Object> studentMap = new LinkedHashMap<>();
                    studentMap.put("ID", student.getId());
                    studentMap.put("Student Name", student.getUsername());

                    List<Map<String, Object>> scoresListByYear = new ArrayList<>();

                    student.getScores().stream()
                            .sorted(Comparator.comparing(Scores::getSchoolYear).thenComparing(Scores::getSubject_ID))
                            .forEach(score -> {
                                String schoolYear = score.getSchoolYear();
                                Subjects subject = subjectService.getSubjectById(score.getSubject_ID());

                                if (subject != null) {
                                    Map<String, Object> scoresMap = scoresListByYear.stream()
                                            .filter(map -> schoolYear.equals(map.get("School Year")))
                                            .findFirst()
                                            .orElseGet(() -> {
                                                Map<String, Object> newMap = new LinkedHashMap<>();
                                                newMap.put("School Year", schoolYear);
                                                scoresListByYear.add(newMap);
                                                return newMap;
                                            });

                                    scoresMap.put(subject.getName() + " Score 15 Min", score.getScore15Min());
                                    scoresMap.put(subject.getName() + " Score 1 Hour", score.getScore1Hour());
                                    scoresMap.put(subject.getName() + " Score Mid Term", score.getScoreMidTerm());
                                    scoresMap.put(subject.getName() + " Score Final Exam", score.getScoreFinalExam());
                                    scoresMap.put(subject.getName() + " Score Overall", score.getScoreOverall());
                                }
                            });

                    studentMap.put("Scores", scoresListByYear);

                    return studentMap;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseData);
    }


    @Override
    public ResponseEntity<Object> queryConductScoreData() {
        return null;
    }

    @Override
    public ResponseEntity<Object> queryPersonalInformationData() {
        return null;
    }
}
