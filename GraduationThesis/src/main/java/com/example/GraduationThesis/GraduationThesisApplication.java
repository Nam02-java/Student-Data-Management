package com.example.GraduationThesis;

import com.example.GraduationThesis.View.Login.LoginFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;

@SpringBootApplication
public class GraduationThesisApplication implements Runnable {


    @Override
    public void run() {
        LoginFrame loginFrame = new LoginFrame();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new GraduationThesisApplication());

        SpringApplication.run(GraduationThesisApplication.class, args);
    }
}
