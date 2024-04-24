package com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.MyProfile.Edit;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditButtonListener implements ActionListener {

    private JTextField usernameField;
    private JTextField emailField;
    private JTextField numberPhoneField;

    private JButton button;

    public EditButtonListener(JTextField usernameField, JTextField emailField, JTextField numberPhoneField, JButton button) {
        this.usernameField = usernameField;
        this.emailField = emailField;
        this.numberPhoneField = numberPhoneField;
        this.button = button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        emailField.setEditable(true);
        numberPhoneField.setEditable(true);
        usernameField.setEditable(true);
        button.setEnabled(true);
    }
}
