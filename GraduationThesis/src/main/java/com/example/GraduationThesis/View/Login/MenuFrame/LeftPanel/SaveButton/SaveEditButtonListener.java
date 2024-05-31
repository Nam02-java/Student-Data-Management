package com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.SaveButton;

import com.example.GraduationThesis.Service.LazySingleton.JsonWebToken.JsonWebTokenManager;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class SaveEditButtonListener implements ActionListener {
    private static JFrame jFrame;
    private JTabbedPane tabbedPane;
    private static JTable tableTabGeneralInformation;
    private static JTable tableTabPosition;
    private static JTable tableScores;
    private static JTable tableConduct;
    private static JTable tablePersonalInformation;

    private static List<String> fieldsToCheck = new ArrayList<>();

    public static Boolean flagSaveEditButton = true;

    /**
     * This variable is created for the purpose of targeting the scores tab
     * Number 2 will be the index number of the score tab
     */
    private static int selectedIndex;

    private static int count = 1;
    private static int countTabConduct;

    public SaveEditButtonListener(JTabbedPane tabbedPane, JTable tableTabGeneralInformation, JTable tableTabPosition, JTable tableScores, JTable tableConduct, JTable tablePersonalInformation, JFrame jFrame) {
        this.tabbedPane = tabbedPane;
        this.tableTabGeneralInformation = tableTabGeneralInformation;
        this.tableTabPosition = tableTabPosition;
        this.tableScores = tableScores;
        this.tableConduct = tableConduct;
        this.tablePersonalInformation = tablePersonalInformation;
        this.jFrame = jFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        selectedIndex = tabbedPane.getSelectedIndex();
        if (selectedIndex == 0) {
            SaveTabGeneralInformation.sendUpdateRequest(tableTabGeneralInformation);
        } else if (selectedIndex == 1) {
            SaveTabPosition.sendUpdateRequest(tableTabPosition);
        } else if (selectedIndex == 2) {
            SaveTabScores.sendUpdateRequest(tableScores);
        } else if (selectedIndex == 3) {
            countTabConduct = tableConduct.getRowCount() / 3;
            SaveTabConduct.sendUpdateRequest(tableConduct);
        } else if (selectedIndex == 4) {
            SaveTabPersonalInformation.sendUpdateRequest(tablePersonalInformation);
        }
    }

    public static void sendHttpRequest(String payload, int studentID) {
        System.out.println(payload);
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

            if (response.statusCode() == 200) {

                if (selectedIndex == 0) {
                    int lastRow = tableTabGeneralInformation.getRowCount();
                    count += 1;
                    if (count == lastRow) {
                        if (response.body().toString().contains("Student updated successfully")) {
                            JOptionPane.showMessageDialog(jFrame, "Updated all successfully");
                            count = 0;
                        }
                    }
                } else if (selectedIndex == 1) {
                    int lastRow = tableTabPosition.getRowCount();
                    count += 1;
                    if (count == lastRow) {
                        if (response.body().toString().contains("Student updated successfully")) {
                            JOptionPane.showMessageDialog(jFrame, "Updated all successfully");
                            count = 0;
                        }
                    }
                } else if (selectedIndex == 2) {
                    int lastRow = tableScores.getRowCount();
                    count += 39;
                    if (count >= lastRow) {
                        if (response.body().toString().contains("Student updated successfully")) {
                            JOptionPane.showMessageDialog(jFrame, "Updated all successfully");
                            count = 0;
                        }
                    }
                } else if (selectedIndex == 3) {
                    if (countTabConduct != 1) {
                        countTabConduct -= 1;
                    } else {
                        if (response.body().toString().contains("Student updated successfully")) {
                            JOptionPane.showMessageDialog(jFrame, "Updated all successfully");
                        }
                    }

                } else if (selectedIndex == 4) {
                    int lastRow = tablePersonalInformation.getRowCount();
                    count += 1;
                    if (count == lastRow) {
                        if (response.body().toString().contains("Student updated successfully")) {
                            JOptionPane.showMessageDialog(jFrame, "Updated all successfully");
                            count = 0;
                        }
                    }
                }

            } else {

                JOptionPane.showMessageDialog(jFrame, "ID " + studentID + " : " + payload);

            }

        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}

