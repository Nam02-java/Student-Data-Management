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
import com.example.GraduationThesis.Service.Utils.CheckValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("UpdateStudentByIdImplementation")
public class UpdateStudentByIdImplementation implements AdminServiceUpdateAPI {

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private CheckValid checkValid;

    @Override
    public ResponseEntity<?> updateStudentByID(@RequestBody UpdateStudentRequest updateStudentRequest) {
        Long studentID = updateStudentRequest.getUserId();
        Student student = studentService.findStudentById(studentID);


        /**
         * get all student to check the last student's id
         */
        List<Student> allStudents = studentService.getAllStudents();
        Long lastStudentId = null;
        if (!allStudents.isEmpty()) {
            Student lastStudent = allStudents.get(allStudents.size() - 1);
            lastStudentId = lastStudent.getId();
        }


        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student ID not found");
        }

        Map<String, String> updates = updateStudentRequest.getUpdates();
        if (updates != null) {
            for (Map.Entry<String, String> entry : updates.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if ("username".equals(key)) {
                    if (value == null || value.trim().isEmpty() || value.length() < 2 || value.length() > 50) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User name must have 2 to 50 characters");
                    }
                }

                if ("classname".equals(key)) {
                    if (value == null || value.trim().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Class name is empty");
                    }
                }

                if ("email".equals(key)) {
                    if (!checkValid.isValidEmail(value)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
                    }
                    Student existingEmail = studentService.findByEmail(value);
                    if (existingEmail != null && !existingEmail.getId().equals(student.getId())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
                    }
                }

                if ("dateOfBirth".equals(key)) {
                    if (value == null || value.trim().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("date of birth is empty");
                    }
                }

                if ("numberphone".equals(key)) {
                    if (!checkValid.isValidPhoneNumber(value)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid student's phone number format");
                    }
                    Student existingNumberphone = studentService.findBynumberPhone(value);
                    if (existingNumberphone != null && !existingNumberphone.getId().equals(student.getId())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Numberphone of student already exists");
                    }
                }

                if ("address".equals(key)) {
                    if (value == null || value.trim().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("address is empty");
                    }
                }

                if ("address".equals(key)) {
                    if (value == null || value.trim().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("address is empty");
                    }
                }

                if ("position".equals(key)) {
                    if (value == null || value.trim().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("position is empty");
                    }
                }

                if ("teachername".equals(key)) {
                    if (value == null || value.trim().isEmpty() || value.length() < 2 || value.length() > 50) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Teacher name must have 2 to 50 characters");
                    }
                }

                if ("partentsname".equals(key)) {
                    if (value == null || value.trim().isEmpty() || value.length() < 2 || value.length() > 50) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Partents name must have 2 to 50 characters");
                    }
                }

                /**
                 * No duplicate control
                 * In real cases, many students with the same parents may be in the same class
                 */
                if ("partensnumberphone".equals(key)) {
                    if (!checkValid.isValidPhoneNumber(value)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid parent's phone number format");
                    }
                }
            }
        }

        // Check scores before saving to database
        ScorePayload scorePayload = updateStudentRequest.getScorePayload();
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

        updateStudentInformation(student, updateStudentRequest.getUpdates());
        updateStudentScores(student, updateStudentRequest.getScorePayload());
        updateStudentConduct(student, updateStudentRequest.getConductPayload());

        studentService.saveStudent(student);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Student updated successfully");
        //response.put("lastID", lastStudentId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("LastID", String.valueOf(lastStudentId));

        return ResponseEntity.ok().headers(headers).body(response);
    }

    @Override
    public ResponseEntity<?> removeAdminPermissions(String numberphone) {
        return null;
    }


    /**
     * information of student
     *
     * @param student
     * @param updates
     */
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

    /**
     * scores of student
     *
     * @param student
     * @param scorePayload
     */
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
                    .filter(s -> !s.isEmpty())
                    .mapToInt(Integer::parseInt)
                    .average()
                    .orElse(0.0);

            score.setScoreOverall(String.valueOf(overallScore));
        }
    }

    /**
     * conduct of student
     *
     * @param student
     * @param conductPayload
     */
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