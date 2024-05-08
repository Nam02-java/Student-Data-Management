package com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel;

import com.example.GraduationThesis.Model.Enitity.Users.Roles.ERole;
import com.example.GraduationThesis.Service.LazySingleton.ListRoles.ListRolesManager;
import com.example.GraduationThesis.Service.LazySingleton.UserName.UserNameManager;
import com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.MyProfile.MyProfileListener;
import com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.AddNewStudent.AddNewStudentListener;
import com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.SaveButton.SaveEditButtonListener;
import com.example.GraduationThesis.View.Login.MenuFrame.MenuFrame;
import com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.Logout.LogoutListener;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabConduct.InitializeTabConduct;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabGeneralInformation.InitializeTabGeneralInformation;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabPersonalInformation.InitializeTabPersonalInformation;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabPosition.InitializeTabPosition;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabScores.InitializeTabScores;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


public class LeftPanel extends JPanel {

    private MenuFrame menuFrame;

    private JTabbedPane tabbedPane;

    private static JLabel welcomeLabel;

    private InitializeTabGeneralInformation tabGeneralInformation;

    private InitializeTabPosition tabPosition;

    private InitializeTabScores tabScores;

    private InitializeTabConduct tabConduct;
    private InitializeTabPersonalInformation tabPersonalInformation;


    public LeftPanel(MenuFrame menuFrame, JTabbedPane tabbedPane, InitializeTabGeneralInformation tabGeneralInformation, InitializeTabPosition tabPosition, InitializeTabScores tabScores, InitializeTabConduct tabConduct, InitializeTabPersonalInformation tabPersonalInformation) {
        this.menuFrame = menuFrame;
        this.tabbedPane = tabbedPane;
        this.tabGeneralInformation = tabGeneralInformation;
        this.tabPosition = tabPosition;
        this.tabScores = tabScores;
        this.tabConduct = tabConduct;
        this.tabPersonalInformation = tabPersonalInformation;

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10)); // Thêm khoảng trống xung quanh

        welcomeLabel = new JLabel("Welcome, " + UserNameManager.getInstance().getUsername());
        JButton logoutButton = new JButton("Logout");
        JButton myProfileButton = new JButton("My Profile");
        JButton addStudentButton = new JButton("Add New Student");
        JButton saveButton = new JButton("Save");

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1;
        buttonPanel.add(Box.createVerticalGlue(), gbc);

        gbc.gridy++;
        buttonPanel.add(myProfileButton, gbc);

        System.out.println(ListRolesManager.getInstance().getRoles());
        if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {

            gbc.gridy++;
            buttonPanel.add(saveButton, gbc);

            gbc.gridy++;
            buttonPanel.add(addStudentButton, gbc);

        }

        gbc.gridy++;
        buttonPanel.add(logoutButton, gbc);

        add(welcomeLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JSeparator(SwingConstants.VERTICAL), BorderLayout.EAST); // Đường kẻ dọc

        logoutButton.addActionListener(new LogoutListener(menuFrame));
        myProfileButton.addActionListener(new MyProfileListener(menuFrame));
        addStudentButton.addActionListener(new AddNewStudentListener(menuFrame));
        saveButton.addActionListener(new SaveEditButtonListener(tabbedPane, tabGeneralInformation.getTable(), tabPosition.getTable(), tabScores.getTable(), tabConduct.getTable(), tabPersonalInformation.getTable(), menuFrame));
    }


    public static void updateWelcomeLabel(String username) {
        welcomeLabel.setText("Welcome, " + username);
    }
}