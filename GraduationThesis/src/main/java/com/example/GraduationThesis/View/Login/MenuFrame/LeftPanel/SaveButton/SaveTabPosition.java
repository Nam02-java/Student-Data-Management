package com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.SaveButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import static com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.SaveButton.SaveEditButtonListener.sendHttpRequest;


public class SaveTabPosition {
    private static boolean flag;

    public static void sendUpdateRequest(JTable table) {
        flag = true;
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            if (flag == false) {
                break;
            } else {
                for (int j = 2; j <= 4; j++) {
                    String value = model.getValueAt(i, j).toString();
                    value = value.replace(" ", "");

                    String payload = null;

                    if (j == 2) { // j == 1 at column classname
                        if (value == null || value.trim().isEmpty()) {
                            payload = "Class name is empty";
                            sendHttpRequest(payload, (Integer) model.getValueAt(i, 0));
                            flag = false;
                            break;
                        }
                    } else if (j == 3) { // j == 2 at column position
                        if (value == null || value.trim().isEmpty()) {
                            payload = "Position is empty";
                            sendHttpRequest(payload, (Integer) model.getValueAt(i, 0));
                            flag = false;
                            break;
                        }
                    } else if (j == 4) { // j == 3 at column teachername
                        if (value == null || value.trim().isEmpty() || value.length() < 2 || value.length() > 50) {
                            payload = "Teacher name is not valid";
                            sendHttpRequest(payload, (Integer) model.getValueAt(i, 0));
                            flag = false;
                            break;
                        }
                    }
                }
            }
        }
        if (flag == true) {
            for (int i = 0; i < model.getRowCount(); i++) {
                String payload = buildPayload(model, i);
                sendHttpRequest(payload, (Integer) model.getValueAt(i, 0));
            }
        }
    }

    private static String buildPayload(DefaultTableModel model, int rowIndex) {
        StringBuilder payloadBuilder = new StringBuilder();
        payloadBuilder.append("{");
        payloadBuilder.append("\"userId\": ").append(model.getValueAt(rowIndex, 0)).append(",");
        payloadBuilder.append("\"updates\": {");
        payloadBuilder.append("\"username\": \"").append(model.getValueAt(rowIndex, 1)).append("\",");
        payloadBuilder.append("\"classname\": \"").append(model.getValueAt(rowIndex, 2)).append("\",");
        payloadBuilder.append("\"position\": \"").append(model.getValueAt(rowIndex, 3)).append("\",");
        payloadBuilder.append("\"teachername\": \"").append(model.getValueAt(rowIndex, 4)).append("\"");
        payloadBuilder.append("}");
        payloadBuilder.append("}");
        return payloadBuilder.toString();
    }
}
