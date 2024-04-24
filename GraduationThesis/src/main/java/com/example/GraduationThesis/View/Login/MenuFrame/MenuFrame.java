package com.example.GraduationThesis.View.Login.MenuFrame;


import com.example.GraduationThesis.Model.Enitity.Users.Roles.ERole;
import com.example.GraduationThesis.Service.LazySingleton.ListRoles.ListRolesManager;
import com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.LeftPanel;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabConduct.InitializeTabConduct;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabPersonalInformation.InitializeTabPersonalInformation;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabScores.InitializeTabScores;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabAction;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabGeneralInformation.InitializeTabGeneralInformation;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabPosition.InitializeTabPosition;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabUsersData.InitializeTabUserData;

import javax.swing.*;
import java.awt.*;

public class MenuFrame extends JFrame {


    public MenuFrame() {

        setTitle("Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // create JTabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();
        InitializeTabGeneralInformation tabGeneralInformation = new InitializeTabGeneralInformation();
        InitializeTabPosition tabPosition = new InitializeTabPosition();
        InitializeTabScores tabScores = new InitializeTabScores();
        InitializeTabConduct tabConduct = new InitializeTabConduct();
        InitializeTabPersonalInformation tabPersonalInformation = new InitializeTabPersonalInformation();

        // add tab general onformation
        tabbedPane.addTab("General Information", tabGeneralInformation);

        // add tab position
        tabbedPane.addTab("Position", tabPosition);

        //add tab scores;
        tabbedPane.addTab("Scores", tabScores);

        //add tab conduct;
        tabbedPane.addTab("Conduct", tabConduct);

        //add tab tab Personal Information;
        tabbedPane.addTab("Personal Information", tabPersonalInformation);


        /**
         * if current user has ROLE_ADMIN
         * they will get a special tab for control users data
         */
        if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {

            // add tab users data
            InitializeTabUserData tabUserData = new InitializeTabUserData(this);
            tabbedPane.addTab("Users Data", tabUserData);

            tabbedPane.addChangeListener(new TabAction(tabGeneralInformation, tabPosition, tabScores, tabConduct, tabPersonalInformation, tabUserData));

        } else {
            tabbedPane.addChangeListener(new TabAction(tabGeneralInformation, tabPosition, tabScores, tabConduct, tabPersonalInformation));
        }


        // select tab 1 at first show
        tabbedPane.setSelectedIndex(0);

        add(tabbedPane);

        add(new LeftPanel(this, tabbedPane, tabGeneralInformation, tabPosition, tabScores, tabConduct, tabPersonalInformation), BorderLayout.WEST);

        setVisible(true);
        requestFocusInWindow();
    }
}