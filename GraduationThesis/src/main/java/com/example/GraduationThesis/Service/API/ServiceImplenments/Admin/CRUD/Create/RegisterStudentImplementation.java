package com.example.GraduationThesis.Service.API.ServiceImplenments.Admin.CRUD.Create;

import com.example.GraduationThesis.Model.Enitity.Student.Conduct.Conduct;
import com.example.GraduationThesis.Model.Enitity.Student.Student;
import com.example.GraduationThesis.Model.Enitity.Student.Subject.Scores;
import com.example.GraduationThesis.Model.PayLoad.Student.SignUpStudent.Conduct.ConductPayload;
import com.example.GraduationThesis.Model.PayLoad.Student.SignUpStudent.Scores.ScorePayload;
import com.example.GraduationThesis.Model.PayLoad.Student.SignUpStudent.Scores.ScoreRequest;
import com.example.GraduationThesis.Model.PayLoad.Student.SignUpStudent.StudenRequest;
import com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Create.AdminServiceCreateAPI;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.Student.StudentService;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.Student.SubjectService;
import com.example.GraduationThesis.Service.Utils.CheckValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.*;


@Service("RegisterStudentImplementation")
public class RegisterStudentImplementation implements AdminServiceCreateAPI {

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private CheckValid checkValid;

    @Override
    public ResponseEntity<?> registerStudent(@RequestBody StudenRequest studenRequest) {

        ScorePayload scorePayload = studenRequest.getScorePayload();
        ConductPayload conductPayload = studenRequest.getConductPayload();

        // Check scores before saving to database
        if (scorePayload != null && scorePayload.getScores() != null) {
            List<ScoreRequest> scores = scorePayload.getScores();

            for (ScoreRequest scoreRequest : scores) {
                String subjectName = scoreRequest.getSubjectName();
                List<String> scoreList = scoreRequest.getScores();
                for (int i = 0; i < scoreList.size(); i++) {
                    String score = scoreList.get(i);

                    score = score.replace(" ", "");

                    if (score.isEmpty()) {
                        // Update score in the list
                        scoreList.set(i, score);
                        continue; // Skip empty scores
                    }

                    // Update score in the list
                    scoreList.set(i, score);

                    if (!checkValid.isValidInteger(score)) {
                        if (i == 0) {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Score 15 minutes" + " of " + subjectName + " is not valid");
                        } else if (i == 1) {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Score 1 hour" + " of " + subjectName + " is not valid");
                        } else if (i == 2) {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Score mid term" + " of " + subjectName + " is not valid");
                        } else {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Score final exam" + " of " + subjectName + " is not valid");
                        }
                    }
                }
                scoreRequest.setScores(scoreList); // Update scores in the ScoreRequest
            }
        }

        if (studentService.existsByUserName(studenRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student name already exists");
        }

        if (studentService.existsByEmail(studenRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student email already exists");
        }

        if (studentService.existsByNumberphone(studenRequest.getNumberphone())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student Number phone already exists");
        }


        Student student = new Student();

        student.setUsername(studenRequest.getUsername());
        student.setClassname(studenRequest.getClassname());
        student.setEmail(studenRequest.getEmail());
        student.setDateOfBirth(studenRequest.getDateOfBirth());
        student.setNumberphone(studenRequest.getNumberphone());
        student.setAddress(studenRequest.getAddress());
        student.setPosition(studenRequest.getPosition());
        student.setTeachername(studenRequest.getTeachername());
        student.setPartentsname(studenRequest.getPartentsname());
        student.setPartensnumberphone(studenRequest.getPartensnumberphone());


        List<Scores> scoresList = new ArrayList<>();
        scorePayload.getScores().forEach(score -> {
            Scores scores = new Scores();
            Long subjectId = subjectService.findSubjectIdByName(score.getSubjectName());

            scores.setSubject_ID(subjectId);

            // Get and save all scores from scores array
            List<String> individualScores = score.getScores();

            scores.setScore15Min(individualScores.get(0));
            scores.setScore1Hour(individualScores.get(1));
            scores.setScoreMidTerm(individualScores.get(2));
            scores.setScoreFinalExam(individualScores.get(3));

            // calculate the final score from the subscores
            double overallScore = individualScores.stream()
                    .filter(s -> !s.isEmpty())
                    .mapToInt(Integer::parseInt)
                    .average()
                    .orElse(0.0); // default value if no points

            // save the summary score to the Scores object
            scores.setScoreOverall(String.valueOf(overallScore));


            scores.setStudent(student);
            scoresList.add(scores);
        });

        Collections.sort(scoresList, Comparator.comparingLong(Scores::getSubject_ID));

        student.setScores(scoresList);

        List<Conduct> conductList = new ArrayList<>();
        conductPayload.getConducts().forEach(conduct -> {
            Conduct conducts = new Conduct();

            List<String> individualScores = conduct.getConduct();
            conducts.setConduct2017_2018(individualScores.get(0));
            conducts.setConduct2018_2019(individualScores.get(1));
            conducts.setConduct2019_2020(individualScores.get(2));
            conducts.setAttendance_Score(individualScores.get(3));
            conducts.setStudent(student);
            conductList.add(conducts);

        });

        student.setConducts(conductList);

        studentService.saveStudent(student);

        return ResponseEntity.ok(new String("Student registered succesfully"));
    }

    @Override
    public ResponseEntity<?> adminAuthorization(String numberphone) {
        return null;
    }

}
