package com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.AddNewStudent;

import com.example.GraduationThesis.View.Login.MenuFrame.MenuFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddNewStudentListener implements ActionListener {

    private MenuFrame menuFrame;

    public AddNewStudentListener(MenuFrame menuFrame) {
        this.menuFrame = menuFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // hidden MenuFrame
        menuFrame.setVisible(false);

        // open MyProfileFrame
        AddNewStudentFrame addNewStudentFrame = new AddNewStudentFrame(menuFrame);

        addNewStudentFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                // Re-show MenuFrame when MyProfileFrame is closed
                menuFrame.setVisible(true);
            }
        });
    }
}
