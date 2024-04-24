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

public class DeleteTabConductImplements implements ActionInterface {
    @Override
    public <T> void delete(T value, ActionType actionType) {

    }

    @Override
    public <T> void deleteTabScores(T value, JTable table, int selectedRow) {

    }

    @Override
    public <T> void deleteTabConduct(T value, JTable table, int selectedRow) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        String payload = buildPayload(model, selectedRow);
        sendRequest(payload);

    }

    @Override
    public void adminAuthorization(String numberPhone) {

    }

    private void sendRequest(String payload) {
        System.out.println("MY PAYLOAD :  " + payload);
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
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private String buildPayload(DefaultTableModel model, int selectedRow) {
        // get information student from table
        int userId = (int) model.getValueAt(selectedRow, 0);

        // get conduct information from table
        String conduct2017_2018 = (String) model.getValueAt(selectedRow, 2);
        String conduct2018_2019 = (String) model.getValueAt(selectedRow, 3);
        String conduct2019_2020 = (String) model.getValueAt(selectedRow, 4);
        String attendanceScore = (String) model.getValueAt(selectedRow, 5);

        // generate the corresponding JSON string
        StringBuilder payloadBuilder = new StringBuilder();
        payloadBuilder.append("{");
        payloadBuilder.append("\"userId\": ").append(userId).append(",");
        payloadBuilder.append("\"conductPayload\": {");
        payloadBuilder.append("\"conducts\": [");
        payloadBuilder.append("{");
        payloadBuilder.append("\"conduct\": [");
        payloadBuilder.append("\"").append(conduct2017_2018).append("\",");
        payloadBuilder.append("\"").append(conduct2018_2019).append("\",");
        payloadBuilder.append("\"").append(conduct2019_2020).append("\",");
        payloadBuilder.append("\"").append(attendanceScore).append("\"");
        payloadBuilder.append("]");
        payloadBuilder.append("}");
        payloadBuilder.append("]");
        payloadBuilder.append("}");
        payloadBuilder.append("}");
        return payloadBuilder.toString();
    }
}
