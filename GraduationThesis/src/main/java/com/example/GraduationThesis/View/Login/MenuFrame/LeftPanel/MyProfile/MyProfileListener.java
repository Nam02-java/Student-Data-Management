package com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.MyProfile;


import com.example.GraduationThesis.Service.LazySingleton.Email.EmailManager;
import com.example.GraduationThesis.Service.LazySingleton.ListRoles.ListRolesManager;
import com.example.GraduationThesis.Service.LazySingleton.NumberPhone.NumberPhoneManager;
import com.example.GraduationThesis.Service.LazySingleton.UserName.UserNameManager;
import com.example.GraduationThesis.View.Login.MenuFrame.MenuFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyProfileListener implements ActionListener {

    private MenuFrame menuFrame;

    public MyProfileListener(MenuFrame menuFrame) {
        this.menuFrame = menuFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // hidden MenuFrame
        menuFrame.setVisible(false);

        // open MyProfileFrame
        MyProfileFrame myProfileFrame = new MyProfileFrame(menuFrame, EmailManager.getInstance().getEmail(),
                NumberPhoneManager.getInstance().getNumberphone(),
                UserNameManager.getInstance().getUsername(),
                ListRolesManager.getInstance().getRoles());

        myProfileFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                // Re-show MenuFrame when MyProfileFrame is closed
                menuFrame.setVisible(true);
            }
        });
    }
}