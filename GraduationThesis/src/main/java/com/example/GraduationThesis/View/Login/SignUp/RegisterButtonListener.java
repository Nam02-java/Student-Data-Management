package com.example.GraduationThesis.View.Login.SignUp;

import com.example.GraduationThesis.View.Login.LoginFrame;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegisterButtonListener implements ActionListener {
    private JFrame jFrame;

    private JTextField usernameField;
    private JPasswordField passwordField;

    private JTextField numberphoneField;
    private JTextField emailField;
    private final List<String> fieldsToCheck = new ArrayList<>();


    public RegisterButtonListener(JFrame jFrame, JTextField usernameField, JPasswordField passwordField, JTextField numberphoneField, JTextField emailField) {
        this.jFrame = jFrame;
        this.usernameField = usernameField;
        this.passwordField = passwordField;
        this.numberphoneField = numberphoneField;
        this.emailField = emailField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String numberphone = numberphoneField.getText();
        String email = emailField.getText();

        if ("Enter username".equals(username)) {
            JOptionPane.showMessageDialog(jFrame, "username can not be null");
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(jFrame, "password can not be null");
            return;
        }

        if ("Enter Number Phone".equals(numberphone)) {
            JOptionPane.showMessageDialog(jFrame, "numberphone can not be null");
            return;
        }

        if ("Enter Email".equals(email)) {
            JOptionPane.showMessageDialog(jFrame, "email can not be null");
            return;
        }

        String requestBody;
        requestBody = new Gson().toJson(
                Map.of("email", email, "numberPhone", numberphone
                        , "userName", username, "password", password)
        );

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/v1/public/auth/signup"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JOptionPane.showMessageDialog(jFrame, response.body());
                jFrame.dispose();
                new LoginFrame();
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
        } catch (IOException | InterruptedException exception) {
            JOptionPane.showMessageDialog(jFrame, "Error occurred: " + exception.getMessage());
        }
    }

    private void displayErrorMessages(JsonObject responseBody) {

        // Add the keys to check to the fieldsToCheck list
        fieldsToCheck.clear();
        fieldsToCheck.add("userName");
        fieldsToCheck.add("password");
        fieldsToCheck.add("numberPhone");
        fieldsToCheck.add("email");


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
