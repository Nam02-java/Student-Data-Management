package com.example.GraduationThesis.View.Login.MenuFrame.TabsController;

import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabConduct.InitializeTabConduct;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabPersonalInformation.InitializeTabPersonalInformation;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabScores.InitializeTabScores;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabUsersData.InitializeTabUserData;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabGeneralInformation.InitializeTabGeneralInformation;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabPosition.InitializeTabPosition;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TabAction implements ChangeListener {
    private InitializeTabGeneralInformation tabGeneralInformation;
    private InitializeTabPosition tabPosition;

    private InitializeTabScores tabScores;

    private InitializeTabConduct tabConduct;

    private InitializeTabPersonalInformation tabPersonalInformation;

    private InitializeTabUserData tabUserData;


    public TabAction(InitializeTabGeneralInformation tabGeneralInformation, InitializeTabPosition tabPosition, InitializeTabScores tabScores, InitializeTabConduct tabConduct, InitializeTabPersonalInformation tabPersonalInformation, InitializeTabUserData tabUserData) {
        this.tabGeneralInformation = tabGeneralInformation;
        this.tabPosition = tabPosition;
        this.tabScores = tabScores;
        this.tabConduct = tabConduct;
        this.tabPersonalInformation = tabPersonalInformation;
        this.tabUserData = tabUserData;
    }

    public TabAction(InitializeTabGeneralInformation tabGeneralInformation, InitializeTabPosition tabPosition, InitializeTabScores tabScores, InitializeTabConduct tabConduct, InitializeTabPersonalInformation tabPersonalInformation) {
        this.tabGeneralInformation = tabGeneralInformation;
        this.tabPosition = tabPosition;
        this.tabScores = tabScores;
        this.tabConduct = tabConduct;
        this.tabPersonalInformation = tabPersonalInformation;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
        int selectedIndex = sourceTabbedPane.getSelectedIndex();
        if (selectedIndex == 0) {
            tabGeneralInformation.updateData();
        } else if (selectedIndex == 1) {
            tabPosition.updateData();
        } else if (selectedIndex == 2) {
            tabScores.updateData();
        } else if (selectedIndex == 3) {
            tabConduct.updateData();
        } else if (selectedIndex == 4) {
            tabPersonalInformation.updateData();
        } else if (selectedIndex == 5) {
            tabUserData.updateData();

        }
    }
}
