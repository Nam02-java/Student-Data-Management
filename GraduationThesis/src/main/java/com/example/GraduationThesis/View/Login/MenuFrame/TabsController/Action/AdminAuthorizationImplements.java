package com.example.GraduationThesis.View.Login.MenuFrame.TabsController.Action;

import com.example.GraduationThesis.Service.LazySingleton.JsonWebToken.JsonWebTokenManager;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ActionType;

import javax.swing.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AdminAuthorizationImplements implements ActionInterface {
    @Override
    public <T> void delete(T value, ActionType actionType) {

    }

    @Override
    public <T> void deleteTabScores(T value, JTable table, int selectedRow) {

    }

    @Override
    public <T> void deleteTabConduct(T value, JTable table, int selectedRow) {

    }


    @Override
    public void adminAuthorization(String numberPhone) {
        HttpClient httpClient = HttpClient.newHttpClient();

        try {

            String apiUrl = "http://localhost:8080/api/v1/admin/adminAuthorization/" + numberPhone;
            // create request HTTP DELETE
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Authorization", "Bearer " + JsonWebTokenManager.getInstance().getJwtToken())
                    .method("POST", HttpRequest.BodyPublishers.noBody()) // Sử dụng phương thức PUT và không có nội dung trong yêu cầu
                    .build();

            // Call API and receive response from server
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Handling the result
            int statusCode = response.statusCode();
            if (statusCode == 200) {
                JOptionPane.showMessageDialog(null, response.body());
                JOptionPane.showMessageDialog(null, "Please log in again");
            } else {
                JOptionPane.showMessageDialog(null, response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
