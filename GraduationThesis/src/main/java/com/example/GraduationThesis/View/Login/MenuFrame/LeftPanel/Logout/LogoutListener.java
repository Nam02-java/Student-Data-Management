package com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.Logout;

import com.example.GraduationThesis.Service.LazySingleton.Email.EmailManager;
import com.example.GraduationThesis.Service.LazySingleton.JsonWebToken.JsonWebTokenManager;
import com.example.GraduationThesis.Service.LazySingleton.ListRoles.ListRolesManager;
import com.example.GraduationThesis.Service.LazySingleton.NumberPhone.NumberPhoneManager;
import com.example.GraduationThesis.Service.LazySingleton.Password.PasswordManager;
import com.example.GraduationThesis.Service.LazySingleton.UserName.UserNameManager;
import com.example.GraduationThesis.View.Login.LoginFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogoutListener implements ActionListener {

    private JFrame jFrame;

    public LogoutListener(JFrame jFrame) {
        this.jFrame = jFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //delete all data of current user before logout
        JsonWebTokenManager.getInstance().delete();
        NumberPhoneManager.getInstance().delete();
        EmailManager.getInstance().delete();
        ListRolesManager.getInstance().delete();
        UserNameManager.getInstance().delete();
        PasswordManager.getInstance().delete();

        jFrame.dispose();
        new LoginFrame();
    }
}
