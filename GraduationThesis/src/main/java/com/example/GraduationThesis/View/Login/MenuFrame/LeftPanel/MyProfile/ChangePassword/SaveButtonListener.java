package com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.MyProfile.ChangePassword;

import com.example.GraduationThesis.Service.LazySingleton.JsonWebToken.JsonWebTokenManager;
import com.example.GraduationThesis.Service.LazySingleton.Password.PasswordManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class SaveButtonListener implements ActionListener {

    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JFrame jFrame;
    private final List<String> fieldsToCheck = new ArrayList<>();

    public SaveButtonListener(JFrame jFrame, JPasswordField currentPasswordField, JPasswordField newPasswordField, JPasswordField confirmPasswordField) {
        this.jFrame = jFrame;
        this.currentPasswordField = currentPasswordField;
        this.newPasswordField = newPasswordField;
        this.confirmPasswordField = confirmPasswordField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String rawPassword = currentPasswordField.getText();
        boolean isMatch = passwordEncoder.matches(rawPassword, PasswordManager.getInstance().getPassword());

        if (isMatch) {
            if (newPasswordField.getText().equals(confirmPasswordField.getText())) {
                String newPassword = newPasswordField.getText();
                callApi(newPassword);
            } else {
                JOptionPane.showMessageDialog(jFrame, "new password not match with confirm password");
            }
        } else {
            JOptionPane.showMessageDialog(jFrame, "current password not match with your password");

        }
    }

    public void callApi(String newPassword) {

        HttpClient httpClient = HttpClient.newHttpClient();

        String url = "http://localhost:8080/api/v1/public/updateUser";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + JsonWebTokenManager.getInstance().getJwtToken())
                .PUT(HttpRequest.BodyPublishers.ofString(
                        "{\"updates\": {\"password\": \"" + newPassword + "\"}}"))
                .build();

        try {
            // Make requests and receive responses from the API
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Check the response code from the API
            if (response.statusCode() == 200) {
                JOptionPane.showMessageDialog(jFrame, "Update password successful");
                PasswordManager.getInstance().setPassword(newPassword);
                jFrame.dispose();
            } else {

                // Check if string is JSON or not
                boolean isJson = isJSONValid(response.body());

                if (isJson) {
                    JsonObject responseBody = JsonParser.parseString(response.body()).getAsJsonObject();
                    displayErrorMessages(responseBody);
                } else {
                    JOptionPane.showMessageDialog(jFrame, response.body().toString());
                }
            }

        } catch (Exception exception) {
            JOptionPane.showMessageDialog(jFrame, "Error occurred: " + exception.getMessage());

        }
    }

    private void displayErrorMessages(JsonObject responseBody) {

        // Add the keys to check to the fieldsToCheck list
        fieldsToCheck.clear();
        fieldsToCheck.add("email");
        fieldsToCheck.add("numberphone");
        fieldsToCheck.add("username");
        fieldsToCheck.add("password");

        StringBuilder errorMessage = new StringBuilder();

        // Checks and displays error messages for the current field
        for (String field : fieldsToCheck) {
            if (responseBody.has(field)) {
                String fieldValue = responseBody.get(field).getAsString();
                // if (fieldValue.contains("is not empty or null") || fieldValue.contains("Number phone") || fieldValue.contains("Student name must have 2 to 50 characters")) {
                errorMessage.append(fieldValue);
                JOptionPane.showMessageDialog(jFrame, errorMessage.toString());
                return; // Stop the loop after displaying the first error message
            }
        }
    }

    // Method to check if string is JSON or not
    public static boolean isJSONValid(String jsonInString) {
        try {
            JsonParser.parseString(jsonInString);
            return true;
        } catch (JsonSyntaxException ex) {
            return false;
        }
    }
}


