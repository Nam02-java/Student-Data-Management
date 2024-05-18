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
        System.out.println("Payload delete : " + payload);
        sendRequest(payload);

    }

    @Override
    public void adminAuthorization(String numberPhone) {

    }

    private void sendRequest(String payload) {
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

        int lastResult = 0;

        Object currentID = model.getValueAt(selectedRow, 0);

        Object checkID;
        for (int i = 0; i < model.getRowCount(); i++) {
            checkID = model.getValueAt(i, 0);
            if (currentID.equals(checkID)) {
                lastResult = i;
                System.out.println("lastResult : " + lastResult);
                break;
            }
        }


        StringBuilder payloadBuilder = new StringBuilder();
        payloadBuilder.append("{");
        payloadBuilder.append("\"userId\": ").

                append(model.getValueAt(selectedRow, 0)).

                append(",");

        payloadBuilder.append("\"conductPayload\": {");
        payloadBuilder.append("\"conducts\": [");

        int countToStopComma = 1;
        for (int i = lastResult; i < lastResult + 3; i++) {
            if (i < model.getRowCount()) {
                String schoolYear = model.getValueAt(i, 2).toString().replace(" ", "");
                String conduct = model.getValueAt(i, 3).toString().replace(" ", "");
                String attendanceScore = model.getValueAt(i, 4).toString().replace(" ", "");

                payloadBuilder.append("{");
                payloadBuilder.append("\"conduct\": [");
                payloadBuilder.append("\"").append(schoolYear).append("\",");
                payloadBuilder.append("\"").append(conduct).append("\",");
                payloadBuilder.append("\"").append(attendanceScore).append("\"");
                payloadBuilder.append("]");
                payloadBuilder.append("}");
                if (i != selectedRow + 2 && i != model.getRowCount() - 1) {
                    if(countToStopComma==3){
                        break;
                    }
                    payloadBuilder.append(",");
                    countToStopComma += 1;
                }
            }
        }

        payloadBuilder.append("]");
        payloadBuilder.append("}");
        payloadBuilder.append("}");

        return payloadBuilder.toString();
    }
}
