package com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.AddNewStudent.Button;

import com.example.GraduationThesis.Service.LazySingleton.JsonWebToken.JsonWebTokenManager;
import com.example.GraduationThesis.View.Login.MenuFrame.MenuFrame;
import com.google.gson.*;

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

public class SubmitButtonListener implements ActionListener {
    private JTextField usernameField;
    private JTextField classnameField;
    private JTextField emailField;
    private JTextField dateOfBirthField;
    private JTextField numberphoneField;
    private JTextField addressField;
    private JTextField positionField;
    private JTextField teachernameField;
    private JTextField partentsnameField;
    private JTextField partensnumberphoneField;
    private ArrayList<JTextField[]> scoreFields;
    private JTextField conduct2017_2018TextField;
    private JTextField conduct2018_2019TextField;
    private JTextField conduct2019_2020TextField;
    private JTextField attendanceScoreTextField;
    private JFrame jFrame;
    private MenuFrame menuFrame;
    private String[] subjects = {"Literature", "Math", "English", "History", "Geography", "Physics", "Chemistry", "Biology", "Citizen_Education", "National_Defense_And_Security_Education", "Technology", "Information_Technology", "Physical_Education"};
    private final List<String> fieldsToCheck = new ArrayList<>();

    public SubmitButtonListener(MenuFrame menuFrame, JFrame jFrame, JTextField usernameField, JTextField classnameField, JTextField emailField, JTextField dateOfBirthField, JTextField numberphoneField, JTextField addressField, JTextField positionField, JTextField teachernameField, JTextField partentsnameField, JTextField partensnumberphoneField, ArrayList<JTextField[]> scoreFields, JTextField conduct2017_2018TextField, JTextField conduct2018_2019TextField, JTextField conduct2019_2020TextField, JTextField attendanceScoreTextField) {
        this.menuFrame = menuFrame;
        this.jFrame = jFrame;
        this.usernameField = usernameField;
        this.classnameField = classnameField;
        this.emailField = emailField;
        this.dateOfBirthField = dateOfBirthField;
        this.numberphoneField = numberphoneField;
        this.addressField = addressField;
        this.positionField = positionField;
        this.teachernameField = teachernameField;
        this.partentsnameField = partentsnameField;
        this.partensnumberphoneField = partensnumberphoneField;
        this.scoreFields = scoreFields;
        this.conduct2017_2018TextField = conduct2017_2018TextField;
        this.conduct2018_2019TextField = conduct2018_2019TextField;
        this.conduct2019_2020TextField = conduct2019_2020TextField;
        this.attendanceScoreTextField = attendanceScoreTextField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        callAPI();

    }


    public void callAPI() {

        // Get data from JTextField fields
        String username = usernameField.getText();
        String classname = classnameField.getText();
        String email = emailField.getText();
        String dateOfBirth = dateOfBirthField.getText();
        String numberphone = numberphoneField.getText();
        String address = addressField.getText();
        String position = positionField.getText();
        String teachername = teachernameField.getText();
        String partentsname = partentsnameField.getText();
        String partensnumberphone = partensnumberphoneField.getText();

        // Create a Json Object to represent the data
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("username", username);
        requestBody.addProperty("classname", classname);
        requestBody.addProperty("email", email);
        requestBody.addProperty("dateOfBirth", dateOfBirth);
        requestBody.addProperty("numberphone", numberphone);
        requestBody.addProperty("address", address);
        requestBody.addProperty("position", position);
        requestBody.addProperty("teachername", teachername);
        requestBody.addProperty("partentsname", partentsname);
        requestBody.addProperty("partensnumberphone", partensnumberphone);

        // Create a Json Object to represent the Payload score
        JsonObject scorePayload = new JsonObject();
        JsonArray scoresArray = new JsonArray();

        // Loop through the Fields scores and add them to the scores Array
        for (int i = 0; i < scoreFields.size(); i++) {
            String subject = subjects[i];
            JTextField[] fields = scoreFields.get(i);

            String fifteenMinutes = fields[0].getText();
            String oneHour = fields[1].getText();
            String midTerm = fields[2].getText();
            String finalTerm = fields[3].getText();

            JsonObject scoreObject = new JsonObject();
            scoreObject.addProperty("subjectName", subject);
            JsonArray scores = new JsonArray();

            /**
             * if text = null -> text = "0"
             */
            for (JTextField field : fields) {
                String fieldValue = field.getText().isEmpty() ? "" : field.getText();
                scores.add(fieldValue);
            }

            scores.add(fifteenMinutes);
            scores.add(oneHour);
            scores.add(midTerm);
            scores.add(finalTerm);

            scoreObject.add("scores", scores);

            scoresArray.add(scoreObject);
        }
        scorePayload.add("scores", scoresArray);

        // Create a JsonObject object to represent conductPay
        JsonObject conductPayload = new JsonObject();
        JsonArray conductsArray = new JsonArray();

        // Create a JsonObject for each conduct and add it to conductsArray
        JsonObject conductObject = new JsonObject();
        JsonArray conductArray = new JsonArray();
        String conduct2017_2018 = conduct2017_2018TextField.getText();
        String conduct2018_2019 = conduct2018_2019TextField.getText();
        String conduct2019_2020 = conduct2019_2020TextField.getText();
        String attendanceScore = attendanceScoreTextField.getText();
        conductArray.add(conduct2017_2018);
        conductArray.add(conduct2018_2019);
        conductArray.add(conduct2019_2020);
        conductArray.add(attendanceScore);
        conductObject.add("conduct", conductArray);
        conductsArray.add(conductObject);
        conductPayload.add("conducts", conductsArray);

        requestBody.add("scorePayload", scorePayload);
        requestBody.add("conductPayload", conductPayload);

        // Convert the request Body object to a JSON string
        String jsonBody = new Gson().toJson(requestBody);

        System.out.println(jsonBody);

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/v1/admin/registerStudent"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + JsonWebTokenManager.getInstance().getJwtToken())
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JOptionPane.showMessageDialog(jFrame, response.body());
                jFrame.dispose();
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
        } catch (IOException | InterruptedException exception) {
            JOptionPane.showMessageDialog(jFrame, "Error occurred: " + exception.getMessage());
        }
    }

    private void displayErrorMessages(JsonObject responseBody) {

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