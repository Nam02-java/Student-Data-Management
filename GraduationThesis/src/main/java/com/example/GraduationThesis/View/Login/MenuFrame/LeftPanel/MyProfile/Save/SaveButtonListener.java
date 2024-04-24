package com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.MyProfile.Save;

import com.example.GraduationThesis.Service.LazySingleton.Email.EmailManager;
import com.example.GraduationThesis.Service.LazySingleton.JsonWebToken.JsonWebTokenManager;
import com.example.GraduationThesis.Service.LazySingleton.NumberPhone.NumberPhoneManager;
import com.example.GraduationThesis.Service.LazySingleton.UserName.UserNameManager;
import com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.LeftPanel;
import com.example.GraduationThesis.View.Login.MenuFrame.MenuFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SaveButtonListener implements ActionListener {
    private JTextField usernameField;
    private JTextField emailField;
    private JTextField numberPhoneField;

    private JFrame jFrame;
    private MenuFrame menuFrame;

    public SaveButtonListener(MenuFrame menuFrame, JFrame jFrame, JTextField usernameField, JTextField emailField, JTextField numberPhoneField) {
        this.menuFrame = menuFrame;
        this.jFrame = jFrame;
        this.usernameField = usernameField;
        this.emailField = emailField;
        this.numberPhoneField = numberPhoneField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Lấy thông tin người dùng từ các JTextField
        String username = usernameField.getText();
        String email = emailField.getText();
        String numberPhone = numberPhoneField.getText();

        // Tạo một đối tượng HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Xây dựng URL của API update user
        String url = "http://localhost:8080/api/v1/public/updateUser";

        // Tạo một yêu cầu HTTP PUT
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + JsonWebTokenManager.getInstance().getJwtToken())
                .PUT(HttpRequest.BodyPublishers.ofString(
                        "{\"updates\": {\"username\": \"" + username + "\", \"email\": \"" + email + "\", \"numberphone\": \"" + numberPhone + "\"}}"))
                .build();

        try {
            // Thực hiện yêu cầu và nhận phản hồi từ API
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Kiểm tra mã phản hồi từ API
            if (response.statusCode() == 200) {
                JOptionPane.showMessageDialog(jFrame, "Update successful");
                UserNameManager.getInstance().setUsername(username);
                NumberPhoneManager.getInstance().setNumberphone(numberPhone);
                EmailManager.getInstance().setEmail(email);
                jFrame.dispose();
                LeftPanel.updateWelcomeLabel(username);

                menuFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(jFrame, response.body());
            }
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(jFrame, "Error occurred: " + exception.getMessage());

        }
    }
}
