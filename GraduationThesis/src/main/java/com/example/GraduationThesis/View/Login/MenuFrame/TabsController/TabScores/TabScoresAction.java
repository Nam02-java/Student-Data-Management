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
                // Iterate through each student
                int studentID = (int) student.get("ID");
                String studentName = (String) student.get("Student Name");

                // Get the scores map
                Map<String, String> scoresMap = (Map<String, String>) student.get("Scores");

                // Iterate through each subject and add the scores to the result
                for (int subjectID = 1; subjectID <= 13; subjectID++) {
                    String subjectName = getSubjectNameByID(subjectID); // Get the subject name

                    // Create a new map for each subject and add it to the result list
                    Map<String, Object> resultMap = new LinkedHashMap<>();
                    resultMap.put("ID", studentID);
                    resultMap.put("Student Name", studentName);
                    resultMap.put("Subject", subjectName);
                    resultMap.put("15 minutes", scoresMap.get(subjectName + " Score 15 Min"));
                    resultMap.put("1 hour", scoresMap.get(subjectName + " Score 1 Hour"));
                    resultMap.put("Mid term", scoresMap.get(subjectName + " Score Mid Term"));
                    resultMap.put("Final exam", scoresMap.get(subjectName + " Score_Final_Exam"));
                    resultMap.put("GPA", scoresMap.get(subjectName + " Score Overall"));

                    result.add(resultMap);
                }
            }

            return result;

        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Method to get subject name by ID from the database
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

