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
import java.util.concurrent.atomic.AtomicInteger;


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

        List<ScorePayload> scorePayloads = studenRequest.getScorePayloads();
        ConductPayload conductPayload = studenRequest.getConductPayload();

        // Check scores before saving to database
        if (scorePayloads != null) {

            //Counting variable to serve the control of school year input
            int count = 0;

            for (ScorePayload scorePayload : scorePayloads) {

                count += 1;
                if (scorePayload.getSchoolYear() == null) {
                    switch (count) {
                        case 1:
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("School year value at first year scores can not be null");
                        case 2:
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("School year value at second year scores can not be null");
                        case 3:
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("School year value at third year scores can not be null");
                    }
                }
                String schoolYear = scorePayload.getSchoolYear();
                schoolYear.replace(" ", "");
                if (schoolYear.isEmpty()) {
                    switch (count) {
                        case 1:
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("School year value at first year scores is not valid");
                        case 2:
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("School year value at second year scores is not valid");
                        case 3:
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("School year value at third year scores is not valid");
                    }
                }

                for (int i = 0; i < scorePayloads.size(); i++) {
                    for (int j = i + 1; j < scorePayloads.size(); j++) {
                        if (scorePayloads.get(i).getSchoolYear().replace(" ", "").equals(scorePayloads.get(j).getSchoolYear().replace(" ", ""))) {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("School years overlap");
                        }
                    }
                }


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
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Score 15 minutes" + " of " + subjectName + " at " + schoolYear + " is not valid");
                            } else if (i == 1) {
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Score 1 hour" + " of " + subjectName + " at " + schoolYear + " is not valid");
                            } else if (i == 2) {
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Score mid term" + " of " + subjectName + " at " + schoolYear + " is not valid");
                            } else {
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Score final exam" + " of " + subjectName + " at " + schoolYear + " is not valid");
                            }
                        }
                    }
                    scoreRequest.setScores(scoreList); // Update scores in the ScoreRequest
                }
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
        if (scorePayloads != null) {
            for (ScorePayload scorePayload : scorePayloads) {
                String schoolYear = scorePayload.getSchoolYear();
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
                    double overallScore = individualScores.stream().filter(s -> !s.isEmpty()).mapToInt(Integer::parseInt).average().orElse(0.0); // default value if no points

                    // save the summary score to the Scores object
                    scores.setScoreOverall(String.valueOf(overallScore));

                    scores.setSchoolYear(schoolYear);
                    scores.setStudent(student);

                    scoresList.add(scores);
                });
            }

            if (scorePayloads.size() < 3) {
                for (ScorePayload scorePayload : scorePayloads) {
                    while (scoresList.size() != 39) {
                        String schoolYear = null;
                        switch (scoresList.size()) {
                            case 13:
                                schoolYear = "Second year";
                                break;
                            case 26:
                                schoolYear = "Third year";
                                break;
                        }

                        String finalSchoolYearResult = schoolYear;

                        scorePayload.getScores().forEach(score -> {
                            Scores scores = new Scores();
                            Long subjectId = subjectService.findSubjectIdByName(score.getSubjectName());

                            scores.setSubject_ID(subjectId);

                            scores.setScore15Min("");
                            scores.setScore1Hour("");
                            scores.setScoreMidTerm("");
                            scores.setScoreFinalExam("");

                            double overallScore = 0.0;
                            scores.setScoreOverall(String.valueOf(overallScore));

                            scores.setSchoolYear(finalSchoolYearResult);
                            scores.setStudent(student);

                            scoresList.add(scores);
                        });
                    }
                }
            }
        } else {
            scorePayloads = new ArrayList<>();

            // Create default empty ScorePayload
            for (int i = 0; i < 3; i++) {
                ScorePayload defaultScorePayload = new ScorePayload();
                switch (i) {
                    case 0:
                        defaultScorePayload.setSchoolYear("First Year");
                        break;
                    case 1:
                        defaultScorePayload.setSchoolYear("Second Year");
                        break;
                    case 2:
                        defaultScorePayload.setSchoolYear("Third Year");
                        break;
                }

                defaultScorePayload.setScores(new ArrayList<>());

                ArrayList<String> subjectNames = new ArrayList<>();
                subjectNames.add("Literature"); // ID 1
                subjectNames.add("Math"); // ID 2
                subjectNames.add("English"); // ID 3
                subjectNames.add("History"); // ID 4
                subjectNames.add("Geography"); // ID 5
                subjectNames.add("Physics"); // ID 6
                subjectNames.add("Chemistry"); // ID 7
                subjectNames.add("Biology"); // ID 8
                subjectNames.add("Citizen_Education"); // ID 9
                subjectNames.add("National_Defense_And_Security_Education"); // ID 10
                subjectNames.add("Technology"); // ID 11
                subjectNames.add("Information_Technology"); // ID 12
                subjectNames.add("Physical_Education"); // ID 13

                for (int j = 0; j < 13; j++) {
                    ScoreRequest defaultScoreRequest = new ScoreRequest();
                    defaultScoreRequest.setSubjectName(subjectNames.get(j));
                    List<String> defaultScores = Arrays.asList("", "", "", "");
                    defaultScoreRequest.setScores(defaultScores);
                    defaultScorePayload.getScores().add(defaultScoreRequest);
                }

                scorePayloads.add(defaultScorePayload);
            }

            // add scores items to scoresList in order from 1 to 13
            for (ScorePayload scorePayload : scorePayloads) {
                String schoolYear = scorePayload.getSchoolYear();
                scorePayload.getScores().forEach(score -> {
                    Scores scores = new Scores();
                    Long subjectId = subjectService.findSubjectIdByName(score.getSubjectName());

                    scores.setSubject_ID(subjectId);

                    scores.setScore15Min("");
                    scores.setScore1Hour("");
                    scores.setScoreMidTerm("");
                    scores.setScoreFinalExam("");

                    double overallScore = 0.0;
                    scores.setScoreOverall(String.valueOf(overallScore));

                    scores.setSchoolYear(schoolYear);
                    scores.setStudent(student);

                    scoresList.add(scores);
                });
            }
        }

        // Sort the score list by school year and subject code
        Collections.sort(scoresList, Comparator.comparing(Scores::getSchoolYear).thenComparing(Scores::getSubject_ID));

        student.setScores(scoresList);

        AtomicInteger count = new AtomicInteger();
        List<Conduct> conductList = new ArrayList<>();
        if (conductPayload != null) {
            conductPayload.getConducts().forEach(conduct -> {
                Conduct conducts = new Conduct();
                List<String> individualScores = conduct.getConduct();

                count.addAndGet(1);
                if (individualScores.get(0) == null || individualScores.get(0).isEmpty()) {
                    System.out.println(conducts.getSchool_year());
                    switch (count.get()) {
                        case 1:
                            conducts.setSchool_year("First Year");
                            break;
                        case 2:
                            conducts.setSchool_year("Second Year");
                            break;
                        case 3:
                            conducts.setSchool_year("Third Year");
                            break;
                    }
                } else {
                    conducts.setSchool_year(individualScores.size() > 0 ? individualScores.get(0) : "");
                }
                conducts.setConduct(individualScores.size() > 1 ? individualScores.get(1) : "");
                conducts.setAttendance_Score(individualScores.size() > 2 ? individualScores.get(2) : "");
                conducts.setStudent(student);
                conductList.add(conducts);
            });

            // Add empty elements if needed
            while (conductList.size() < 3) {

                Conduct emptyConduct = new Conduct();

                switch (conductList.size()) {
                    case 0:
                        emptyConduct.setSchool_year("First Year");
                        break;
                    case 1:
                        emptyConduct.setSchool_year("Second Year");
                        break;
                    case 2:
                        emptyConduct.setSchool_year("Third Year");
                        break;
                }

                emptyConduct.setConduct("");
                emptyConduct.setAttendance_Score("");
                emptyConduct.setStudent(student);
                conductList.add(emptyConduct);
            }

        } else {
            for (int i = 0; i < 3; i++) {
                Conduct emptyConduct = new Conduct();
                switch (i) {
                    case 0:
                        emptyConduct.setSchool_year("First Year");
                        break;
                    case 1:
                        emptyConduct.setSchool_year("Second Year");
                        break;
                    case 2:
                        emptyConduct.setSchool_year("Third Year");
                        break;
                }
                emptyConduct.setConduct("");
                emptyConduct.setAttendance_Score("");
                emptyConduct.setStudent(student);
                conductList.add(emptyConduct);
            }
        }

        student.setConducts(conductList);

        studentService.saveStudent(student);

        return ResponseEntity.ok(new String("Student registered succesfully"));
    }

    @Override
    public ResponseEntity<?> adminAuthorization(String numberphone) {
        return null;
    }
}
