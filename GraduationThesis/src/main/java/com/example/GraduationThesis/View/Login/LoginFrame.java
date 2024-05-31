package com.example.GraduationThesis.View.Login;

import com.example.GraduationThesis.View.Login.SignUp.SignUpButtonListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JTextField textField1 = createTextFieldWithPlaceholder("Enter username");
        JPasswordField passwordField = createPasswordFieldWithPlaceholder("Enter password");


        JButton loginButton = new JButton("Login");
        loginButton.setBounds(135, 150, 100, 25);
        loginButton.addActionListener(new LoginButtonListener(textField1, passwordField, this));
        add(loginButton);

        JButton signUpButton = new JButton("SignUp");
        signUpButton.setBounds(10, 225, 120, 25);
        signUpButton.addActionListener(new SignUpButtonListener(this));
        add(signUpButton);

        setLayout(null);
        requestFocusInWindow();
        setVisible(true);
    }


    // function to create JTextField with placeholder text
    private JTextField createTextFieldWithPlaceholder(String placeholder) {
        JTextField textField = new JTextField();
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });

        textField.setBounds(110, 50, 150, 25);
        add(textField);

        return textField;
    }

    // JPasswordField method with placeholder text
    private JPasswordField createPasswordFieldWithPlaceholder(String placeholder) {
        JPasswordField passwordField = new JPasswordField() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(Color.GRAY);
                    g2.drawString(placeholder, 5, 15);
                    g2.dispose();
                }
            }
        };

        passwordField.setBounds(110, 100, 150, 25);
        add(passwordField);

        return passwordField;
    }
}


