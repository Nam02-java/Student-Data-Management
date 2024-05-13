package com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.SaveButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.SaveButton.SaveEditButtonListener.flagSaveEditButton;
import static com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.SaveButton.SaveEditButtonListener.sendHttpRequest;


public class SaveTabGeneralInformation {
    private static boolean flag;


    public static void sendUpdateRequest(JTable table) {
        flag = true;
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            if (flag == false) {
                break;
            } else {
                for (int j = 1; j <= 6; j++) {
                    String value = model.getValueAt(i, j).toString();
                    value = value.replace(" ", "");

                    String payload = null;

                    if (j == 1) { // j == 1 at column username
                        if (value == null || value.trim().isEmpty() || value.length() < 2) {
                            payload = "User name must have 2 to 50 characters";
                            sendHttpRequest(payload, (Integer) model.getValueAt(i, 0));
                            flag = false;
                            break;
                        }
                        for (int z = 0; z < model.getRowCount(); z++) {
                            String existingUserName = model.getValueAt(z, 1).toString();
                            if (existingUserName.equals(model.getValueAt(i, j))) {
                                if (z == i) {
                                    continue;
                                }
                                payload = "UserName of student already exists";
                                sendHttpRequest(payload, (Integer) model.getValueAt(i, 0));
                                flag = false;
                                break;
                            }
                        }
                    } else if (j == 2) { // j == 2 at column classname
                        if (value == null || value.trim().isEmpty()) {
                            payload = "Class name is empty";
                            sendHttpRequest(payload, (Integer) model.getValueAt(i, 0));
                            flag = false;
                            break;
                        }
                    } else if (j == 3) { // j == 3 at column position
                        if (value == null || value.trim().isEmpty()) {
                            payload = "Position is empty";
                            sendHttpRequest(payload, (Integer) model.getValueAt(i, 0));
                            flag = false;
                            break;
                        }
                    } else if (j == 4) { // j == 4 at column teacher name
                        if (value == null || value.trim().isEmpty() || value.length() < 2 || value.length() > 50) {
                            payload = "Teacher name is not valid";
                            sendHttpRequest(payload, (Integer) model.getValueAt(i, 0));
                            flag = false;
                            break;
                        }
                    } else if (j == 5) { // j == 5 at column address
                        if (value == null || value.trim().isEmpty()) {
                            payload = "Address is empty";
                            sendHttpRequest(payload, (Integer) model.getValueAt(i, 0));
                            flag = false;
                            break;
                        }
                    } else if (j == 6) { // j == 6 at column numberphone
                        if (!isValidPhoneNumber(value)) {
                            payload = "Invalid student's phone number format";
                            sendHttpRequest(payload, (Integer) model.getValueAt(i, 0));
                            flag = false;
                            break;
                        }
                        for (int z = 0; z < model.getRowCount(); z++) {
                            String existingNumberphone = model.getValueAt(z, 6).toString();
                            if (existingNumberphone.equals(model.getValueAt(i, j))) {
                                if (z == i) {
                                    continue;
                                }
                                payload = "Numberphone of student already exists";
                                sendHttpRequest(payload, (Integer) model.getValueAt(i, 0));
                                flag = false;
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (flag == true) {
            for (int i = 0; i < model.getRowCount(); i++) {
                String payload = buildPayload(model, i);
                sendHttpRequest(payload, (Integer) model.getValueAt(i, 0));
//                if (flagSaveEditButton == false) {
//                    break;
//                }
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
        payloadBuilder.append("\"teachername\": \"").append(model.getValueAt(rowIndex, 4)).append("\",");
        payloadBuilder.append("\"address\": \"").append(model.getValueAt(rowIndex, 5)).append("\",");
        payloadBuilder.append("\"numberphone\": \"").append(model.getValueAt(rowIndex, 6)).append("\"");
        payloadBuilder.append("}");
        payloadBuilder.append("}");
        return payloadBuilder.toString();
    }


    private static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("\\d{10,11}");
    }


}
