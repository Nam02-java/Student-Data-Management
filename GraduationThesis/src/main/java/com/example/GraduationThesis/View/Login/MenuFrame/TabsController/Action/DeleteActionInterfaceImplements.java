package com.example.GraduationThesis.View.Login.MenuFrame.TabsController.Action;

import com.example.GraduationThesis.Service.LazySingleton.JsonWebToken.JsonWebTokenManager;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ActionType;

import javax.swing.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DeleteActionInterfaceImplements implements ActionInterface {


    @Override
    public <T> void delete(T value, ActionType actionType) {
        HttpClient httpClient = HttpClient.newHttpClient();

        String apiUrl = null;
        try {
            if (actionType == ActionType.DELETE_USER) {
                apiUrl = "http://localhost:8080/api/v1/admin/user/" + value;
            } else if (actionType == ActionType.DELETE_STUDENT) {
                apiUrl = "http://localhost:8080/api/v1/admin/student/" + value;
            }

            // create HttpRequest HTTP DELETE
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Authorization", "Bearer " + JsonWebTokenManager.getInstance().getJwtToken())
                    .DELETE()
                    .build();

            // call API and get response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // handling the result
            int statusCode = response.statusCode();
            if (statusCode == 200) {
                System.out.println("Student with ID " + value + " deleted successfully.");
            } else if (statusCode == 404) {
                System.out.println("Student with ID " + value + " not found.");
            } else {
                System.out.println("Failed to delete student with ID " + value + ". Error: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> void deleteTabScores(T value, JTable table ,int selectedRow) {

    }

    @Override
    public <T> void deleteTabConduct(T value, JTable table, int selectedRow) {

    }


    @Override
    public void adminAuthorization(String numberPhone) {


    }
}

