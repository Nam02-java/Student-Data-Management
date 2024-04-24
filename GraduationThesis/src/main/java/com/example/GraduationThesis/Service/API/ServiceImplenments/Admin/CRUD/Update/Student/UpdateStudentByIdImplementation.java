package com.example.GraduationThesis.Service.API.ServiceImplenments.Admin.CRUD.Update.Student;

import com.example.GraduationThesis.Model.Enitity.Student.Student;
import com.example.GraduationThesis.Model.Enitity.Student.Subject.Scores;
import com.example.GraduationThesis.Model.Enitity.Student.Subject.Subjects;
import com.example.GraduationThesis.Model.PayLoad.Student.SignUpStudent.Conduct.ConductPayload;
import com.example.GraduationThesis.Model.PayLoad.Student.SignUpStudent.Conduct.ConductRequest;
import com.example.GraduationThesis.Model.PayLoad.Student.SignUpStudent.Scores.ScorePayload;
import com.example.GraduationThesis.Model.PayLoad.Student.SignUpStudent.Scores.ScoreRequest;
import com.example.GraduationThesis.Model.PayLoad.Student.UpdateStudent.UpdateStudentRequest;
import com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Update.AdminServiceUpdateAPI;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.Student.StudentService;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.Student.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@Service("UpdateStudentByIdImplementation")
public class UpdateStudentByIdImplementation implements AdminServiceUpdateAPI {

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;


    @Override
    public ResponseEntity<?> updateStudentByID(@RequestBody UpdateStudentRequest updateStudentRequest) {
        Long studentID = updateStudentRequest.getUserId();
        Student student = studentService.findStudentById(studentID);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student ID not found");
        }

        updateStudentInformation(student, updateStudentRequest.getUpdates());
        updateStudentScores(student, updateStudentRequest.getScorePayload());
        updateStudentConduct(student, updateStudentRequest.getConductPayload());

        studentService.saveStudent(student);
        return ResponseEntity.ok("Student updated successfully");
    }

    @Override
    public ResponseEntity<?> removeAdminPermissions(String numberphone) {
        return null;
    }


    private void updateStudentInformation(Student student, Map<String, String> updates) {
        if (updates != null) {
            updates.forEach((key, value) -> {
                switch (key) {
                    case "username":
                        student.setUsername(value);
                        break;
                    case "classname":
                        student.setClassname(value);
                        break;
                    case "email":
                        student.setEmail(value);
                        break;
                    case "dateOfBirth":
                        student.setDateOfBirth(value);
                        break;
                    case "numberphone":
                        student.setNumberphone(value);
                        break;
                    case "address":
                        student.setAddress(value);
                        break;
                    case "position":
                        student.setPosition(value);
                        break;
                    case "teachername":
                        student.setTeachername(value);
                        break;
                    case "partentsname":
                        student.setPartentsname(value);
                        break;
                    case "partensnumberphone":
                        student.setPartensnumberphone(value);
                        break;
                }
            });
        }
    }

    private void updateStudentScores(Student student, ScorePayload scorePayload) {
        if (scorePayload != null && scorePayload.getScores() != null) {
            for (ScoreRequest scoreRequest : scorePayload.getScores()) {
                String subjectName = scoreRequest.getSubjectName();
                List<String> scoreValues = scoreRequest.getScores();
                student.getScores().forEach(score -> {
                    Subjects subject = subjectService.getSubjectByName(subjectName);
                    if (subject != null && score.getSubject_ID() == subject.getId()) {
                        updateScoreValues(score, scoreValues);
                    }
                });
            }
        }
    }

    private void updateScoreValues(Scores score, List<String> scoreValues) {
        if (scoreValues.size() >= 4) {
            score.setScore15Min(scoreValues.get(0));
            score.setScore1Hour(scoreValues.get(1));
            score.setScoreMidTerm(scoreValues.get(2));
            score.setScoreFinalExam(scoreValues.get(3));
          //  score.setScoreOverall(scoreValues.get(4));

            double overallScore = scoreValues.stream()
                    .filter(s -> !s.isEmpty()) // Lọc ra các ô không trống
                    .mapToInt(Integer::parseInt)
                    .average()
                    .orElse(0.0); // Giá trị mặc định nếu không có điểm

            // Lưu điểm tổng kết vào đối tượng Scores
            score.setScoreOverall(String.valueOf(overallScore));
        }
    }

    private void updateStudentConduct(Student student, ConductPayload conductPayload) {
        if (conductPayload != null && conductPayload.getConducts() != null) {
            for (ConductRequest conductRequest : conductPayload.getConducts()) {
                List<String> conductValues = conductRequest.getConduct();
                student.getConducts().forEach(conduct -> {
                    if (conductValues.size() >= 4) {
                        conduct.setConduct2017_2018(conductValues.get(0));
                        conduct.setConduct2018_2019(conductValues.get(1));
                        conduct.setConduct2019_2020(conductValues.get(2));
                        conduct.setAttendance_Score(conductValues.get(3));
                    }
                });
            }
        }
    }
}