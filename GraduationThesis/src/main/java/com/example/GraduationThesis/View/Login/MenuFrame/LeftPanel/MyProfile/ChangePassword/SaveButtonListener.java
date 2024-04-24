package com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.MyProfile.ChangePassword;

import com.example.GraduationThesis.Service.LazySingleton.JsonWebToken.JsonWebTokenManager;
import com.example.GraduationThesis.Service.LazySingleton.Password.PasswordManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SaveButtonListener implements ActionListener {

    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    private JFrame jFrame;

    public SaveButtonListener(JFrame jFrame, JPasswordField currentPasswordField, JPasswordField newPasswordField, JPasswordField confirmPasswordField) {
        this.jFrame = jFrame;
        this.currentPasswordField = currentPasswordField;
        this.newPasswordField = newPasswordField;
        this.confirmPasswordField = confirmPasswordField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String rawPassword = currentPasswordField.getText();
        boolean isMatch = passwordEncoder.matches(rawPassword, PasswordManager.getInstance().getPassword());

        if (isMatch) {
            if (newPasswordField.getText().equals(confirmPasswordField.getText())) {
                String newPassword = newPasswordField.getText();
                callApi(newPassword);
            } else {
                JOptionPane.showMessageDialog(jFrame, "new password not match with confirm password");
            }
        } else {
            JOptionPane.showMessageDialog(jFrame, "current password not match with your password");

        }
    }

    public void callApi(String newPassword) {

        HttpClient httpClient = HttpClient.newHttpClient();

        String url = "http://localhost:8080/api/v1/public/updateUser";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + JsonWebTokenManager.getInstance().getJwtToken())
                .PUT(HttpRequest.BodyPublishers.ofString(
                        "{\"updates\": {\"password\": \"" + newPassword + "\"}}"))
                .build();

        try {
            // Make requests and receive responses from the API
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Check the response code from the API
            if (response.statusCode() == 200) {
                JOptionPane.showMessageDialog(jFrame, "Update password successful");
                PasswordManager.getInstance().setPassword(newPassword);
                jFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(jFrame, response.body());
            }
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(jFrame, "Error occurred: " + exception.getMessage());

        }
    }
}


