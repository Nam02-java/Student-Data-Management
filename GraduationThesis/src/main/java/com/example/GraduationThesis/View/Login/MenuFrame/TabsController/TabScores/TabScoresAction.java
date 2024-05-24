package com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabScores;

import com.example.GraduationThesis.Model.Enitity.Users.Roles.ERole;
import com.example.GraduationThesis.Service.LazySingleton.JsonWebToken.JsonWebTokenManager;
import com.example.GraduationThesis.Service.LazySingleton.ListRoles.ListRolesManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class TabScoresAction {

    public static List<Map<String, Object>> Action() {
        HttpClient httpClient = HttpClient.newHttpClient();

        String API = null;

        if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {
            API = "http://localhost:8080/api/v1/admin/queryScoresForSubjectsData";
        } else {
            API = "http://localhost:8080/api/v1/user/queryScoresForSubjectsData";
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API))
                .header("Authorization", "Bearer " + JsonWebTokenManager.getInstance().getJwtToken())
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();
            System.out.println("Response from API: " + responseBody);

            // Convert JSON into a list of maps
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> students = objectMapper.readValue(responseBody, new TypeReference<List<Map<String, Object>>>() {
            });

            List<Map<String, Object>> result = new ArrayList<>();
            for (Map<String, Object> student : students) {
                // Lấy thông tin sinh viên
                int studentID = (int) student.get("ID");
                String studentName = (String) student.get("Student Name");
                List<Map<String, String>> scoresList = (List<Map<String, String>>) student.get("Scores");

                for (Map<String, String> scores : scoresList) {
                    String schoolYear = scores.get("School Year");

                    // Lặp qua từng môn học
                    for (int subjectID = 1; subjectID <= 13; subjectID++) {
                        String subjectName = getSubjectNameByID(subjectID);

                        // Tạo một bản đồ mới cho từng môn học
                        Map<String, Object> resultMap = new LinkedHashMap<>();
                        resultMap.put("ID", studentID);
                        resultMap.put("Student Name", studentName);
                        resultMap.put("Subject", subjectName);
                        resultMap.put("School Year", schoolYear);
                        resultMap.put("15 minutes", scores.get(subjectName + " Score 15 Min"));
                        resultMap.put("1 hour", scores.get(subjectName + " Score 1 Hour"));
                        resultMap.put("Mid term", scores.get(subjectName + " Score Mid Term"));
                        resultMap.put("Final exam", scores.get(subjectName + " Score Final Exam"));
                        resultMap.put("GPA", scores.get(subjectName + " Score Overall"));

                        result.add(resultMap);
                    }
                }
            }

            return result;

        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Phương thức để lấy tên môn học theo ID từ cơ sở dữ liệu
    private static String getSubjectNameByID(int subjectID) {
        Map<Integer, String> subjectMap = new HashMap<>();
        subjectMap.put(1, "Literature");
        subjectMap.put(2, "Math");
        subjectMap.put(3, "English");
        subjectMap.put(4, "History");
        subjectMap.put(5, "Geography");
        subjectMap.put(6, "Physics");
        subjectMap.put(7, "Chemistry");
        subjectMap.put(8, "Biology");
        subjectMap.put(9, "Citizen_Education");
        subjectMap.put(10, "National_Defense_And_Security_Education");
        subjectMap.put(11, "Technology");
        subjectMap.put(12, "Information_Technology");
        subjectMap.put(13, "Physical_Education");

        return subjectMap.get(subjectID);
    }
}
