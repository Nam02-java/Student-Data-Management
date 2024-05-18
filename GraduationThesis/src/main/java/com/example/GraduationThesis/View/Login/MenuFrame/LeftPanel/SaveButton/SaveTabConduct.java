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

        /**
         * Special variable
         * A student has 3 conducts corresponding to 3 years of study
         * For example, there are 3 students, the total number of conducts is 9, divided equally, each student has 3 conducts (9 / 3 = 3)
         * => only need 3 updates instead of 9 times and 3 times for 3 students respectively
         * The total number of records in the table is always guaranteed to be divisible by 3 thanks to strictly managing the addition of students
         */
        Integer divisibleCheck = model.getRowCount() / 3;

        for (int i = 0; i < model.getRowCount(); i++) {

            if (divisibleCheck != 0) {

                String payload = buildPayload(model, i);

                /**
                 * After finishing creating the payload for the first student
                 * i += 2 will be the starting number of conduct of the second student and the limit will have 3 loops -> elements will have 3 - 4 - 5
                 * now we have i = 5
                 * i += 2 will be the number of starting conduct of the third student and the limit will have 3 loops -> elements will have 6 - 7 -8
                 * Continue repeating until finished
                 */
                i += 2;

                if (payload != null) {
                    sendHttpRequest(payload, (Integer) model.getValueAt(i, 0));
                    divisibleCheck -= 1;
                } else {
                    System.out.println("Failed to build payload for row " + (i + 1));
                }

            } else {
                break;
            }
        }
        updateData(table);
    }


    private static String buildPayload(DefaultTableModel model, int selectedRow) {

        StringBuilder payloadBuilder = new StringBuilder();
        payloadBuilder.append("{");
        payloadBuilder.append("\"userId\": ").append(model.getValueAt(selectedRow, 0)).append(",");
        payloadBuilder.append("\"conductPayload\": {");
        payloadBuilder.append("\"conducts\": [");

        for (int i = selectedRow; i < selectedRow + 3; i++) {
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
                    payloadBuilder.append(",");
                }
            }
        }

        payloadBuilder.append("]");
        payloadBuilder.append("}");
        payloadBuilder.append("}");

        return payloadBuilder.toString();
    }


    private static void updateData(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Xóa dữ liệu hiện tại khỏi bảng

        List<Map<String, Object>> data = TabConductAction.Action();

        for (Map<String, Object> row : data) {
            int studentID = (int) row.get("ID");
            String studentName = (String) row.get("Student Name");

            // Get information about student's conduct line by line
            List<Map<String, Object>> conducts = (List<Map<String, Object>>) row.get("Conducts");

            if (conducts.size() >= 3) {

                int conductCount = 0; // Variable that counts the number of behavior records added to the table

                for (Map<String, Object> conduct : conducts) {
                    String schoolYear = (String) conduct.get("School_Year");
                    String conductToString = (String) conduct.get("Conduct");
                    String attendanceScore = (String) conduct.get("Attendance_Score");

                    if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {
                        model.addRow(new Object[]{studentID, studentName, schoolYear, conductToString, attendanceScore, "Delete"});
                    } else {
                        model.addRow(new Object[]{studentID, studentName, schoolYear, conductToString, attendanceScore});
                    }

                    conductCount++;

                    // If 3 behavior records have been injected, exit the loop
                    if (conductCount >= 3) {
                        break;
                    }
                }
            }
        }
    }
}