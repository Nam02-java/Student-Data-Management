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

    private static boolean flag;

    public static void sendUpdateRequest(JTable table) {

        flag = true;

        DefaultTableModel model = (DefaultTableModel) table.getModel();

        int countToFindOutTheSchoolYear = 0;

        for (int i = 0; i < model.getRowCount(); i++) {

            if (flag == false) {
                break;

            } else {

                String payload = null;

                for (int j = 4; j <= 7; j++) {

                    if (i % 13 == 0) {

                        if (countToFindOutTheSchoolYear >= 3) {
                            countToFindOutTheSchoolYear = 0;
                        }

                        countToFindOutTheSchoolYear += 1;

                        String schoolYear = model.getValueAt(i, 3).toString();
                        schoolYear.replace(" ", "");
                        if (schoolYear.isEmpty()) {
                            String studentName = model.getValueAt(i, 1).toString();

                            String schoolYearSeries = null;

                            switch (countToFindOutTheSchoolYear) {
                                case 1:
                                    schoolYearSeries = "First Year";
                                    break;
                                case 2:
                                    schoolYearSeries = "Second Year";
                                    break;
                                case 3:
                                    schoolYearSeries = "Third Year";
                                    break;
                            }

                            payload = "School year value of " + studentName + " at " + schoolYearSeries + " is not valid ";
                            sendHttpRequest(payload, (Integer) model.getValueAt(i, 0));

                            flag = false;

                            break;
                        }
                    }

                    String score = model.getValueAt(i, j).toString();

                    score = score.replace(" ", "");

                    if (score.isEmpty()) {
                        continue; // Skip empty scores
                    }


                    if (!isValidInteger(score)) {
                        if (j == 4) {
                            payload = "score 15 miniutes of " + model.getValueAt(i, 2) + " is not valid";
                            sendHttpRequest(payload, (Integer) model.getValueAt(i, 0));
                            flag = false;
                            break;
                        } else if (j == 5) {
                            payload = "score 1 hour of " + model.getValueAt(i, 2) + " is not valid";
                            sendHttpRequest(payload, (Integer) model.getValueAt(i, 0));
                            flag = false;
                            break;
                        } else if (j == 6) {
                            payload = "score mid term of " + model.getValueAt(i, 2) + " is not valid";
                            sendHttpRequest(payload, (Integer) model.getValueAt(i, 0));
                            flag = false;
                            break;
                        } else if (j == 7) {
                            payload = "score final term of " + model.getValueAt(i, 2) + " is not valid";
                            sendHttpRequest(payload, (Integer) model.getValueAt(i, 0));
                            flag = false;
                            break;
                        }
                    }
                }
            }
        }
        if (flag == true) {

            int totalRow = model.getRowCount();
            int numberOfSubjectsPerYear = 13;
            int totalYearStudyOfAstudent = 3;

            totalRow /= numberOfSubjectsPerYear;
            totalRow /= totalYearStudyOfAstudent;

            List<String> listId = new ArrayList<>();
            int countId = -39;

            String idStudent = null;
            for (int i = 0; i < totalRow; i++) {
                countId += 39;
                if (i == totalRow) {
                    idStudent = model.getValueAt(countId - 1, 0).toString();
                    listId.add(String.valueOf(idStudent));
                    break;
                }
                idStudent = model.getValueAt(countId, 0).toString();
                listId.add(String.valueOf(idStudent));
            }

            for (int i = 0; i < listId.size(); i++) {
                String payload = buildPayload(model, Integer.parseInt(listId.get(i)));
                if (payload != null) {
                    sendHttpRequest(payload, 0);
                }
            }
            updateData(table);
        }
    }


    private static String buildPayload(DefaultTableModel model, int selectedId) {
        Object studentId = selectedId;

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

    private static void updateData(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // delete current data in the table

        List<Map<String, Object>> data = TabScoresAction.Action();
        if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {
            data.forEach(row -> model.addRow(new Object[]{row.get("ID"), row.get("Student Name"), row.get("Subject"), row.get("School Year"), row.get("15 minutes"), row.get("1 hour"), row.get("Mid term"), row.get("Final exam"), row.get("GPA"), "Delete"}));
        } else {
            data.forEach(row -> model.addRow(new Object[]{row.get("ID"), row.get("Student Name"), row.get("Subject"), row.get("School Year"), row.get("15 minutes"), row.get("1 hour"), row.get("Mid term"), row.get("Final exam"), row.get("GPA")}));
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
