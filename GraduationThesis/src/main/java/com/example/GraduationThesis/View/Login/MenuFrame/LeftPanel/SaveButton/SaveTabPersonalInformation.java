package com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.SaveButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import static com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.SaveButton.SaveEditButtonListener.sendHttpRequest;

public class SaveTabPersonalInformation {
    public static void sendUpdateRequest(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String payload = buildPayload(model, i);
            if (payload != null) {
                System.out.println("MY PAYLOAD : " + payload);
                sendHttpRequest(payload);
            } else {
                System.out.println("Failed to build payload for row " + (i + 1));
            }
        }
    }

    private static String buildPayload(DefaultTableModel model, int rowIndex) {

        StringBuilder payloadBuilder = new StringBuilder();
        payloadBuilder.append("{");
        payloadBuilder.append("\"userId\": ").append(model.getValueAt(rowIndex, 0)).append(",");
        payloadBuilder.append("\"updates\": {");
        payloadBuilder.append("\"username\": \"").append(model.getValueAt(rowIndex, 1)).append("\",");
        payloadBuilder.append("\"email\": \"").append(model.getValueAt(rowIndex, 2)).append("\",");
        payloadBuilder.append("\"dateOfBirth\": \"").append(model.getValueAt(rowIndex, 3)).append("\",");
        payloadBuilder.append("\"address\": \"").append(model.getValueAt(rowIndex, 4)).append("\",");
        payloadBuilder.append("\"numberphone\": \"").append(model.getValueAt(rowIndex, 5)).append("\",");
        payloadBuilder.append("\"partentsname\": \"").append(model.getValueAt(rowIndex, 6)).append("\",");
        payloadBuilder.append("\"partensnumberphone\": \"").append(model.getValueAt(rowIndex, 7)).append("\"");
        payloadBuilder.append("}");
        payloadBuilder.append("}");
        return payloadBuilder.toString();
    }
}

