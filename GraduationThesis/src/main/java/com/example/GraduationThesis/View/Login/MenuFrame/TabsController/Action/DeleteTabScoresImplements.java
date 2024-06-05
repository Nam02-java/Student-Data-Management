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

        // remove space line
        int rowCount = model.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            boolean allEmpty = true;
            for (int j = 0; j < model.getColumnCount(); j++) {
                Object valueModel = model.getValueAt(i, j);

                if (valueModel != null && !valueModel.toString().isEmpty()) {
                    allEmpty = false;
                    break;
                }
            }
            if (allEmpty) {
                model.removeRow(i);
            }
        }

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


    /**
     * no needed
     * this is old method
     * @param model
     * @param selectedRow
     * @return
     */
//    private static String buildPayload(DefaultTableModel model, int selectedRow) {
//        // get the subject name from the row of the table
//        Object subjectName = model.getValueAt(selectedRow, 2); // subject name column = 2
//
//        // get scores from the columns "School year", "15 minutes", "1 hour", "Mid term", "Final exam"
//        List<String> scoresList = new ArrayList<>();
//        for (int i = 3; i <= 7; i++) {
//            Object score = model.getValueAt(selectedRow, i);
//            if (score == "") {
//                scoresList.add("\"\"");
//            }
//        }
//
//        // build payload JSON
//        StringBuilder payloadBuilder = new StringBuilder();
//        payloadBuilder.append("{");
//        payloadBuilder.append("\"userId\": ").append(model.getValueAt(selectedRow, 0)).append(",");
//        payloadBuilder.append("\"scorePayload\": {");
//        payloadBuilder.append("\"scores\": [");
//        payloadBuilder.append("{");
//        payloadBuilder.append("\"subjectName\": \"").append(subjectName).append("\",");
//        payloadBuilder.append("\"scores\": ").append(scoresList);
//        payloadBuilder.append("}");
//        payloadBuilder.append("]");
//        payloadBuilder.append("}");
//        payloadBuilder.append("}");
//        return payloadBuilder.toString();
//    }

    /**
     * new method to build payload
     * code by namjava02 at 28/5/24
     * @param model
     * @param selectedRow
     * @return
     */
    private static String buildPayload(DefaultTableModel model, int selectedRow) {
        // get the subject name from the row of the table
        Object studentId = model.getValueAt(selectedRow, 0); // subject name column = 2

        // get scores from the columns "School year", "15 minutes", "1 hour", "Mid term", "Final exam"
        List<String> scoresList = new ArrayList<>();

        List<String> schoolYearList = new ArrayList<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            Object presentId = model.getValueAt(i, 0);
            if (studentId.equals(presentId)) {

                Object subjectName = model.getValueAt(i, 2);
                if (subjectName.equals("Literature")) {
                    Object schoolYear = model.getValueAt(i, 3);
                    schoolYearList.add("\"" + schoolYear.toString() + "\""); // Add quotes to the value
                }

                for (int j = 4; j <= 7; j++) {
                    Object score = model.getValueAt(i, j);
                    if (score == "") {
                        scoresList.add("\"\"");
                        continue;
                    }
                    scoresList.add("\"" + score.toString() + "\""); // Add quotes to the value
                }
            }
        }

        // build payload JSON
        StringBuilder payloadBuilder = new StringBuilder();
        payloadBuilder.append("{");
        payloadBuilder.append("\"userId\": ").append(studentId).append(",");
        payloadBuilder.append("\"scorePayloads\": [");

        int numSubjects = 13; // Fixed number of subjects for consistency
        int expectedScoresPerYear = numSubjects * 4;

        for (int i = 0; i < schoolYearList.size(); i++) {
            if (i > 0) {
                payloadBuilder.append(",");
            }
            payloadBuilder.append("{");
            payloadBuilder.append("\"schoolYear\": ").append(schoolYearList.get(i)).append(",");
            payloadBuilder.append("\"scores\": [");

            for (int j = 0; j < numSubjects; j++) {
                if (j > 0) {
                    payloadBuilder.append(",");
                }
                int subjectIndex = j;
                Object subjectName = model.getValueAt(subjectIndex, 2);
                payloadBuilder.append("{");
                payloadBuilder.append("\"subjectName\": \"").append(subjectName).append("\",");
                payloadBuilder.append("\"scores\": [");

                for (int k = 0; k < 4; k++) {
                    if (k > 0) {
                        payloadBuilder.append(",");
                    }
                    int scoreIndex = i * expectedScoresPerYear + j * 4 + k;
                    if (scoreIndex < scoresList.size()) {
                        payloadBuilder.append(scoresList.get(scoreIndex));
                    } else {
                        payloadBuilder.append("\"\"");
                    }
                }
                payloadBuilder.append("]");
                payloadBuilder.append("}");
            }

            payloadBuilder.append("]");
            payloadBuilder.append("}");
        }

        payloadBuilder.append("]");
        payloadBuilder.append("}");
        return payloadBuilder.toString();
    }
}