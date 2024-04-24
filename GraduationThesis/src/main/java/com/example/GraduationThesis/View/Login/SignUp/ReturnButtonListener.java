package com.example.GraduationThesis.View.Login.SignUp;

import com.example.GraduationThesis.View.Login.LoginFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReturnButtonListener implements ActionListener {

    private JFrame jFrame;

    public ReturnButtonListener(JFrame jFrame) {
        this.jFrame = jFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        jFrame.dispose();
        new LoginFrame();
    }
}
