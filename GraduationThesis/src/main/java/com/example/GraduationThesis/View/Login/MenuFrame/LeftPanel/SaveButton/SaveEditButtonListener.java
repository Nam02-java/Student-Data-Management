package com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.SaveButton;

import com.example.GraduationThesis.Service.LazySingleton.JsonWebToken.JsonWebTokenManager;
import com.example.GraduationThesis.View.Login.LoginFrame;
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

public class SaveEditButtonListener implements ActionListener {
    private static JFrame jFrame;
    private JTabbedPane tabbedPane;
    private JTable tableTabGeneralInformation;
    private JTable tableTabPosition;
    private JTable tableScores;

    private JTable tableConduct;

    private JTable tablePersonalInformation;

    private static List<String> fieldsToCheck = new ArrayList<>();

    public static Boolean flagSaveEditButton = true;


    public SaveEditButtonListener(JTabbedPane tabbedPane, JTable tableTabGeneralInformation, JTable tableTabPosition, JTable tableScores, JTable tableConduct, JTable tablePersonalInformation, JFrame jFrame) {
        this.tabbedPane = tabbedPane;
        this.tableTabGeneralInformation = tableTabGeneralInformation;
        this.tableTabPosition = tableTabPosition;
        this.tableScores = tableScores;
        this.tableConduct = tableConduct;
        this.tablePersonalInformation = tablePersonalInformation;
        this.jFrame = jFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedIndex = tabbedPane.getSelectedIndex();
        if (selectedIndex == 0) {
            SaveTabGeneralInformation.sendUpdateRequest(tableTabGeneralInformation);
        } else if (selectedIndex == 1) {
            SaveTabPosition.sendUpdateRequest(tableTabPosition);
        } else if (selectedIndex == 2) {
            SaveTabScores.sendUpdateRequest(tableScores);
        } else if (selectedIndex == 3) {
            SaveTabConduct.sendUpdateRequest(tableConduct);
        } else if (selectedIndex == 4) {
            SaveTabPersonalInformation.sendUpdateRequest(tablePersonalInformation);
        }
    }


    public static void sendHttpRequest(String payload, int studentID) {
        HttpClient httpClient = HttpClient.newHttpClient();
        String url = "http://localhost:8080/api/v1/admin/updateStudentByID";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + JsonWebTokenManager.getInstance().getJwtToken())
                .PUT(HttpRequest.BodyPublishers.ofString(payload))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response from server: " + response.body());

            if (response.statusCode() == 200) {
                // Display successful update notification for each student
                JOptionPane.showMessageDialog(jFrame, "ID " + studentID + " : " + response.body());
            } else {
                flagSaveEditButton = false;
                // Check if string is JSON or not
                boolean isJson = isJSONValid(response.body());
                if (isJson) {
                    JsonObject responseBody = JsonParser.parseString(response.body()).getAsJsonObject();
                    displayErrorMessages(responseBody, studentID);
                } else {
                    JOptionPane.showMessageDialog(jFrame, "ID " + studentID + " : " + response.body().toString());
                }
                flagSaveEditButton = true;
            }

        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private static void displayErrorMessages(JsonObject responseBody, int studentID) {

        // Add the keys to check to the fieldsToCheck list
        fieldsToCheck.clear();
        fieldsToCheck.add("username");
        fieldsToCheck.add("classname");
        fieldsToCheck.add("email");
        fieldsToCheck.add("dateOfBirth");
        fieldsToCheck.add("numberphone");
        fieldsToCheck.add("address");
        fieldsToCheck.add("position");
        fieldsToCheck.add("teachername");
        fieldsToCheck.add("partentsname");
        fieldsToCheck.add("partensnumberphone");


        StringBuilder errorMessage = new StringBuilder();

        // Checks and displays error messages for the current field
        for (String field : fieldsToCheck) {
            if (responseBody.has(field)) {
                String fieldValue = responseBody.get(field).getAsString();
                // if (fieldValue.contains("is not empty or null") || fieldValue.contains("Number phone") || fieldValue.contains("Student name must have 2 to 50 characters")) {
                errorMessage.append(fieldValue);
                JOptionPane.showMessageDialog(jFrame, "ID " + studentID + " : " + errorMessage.toString());
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

