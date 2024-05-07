package com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.MyProfile.Save;

import com.example.GraduationThesis.Service.LazySingleton.Email.EmailManager;
import com.example.GraduationThesis.Service.LazySingleton.JsonWebToken.JsonWebTokenManager;
import com.example.GraduationThesis.Service.LazySingleton.NumberPhone.NumberPhoneManager;
import com.example.GraduationThesis.Service.LazySingleton.UserName.UserNameManager;
import com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.LeftPanel;
import com.example.GraduationThesis.View.Login.MenuFrame.MenuFrame;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
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
    private JTextField usernameField;
    private JTextField emailField;
    private JTextField numberPhoneField;
    private JFrame jFrame;
    private MenuFrame menuFrame;
    private final List<String> fieldsToCheck = new ArrayList<>();

    public SaveButtonListener(MenuFrame menuFrame, JFrame jFrame, JTextField usernameField, JTextField emailField, JTextField numberPhoneField) {
        this.menuFrame = menuFrame;
        this.jFrame = jFrame;
        this.usernameField = usernameField;
        this.emailField = emailField;
        this.numberPhoneField = numberPhoneField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String email = emailField.getText();
        String numberPhone = numberPhoneField.getText();

        HttpClient client = HttpClient.newHttpClient();

        String url = "http://localhost:8080/api/v1/public/updateUser";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + JsonWebTokenManager.getInstance().getJwtToken())
                .PUT(HttpRequest.BodyPublishers.ofString(
                        "{\"updates\": {\"username\": \"" + username + "\", \"email\": \"" + email + "\", \"numberphone\": \"" + numberPhone + "\"}}"))
                .build();

        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {

                JOptionPane.showMessageDialog(jFrame, "Update successful");

                UserNameManager.getInstance().setUsername(username);
                NumberPhoneManager.getInstance().setNumberphone(numberPhone);
                EmailManager.getInstance().setEmail(email);

                jFrame.dispose();

                LeftPanel.updateWelcomeLabel(username);

                menuFrame.setVisible(true);

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
