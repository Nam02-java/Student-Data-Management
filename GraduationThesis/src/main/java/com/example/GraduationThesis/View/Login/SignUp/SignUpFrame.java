package com.example.GraduationThesis.View.Login.SignUp;


import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SignUpFrame extends JFrame {

    public SignUpFrame() {

        setTitle("SignUp");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);


        JTextField usernameFieled = createTextFieldWithPlaceholderForUsername("Enter username");
        JPasswordField passwordField = createPasswordFieldWithPlaceholderForPassword("Enter Password");
        JTextField numberphoneField = createTextFieldWithPlaceholderForNumberphone("Enter Number Phone");
        JTextField emailField = createTextFieldWithPlaceholderForEmail("Enter Email");

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(135, 180, 100, 25);
        registerButton.addActionListener(new RegisterButtonListener(this, usernameFieled, passwordField, numberphoneField, emailField));
        add(registerButton);

        JButton returnButton = new JButton("Return");
        returnButton.setBounds(10, 230, 100, 25);
        returnButton.addActionListener(new ReturnButtonListener(this));
        add(returnButton);

        setLayout(null);
        requestFocusInWindow();
        setVisible(true);
    }

    private JTextField createTextFieldWithPlaceholderForUsername(String placeholder) {
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

        textField.setBounds(110, 20, 150, 25);
        add(textField);

        return textField;
    }

    private JPasswordField createPasswordFieldWithPlaceholderForPassword(String placeholder) {
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

        passwordField.setBounds(110, 60, 150, 25);
        add(passwordField);

        return passwordField;
    }


    private JTextField createTextFieldWithPlaceholderForNumberphone(String placeholder) {
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

        textField.setBounds(110, 100, 150, 25);
        add(textField);

        return textField;
    }

    private JTextField createTextFieldWithPlaceholderForEmail(String placeholder) {
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

        textField.setBounds(110, 140, 150, 25);
        add(textField);

        return textField;
    }
}
