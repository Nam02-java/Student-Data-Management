package com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.SaveButton;

import com.example.GraduationThesis.Model.Enitity.Users.Roles.ERole;
import com.example.GraduationThesis.Service.LazySingleton.ListRoles.ListRolesManager;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabScores.TabScoresAction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.SaveButton.SaveEditButtonListener.sendHttpRequest;

public class SaveTabScores {
    public static void sendUpdateRequest(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();


        for (int i = 0; i < model.getRowCount(); i++) {
            String payload = buildPayload(model, i);
            if (payload != null) {
                sendHttpRequest(payload, (Integer) model.getValueAt(i, 0));
            } else {
                System.out.println("Failed to build payload for row " + (i + 1));
            }
        }
        updateData(table);
    }

    private static String buildPayload(DefaultTableModel model, int selectedRow) {
        // Get the subject name from the row of the table
        Object subjectName = model.getValueAt(selectedRow, 2); // Subject name column

        // Get scores from the columns "15 minutes", "1 hour", "Mid term", "Final exam"
        List<String> scoresList = new ArrayList<>();
        for (int i = 3; i <= 6; i++) {
            Object score = model.getValueAt(selectedRow, i);
            if (score == "") {
                scoresList.add("\"\"");
            } else {
                scoresList.add("\"" + score.toString() + "\""); // Thêm dấu ngoặc kép vào giá trị
              //  scoresList.add(score.toString());
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
        //payloadBuilder.append("\"scores\": ").append(scoresList);
        payloadBuilder.append("\"scores\": [").append(String.join(",", scoresList)).append("]");
        payloadBuilder.append("}");
        payloadBuilder.append("]");
        payloadBuilder.append("}");
        payloadBuilder.append("}");
        return payloadBuilder.toString();
    }

    private static void updateData(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // delete current data in the table

        List<Map<String, Object>> data = TabScoresAction.Action();
        if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {
            data.forEach(row -> model.addRow(new Object[]{row.get("ID"), row.get("Student Name"), row.get("Subject"), row.get("15 minutes"), row.get("1 hour"), row.get("Mid term"), row.get("Final exam"), row.get("GPA"), "Delete"}));
        } else {
            data.forEach(row -> model.addRow(new Object[]{row.get("ID"), row.get("Student Name"), row.get("Subject"), row.get("15 minutes"), row.get("1 hour"), row.get("Mid term"), row.get("Final exam"), row.get("GPA")}));
        }
    }

    private static boolean isValidInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
