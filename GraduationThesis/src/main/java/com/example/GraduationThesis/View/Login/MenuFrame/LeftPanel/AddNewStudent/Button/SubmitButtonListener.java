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
    private JTextField schoolYearField;
    private ArrayList<JTextField> schoolYearList;
    private ArrayList<JTextField[]> scoreFields1;
    private ArrayList<JTextField[]> scoreFields2;
    private ArrayList<JTextField[]> scoreFields3;
    private ArrayList<JTextField[]> conductFieldsList;
    private JFrame jFrame;
    private MenuFrame menuFrame;
    private String[] subjects = {"Literature", "Math", "English", "History", "Geography", "Physics", "Chemistry", "Biology", "Citizen_Education", "National_Defense_And_Security_Education", "Technology", "Information_Technology", "Physical_Education"};
    private final List<String> fieldsToCheck = new ArrayList<>();


    public SubmitButtonListener(MenuFrame menuFrame, JFrame jFrame, JTextField usernameField, JTextField classnameField, JTextField emailField, JTextField dateOfBirthField, JTextField numberphoneField, JTextField addressField, JTextField positionField, JTextField teachernameField, JTextField partentsnameField, JTextField partensnumberphoneField, JTextField schoolYearField, ArrayList<JTextField> schoolYearList, ArrayList<JTextField[]> scoreFields1, ArrayList<JTextField[]> scoreFields2, ArrayList<JTextField[]> scoreFields3, ArrayList<JTextField[]> conductFieldsList) {
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
        this.schoolYearField = schoolYearField;
        this.schoolYearList = schoolYearList;
        this.scoreFields1 = scoreFields1;
        this.scoreFields2 = scoreFields2;
        this.scoreFields3 = scoreFields3;
        this.conductFieldsList = conductFieldsList;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        callAPI();
    }

    public void callAPI() {
        //Get data from JTextField fields
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


        // Create a JsonObject to represent the data
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

        // Create a JsonArray to represent the scorePayloads
        JsonArray scorePayloadsArray = new JsonArray();

        // Get data from scoreFields for each academic year
        JsonObject year1Scores = getScoresData(0, scoreFields1);
        JsonObject year2Scores = getScoresData(1, scoreFields2);
        JsonObject year3Scores = getScoresData(2, scoreFields3);


        // Add data for each academic year to scorePayloadsArray
        scorePayloadsArray.add(year1Scores);
        scorePayloadsArray.add(year2Scores);
        scorePayloadsArray.add(year3Scores);

        requestBody.add("scorePayloads", scorePayloadsArray);

        // Create a JsonObject to represent conductPayload
        JsonObject conductPayload = new JsonObject();
        JsonArray conductsArray = new JsonArray();

        // Loop through conductFieldsList to get conduct data for each academic year
        for (JTextField[] conductFields : conductFieldsList) {
            JsonObject conductObject = new JsonObject();
            JsonArray conductArray = new JsonArray();

            System.out.println(conductFields[0].getText().toString());

            // Get data from JTextField fields for conduct
            String schoolYear = conductFields[0].getText().replace(" ", "");
            String conduct = conductFields[1].getText().replace(" ", "");
            String attendanceScore = conductFields[2].getText().replace(" ", "");

            // Add conduct data to conductArray
            conductArray.add(schoolYear);
            conductArray.add(conduct);
            conductArray.add(attendanceScore);

            // Add conductArray to conductObject
            conductObject.add("conduct", conductArray);

            // Add conductObject to conductsArray
            conductsArray.add(conductObject);
        }

        conductPayload.add("conducts", conductsArray);
        requestBody.add("conductPayload", conductPayload);

        // Convert the request Body object to a JSON string
        String jsonBody = new Gson().toJson(requestBody);

        System.out.println("Add new student JSON: " + jsonBody);

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest
                .newBuilder()
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

    private JsonObject getScoresData(int chooseYear, ArrayList<JTextField[]> scoreFields) {

        JsonObject scorePayload = new JsonObject();

        String schoolYear = null;

        schoolYearField.setText(schoolYearField.getText().replace(" ", ""));


        schoolYearList.add(schoolYearField);


        schoolYear = schoolYearList.get(chooseYear).getText();

        scorePayload.addProperty("schoolYear", schoolYear);

        JsonArray scoresArray = new JsonArray();
        for (int j = 0; j < subjects.length; j++) {
            JsonObject scoreObject = new JsonObject();
            scoreObject.addProperty("subjectName", subjects[j]);
            JsonArray scores = new JsonArray();
            JTextField[] fields = scoreFields.get(j);

            for (JTextField field : fields) {
                String fieldValue = field.getText().isEmpty() ? "" : field.getText();
                scores.add(fieldValue);
            }

            scoreObject.add("scores", scores);
            scoresArray.add(scoreObject);
        }

        scorePayload.add("scores", scoresArray);
        return scorePayload;
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