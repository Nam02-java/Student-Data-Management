package com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.SaveButton;

import com.example.GraduationThesis.Model.Enitity.Users.Roles.ERole;
import com.example.GraduationThesis.Service.LazySingleton.ListRoles.ListRolesManager;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabConduct.TabConductAction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.List;
import java.util.Map;

import static com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.SaveButton.SaveEditButtonListener.sendHttpRequest;

public class SaveTabConduct {

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
        // get information student from table
        int userId = (int) model.getValueAt(selectedRow, 0);

        String conduct2017_2018 = (String) model.getValueAt(selectedRow, 2);
        String conduct2018_2019 = (String) model.getValueAt(selectedRow, 3);
        String conduct2019_2020 = (String) model.getValueAt(selectedRow, 4);
        String attendanceScore = (String) model.getValueAt(selectedRow, 5);

        // build JSON
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

    private static void updateData(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // delete current data in table

        // get data from api
        List<Map<String, Object>> data = TabConductAction.Action();

        // iterate over each object in the list and add it to the table
        data.forEach(row -> {
            // get data of student
            int studentID = (int) row.get("ID");
            String studentName = (String) row.get("Student Name");

            List<Map<String, Object>> conducts = (List<Map<String, Object>>) row.get("Conducts");
            conducts.stream().findFirst().ifPresent(conduct -> {
                String conduct2017_2018 = (String) conduct.get("Conduct2017_2018");
                String conduct2018_2019 = (String) conduct.get("Conduct2018_2019");
                String conduct2019_2020 = (String) conduct.get("Conduct2019_2020");
                String attendanceScore = (String) conduct.get("Attendance_Score");

                if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {
                    model.addRow(new Object[]{studentID, studentName, conduct2017_2018, conduct2018_2019, conduct2019_2020, attendanceScore, "Delete"});

                } else {
                    model.addRow(new Object[]{studentID, studentName, conduct2017_2018, conduct2018_2019, conduct2019_2020, attendanceScore});
                }
            });
        });
    }
}
