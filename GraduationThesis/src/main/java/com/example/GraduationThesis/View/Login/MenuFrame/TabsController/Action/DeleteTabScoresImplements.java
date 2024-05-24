package com.example.GraduationThesis.View.Login.MenuFrame.TabsController.Action;

import com.example.GraduationThesis.Service.LazySingleton.JsonWebToken.JsonWebTokenManager;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ActionType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


public class DeleteTabScoresImplements implements ActionInterface {
    @Override
    public <T> void delete(T value, ActionType actionType) {

    }

    @Override
    public <T> void deleteTabScores(T value, JTable table, int selectedRow) {

        DefaultTableModel model = (DefaultTableModel) table.getModel();

        String payload = buildPayload(model, selectedRow);

        sendRequest(payload);

    }

    @Override
    public <T> void deleteTabConduct(T value, JTable table, int selectedRow) {

    }

    @Override
    public void adminAuthorization(String numberPhone) {

    }

    private void sendRequest(String payload) {
        System.out.println("MY PAYLOAD :  " + payload);
        HttpClient httpClient = HttpClient.newHttpClient();
        String url = "http://localhost:8080/api/v1/admin/updateStudentByID";
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json").header("Authorization", "Bearer " + JsonWebTokenManager.getInstance().getJwtToken()).PUT(HttpRequest.BodyPublishers.ofString(payload)).build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response from server: " + response.body());
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }


    private static String buildPayload(DefaultTableModel model, int selectedRow) {
        // get the subject name from the row of the table
        Object subjectName = model.getValueAt(selectedRow, 2); // subject name column = 3

        // get scores from the columns "15 minutes", "1 hour", "Mid term", "Final exam"
        List<String> scoresList = new ArrayList<>();
        for (int i = 3; i <= 6; i++) {
            Object score = model.getValueAt(selectedRow, i);
            if (score == "") {
                scoresList.add("\"\"");
            }
        }

        // build payload JSON
        StringBuilder payloadBuilder = new StringBuilder();
        payloadBuilder.append("{");
        payloadBuilder.append("\"userId\": ").append(model.getValueAt(selectedRow, 0)).append(",");
        payloadBuilder.append("\"scorePayload\": {");
        payloadBuilder.append("\"scores\": [");
        payloadBuilder.append("{");
        payloadBuilder.append("\"subjectName\": \"").append(subjectName).append("\",");
        payloadBuilder.append("\"scores\": ").append(scoresList);
        payloadBuilder.append("}");
        payloadBuilder.append("]");
        payloadBuilder.append("}");
        payloadBuilder.append("}");
        return payloadBuilder.toString();
    }
}
