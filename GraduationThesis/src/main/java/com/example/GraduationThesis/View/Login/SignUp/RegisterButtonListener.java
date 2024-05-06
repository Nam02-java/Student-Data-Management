package com.example.GraduationThesis.View.Login.SignUp;

import com.example.GraduationThesis.View.Login.LoginFrame;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class RegisterButtonListener implements ActionListener {
    private JFrame jFrame;

    private JTextField usernameField;
    private JPasswordField passwordField;

    private JTextField numberphoneField;
    private JTextField emailField;


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
                JsonObject responseBody = JsonParser.parseString(response.body()).getAsJsonObject();
                displayErrorMessages(responseBody);
            }
        } catch (IOException | InterruptedException exception) {
            JOptionPane.showMessageDialog(jFrame, "Error occurred: " + exception.getMessage());
        }
    }

    private void displayErrorMessages(JsonObject responseBody) {
        StringBuilder errorMessage = new StringBuilder();

        // Check and append error messages for the "userName" field
        if (responseBody.has("userName")) {
            errorMessage.append("User Name: ").append(responseBody.get("userName").getAsString()).append("\n");
            // Display error message and return if error found for "userName" field
            JOptionPane.showMessageDialog(jFrame, errorMessage.toString());
            return;
        }

        // Check and append error messages for the "password" field
        if (responseBody.has("password")) {
            errorMessage.append("Password: ").append(responseBody.get("password").getAsString()).append("\n");
            // Display error message and return if error found for "password" field
            JOptionPane.showMessageDialog(jFrame, errorMessage.toString());
            return;
        }
        if (responseBody.has("numberPhone")) {
            errorMessage.append("Number Phone: ").append(responseBody.get("numberPhone").getAsString()).append("\n");
            // Display error message and return if error found for "numberPhone" field
            JOptionPane.showMessageDialog(jFrame, errorMessage.toString());
            return;
        }

        // Check and append error messages for the "email" field
        if (responseBody.has("email")) {
            errorMessage.append("Email: ").append(responseBody.get("email").getAsString()).append("\n");
            // Display error message and return if error found for "email" field
            JOptionPane.showMessageDialog(jFrame, errorMessage.toString());
        }
    }
}
