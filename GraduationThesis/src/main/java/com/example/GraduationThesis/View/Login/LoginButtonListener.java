package com.example.GraduationThesis.View.Login;

import com.example.GraduationThesis.Service.LazySingleton.Email.EmailManager;
import com.example.GraduationThesis.Service.LazySingleton.JsonWebToken.JsonWebTokenManager;
import com.example.GraduationThesis.Service.LazySingleton.ListRoles.ListRolesManager;
import com.example.GraduationThesis.Service.LazySingleton.NumberPhone.NumberPhoneManager;
import com.example.GraduationThesis.Service.LazySingleton.UserName.UserNameManager;
import com.example.GraduationThesis.View.Login.MenuFrame.MenuFrame;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class LoginButtonListener implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JFrame jFrame;


    // add a new constructor that accepts authenticationManager and tokenProvider parameters
    public LoginButtonListener(JTextField usernameField, JPasswordField passwordField, JFrame jFrame) {
        this.usernameField = usernameField;
        this.passwordField = passwordField;
        this.jFrame = jFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if ("Enter username".equals(username)) {
            JOptionPane.showMessageDialog(jFrame, "username can not be null");
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(jFrame, "password can not be null");
            return;
        }

        String requestBody;
        requestBody = new Gson().toJson(
                Map.of("username", username, "password", password)
        );

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/v1/public/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse JSON response
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);

            System.out.println(jsonObject.get("accessToken"));
            if (jsonObject.get("accessToken").isJsonNull()) {
                displayErrorMessages(jsonObject);
                return;
            }


            // get value of accessToken
            String accessToken = jsonObject.get("accessToken").getAsString();
            JsonWebTokenManager.getInstance().setJwtToken(accessToken);

            String userName = jsonObject.get("userName").getAsString();
            UserNameManager.getInstance().setUsername(userName);

            String numberPhone = jsonObject.get("numberPhone").getAsString();
            NumberPhoneManager.getInstance().setNumberphone(numberPhone);

            String email = jsonObject.get("email").getAsString();
            EmailManager.getInstance().setEmail(email);


            // get the value of listRole from jsonObject
            JsonElement listRoleElement = jsonObject.get("listRole");

            // checks if listRoleElement is a JsonPrimitive and has a string value
            if (listRoleElement instanceof JsonPrimitive && ((JsonPrimitive) listRoleElement).isString()) {
                // convert JSON string to a JSON array and convert to Java list
                List<String> roles = new Gson().fromJson(listRoleElement.getAsString(), new TypeToken<List<String>>() {
                }.getType());

                // save the list of roles to ListRolesManager
                ListRolesManager.getInstance().setRoles(roles);
            }


        } catch (Exception exception) {
            JOptionPane.showMessageDialog(jFrame, "Error occurred: " + exception.getMessage());
        }
        jFrame.dispose();
        new MenuFrame();
    }

    private void displayErrorMessages(JsonObject responseBody) {
        StringBuilder errorMessage = new StringBuilder();

        // Check and append error messages for the "userName" field
        if (responseBody.has("status")) {
            errorMessage.append("status: ").append(responseBody.get("status").getAsString()).append("\n");
            // Display error message and return if error found for "status" field
            JOptionPane.showMessageDialog(jFrame, errorMessage.toString());
        }
    }
}