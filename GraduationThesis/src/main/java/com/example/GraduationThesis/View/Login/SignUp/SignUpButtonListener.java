package com.example.GraduationThesis.View.Login.SignUp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpButtonListener extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;

    private JTextField numberphoneField;
    private JTextField emailField;
    private JFrame jFrame;


    // add new constructor that accepts authenticationManager and tokenProvider parameters
    public SignUpButtonListener(JFrame jFrame) {
        this.jFrame = jFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        jFrame.dispose();
        new SignUpFrame();

    }
}
