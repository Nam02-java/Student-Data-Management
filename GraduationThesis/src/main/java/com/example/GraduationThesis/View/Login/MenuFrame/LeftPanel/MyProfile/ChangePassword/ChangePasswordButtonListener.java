package com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.MyProfile.ChangePassword;

import com.example.GraduationThesis.View.Login.MenuFrame.MenuFrame;
import com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.MyProfile.MyProfileFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangePasswordButtonListener implements ActionListener {

    private MyProfileFrame myProfileFrame;

    private MenuFrame menuFrame;

    public ChangePasswordButtonListener(MyProfileFrame myProfileFrame, MenuFrame menuFrame) {
        this.myProfileFrame = myProfileFrame;
        this.menuFrame = menuFrame;
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        // hidden MyProfileFrame
        myProfileFrame.setVisible(false);

        // Create and display a new window to change the password
        ChangePasswordFrame changePasswordFrame = new ChangePasswordFrame();
        changePasswordFrame.setVisible(true);

        changePasswordFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                // Re-show MenuFrame when MyProfileFrame is closed
                menuFrame.setVisible(true);
            }
        });
    }
}


