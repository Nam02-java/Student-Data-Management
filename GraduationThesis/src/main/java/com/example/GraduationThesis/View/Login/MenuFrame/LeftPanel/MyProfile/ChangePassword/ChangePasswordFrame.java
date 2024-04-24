package com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.MyProfile.ChangePassword;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangePasswordFrame extends JFrame {

    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;


    public ChangePasswordFrame() {
        setTitle("Change Password");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel currentPasswordLabel = new JLabel("Current Password:");
        currentPasswordField = new JPasswordField(15);

        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordField = new JPasswordField(15);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordField = new JPasswordField(15);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");


        saveButton.addActionListener(new SaveButtonListener(this, currentPasswordField, newPasswordField, confirmPasswordField));

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        addComponent(panel, currentPasswordLabel, gbc);
        gbc.gridx++;
        addComponent(panel, currentPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        addComponent(panel, newPasswordLabel, gbc);
        gbc.gridx++;
        addComponent(panel, newPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        addComponent(panel, confirmPasswordLabel, gbc);
        gbc.gridx++;
        addComponent(panel, confirmPasswordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addComponent(panel, buttonPanel, gbc);

        add(panel);

        setVisible(true);
    }

    private void addComponent(JPanel panel, Component component, GridBagConstraints gbc) {
        panel.add(component, gbc);
    }
}
