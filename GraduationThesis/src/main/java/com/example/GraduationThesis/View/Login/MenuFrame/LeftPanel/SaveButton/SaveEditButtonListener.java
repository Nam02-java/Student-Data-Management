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

public class SaveEditButtonListener implements ActionListener {
    private JTabbedPane tabbedPane;
    private JTable tableTabGeneralInformation;
    private JTable tableTabPosition;
    private JTable tableScores;

    private JTable tableConduct;

    private JTable tablePersonalInformation;

    public SaveEditButtonListener(JTabbedPane tabbedPane, JTable tableTabGeneralInformation, JTable tableTabPosition, JTable tableScores, JTable tableConduct, JTable tablePersonalInformation) {
        this.tabbedPane = tabbedPane;
        this.tableTabGeneralInformation = tableTabGeneralInformation;
        this.tableTabPosition = tableTabPosition;
        this.tableScores = tableScores;
        this.tableConduct = tableConduct;
        this.tablePersonalInformation = tablePersonalInformation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedIndex = tabbedPane.getSelectedIndex();
        if (selectedIndex == 0) {
            SaveTabGeneralInformation.sendUpdateRequest(tableTabGeneralInformation);
        } else if (selectedIndex == 1) {
            SaveTabPosition.sendUpdateRequest(tableTabPosition);
        } else if (selectedIndex == 2) {
            SaveTabScores.sendUpdateRequest(tableScores);
        } else if (selectedIndex == 3) {
            SaveTabConduct.sendUpdateRequest(tableConduct);
        } else if (selectedIndex == 4) {
            SaveTabPersonalInformation.sendUpdateRequest(tablePersonalInformation);
        }
        JOptionPane.showMessageDialog(null, "Update successful", "Success", JOptionPane.INFORMATION_MESSAGE);
    }


    public static void sendHttpRequest(String payload) {
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
}

