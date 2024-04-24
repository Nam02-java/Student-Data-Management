package com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.MyProfile;

import com.example.GraduationThesis.Model.Enitity.Users.Roles.ERole;
import com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.MyProfile.Edit.EditButtonListener;
import com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.MyProfile.ChangePassword.ChangePasswordButtonListener;
import com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.MyProfile.Save.SaveButtonListener;
import com.example.GraduationThesis.View.Login.MenuFrame.MenuFrame;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MyProfileFrame extends JFrame {

    private JTextField usernameField;
    private JTextField emailField;
    private JTextField numberPhoneField;

    private MenuFrame menuFrame;

    public MyProfileFrame(MenuFrame menuFrame, String email, String numberPhone, String username, List<String> roles) {
        this.menuFrame = menuFrame;
        setTitle("My Profile");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close this window without affecting the MenuFrame
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(email, 15);
        emailField.setEditable(false); // No editing allowed

        JLabel numberPhoneLabel = new JLabel("Number Phone:");
        numberPhoneField = new JTextField(numberPhone, 15);
        numberPhoneField.setEditable(false); // No editing allowed

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(username, 15);
        usernameField.setEditable(false); // No editing allowed

        JLabel rolesLabel = new JLabel("Roles:");
        String role = "User";

        // Check if the role list contains ROLE_ADMIN
        if (roles.contains(ERole.ROLE_ADMIN.toString())) {
            role += " && Admin";
        }

        JTextField rolesField = new JTextField(role, 15);
        rolesField.setEditable(false); // Không cho phép chỉnh sửa

        // Create an Edit button and a Save button
        JButton editButton = new JButton("Edit");
        JButton saveButton = new JButton("Save");

        saveButton.setEnabled(false);

        // Create a JPanel to contain the Edit button and Save button
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(editButton, BorderLayout.WEST);
        buttonPanel.add(Box.createHorizontalStrut(10)); // The distance between the Edit button and the Save button
        buttonPanel.add(saveButton, BorderLayout.EAST);

        addComponent(panel, emailLabel, gbc);
        gbc.gridx++;
        addComponent(panel, emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        addComponent(panel, numberPhoneLabel, gbc);
        gbc.gridx++;
        addComponent(panel, numberPhoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        addComponent(panel, usernameLabel, gbc);
        gbc.gridx++;
        addComponent(panel, usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        addComponent(panel, rolesLabel, gbc);
        gbc.gridx++;
        addComponent(panel, rolesField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addComponent(panel, buttonPanel, gbc);

        JButton changePasswordButton = new JButton("Change Password");

        JPanel changePasswordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        changePasswordPanel.add(changePasswordButton);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        addComponent(panel, changePasswordPanel, gbc);

        editButton.addActionListener(new EditButtonListener(usernameField, emailField, numberPhoneField, saveButton));
        saveButton.addActionListener(new SaveButtonListener(menuFrame, this, usernameField, emailField, numberPhoneField));

        changePasswordButton.addActionListener(new ChangePasswordButtonListener(this, menuFrame));

        add(panel);

        requestFocusInWindow();
        setVisible(true);
    }

    // Method to add component to JPanel with GridBagLayout constraints
    private void addComponent(JPanel panel, Component component, GridBagConstraints gbc) {
        panel.add(component, gbc);
    }
}
