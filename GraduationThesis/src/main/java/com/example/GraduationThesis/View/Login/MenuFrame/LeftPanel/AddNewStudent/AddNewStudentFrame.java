package com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.AddNewStudent;


import com.example.GraduationThesis.View.Login.MenuFrame.LeftPanel.AddNewStudent.Button.SubmitButtonListener;
import com.example.GraduationThesis.View.Login.MenuFrame.MenuFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class AddNewStudentFrame extends JFrame {
    private JTextField usernameField;
    private JTextField classnameField;
    private JTextField emailField;
    private JTextField dateOfBirthField;
    private JTextField numberphoneField;
    private JTextField addressField;
    private JTextField positionField;
    private JTextField teachernameField;
    private JTextField partentsnameField;
    private JTextField partensnumberphoneField;
    private ArrayList<JTextField> schoolYearList;
    private JTextField schoolYearField;
    private ArrayList<JTextField[]> scoreFields1;
    private ArrayList<JTextField[]> scoreFields2;
    private ArrayList<JTextField[]> scoreFields3;
    private ArrayList<JTextField[]> conductFieldsList;
    private MenuFrame menuFrame;

    public AddNewStudentFrame(MenuFrame menuFrame) {
        this.menuFrame = menuFrame;

        // Create labels and text fields for personal information
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        JLabel classnameLabel = new JLabel("Classname:");
        classnameField = new JTextField(20);
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);
        JLabel dateOfBirthLabel = new JLabel("Date of Birth:");
        dateOfBirthField = new JTextField(20);
        JLabel numberphoneLabel = new JLabel("Numberphone:");
        numberphoneField = new JTextField(20);
        JLabel addressLabel = new JLabel("Address:");
        addressField = new JTextField(20);
        JLabel positionLabel = new JLabel("Position:");
        positionField = new JTextField(20);
        JLabel teachernameLabel = new JLabel("Teacher Name:");
        teachernameField = new JTextField(20);
        JLabel partentsnameLabel = new JLabel("Parent's Name:");
        partentsnameField = new JTextField(20);
        JLabel partensnumberphoneLabel = new JLabel("Parent's Numberphone:");
        partensnumberphoneField = new JTextField(20);

        // Create a panel for personal information
        JPanel personalInfoPanel = new JPanel(new GridLayout(12, 2));
        personalInfoPanel.add(usernameLabel);
        personalInfoPanel.add(usernameField);
        personalInfoPanel.add(classnameLabel);
        personalInfoPanel.add(classnameField);
        personalInfoPanel.add(emailLabel);
        personalInfoPanel.add(emailField);
        personalInfoPanel.add(dateOfBirthLabel);
        personalInfoPanel.add(dateOfBirthField);
        personalInfoPanel.add(numberphoneLabel);
        personalInfoPanel.add(numberphoneField);
        personalInfoPanel.add(addressLabel);
        personalInfoPanel.add(addressField);
        personalInfoPanel.add(positionLabel);
        personalInfoPanel.add(positionField);
        personalInfoPanel.add(teachernameLabel);
        personalInfoPanel.add(teachernameField);
        personalInfoPanel.add(partentsnameLabel);
        personalInfoPanel.add(partentsnameField);
        personalInfoPanel.add(partensnumberphoneLabel);
        personalInfoPanel.add(partensnumberphoneField);

        // Create panels for scores
        schoolYearList = new ArrayList<>();
        JPanel firstYearScoresPanel = createScoresPanel("First Year", scoreFields1);
        JPanel secondYearScoresPanel = createScoresPanel("Second Year", scoreFields2);
        JPanel thirdYearScoresPanel = createScoresPanel("Third Year", scoreFields3);

        // Create a panel for conduct
        JPanel conductPanel = new JPanel(new GridLayout(4, 4));

        // Add labels for each column
        conductPanel.add(new JLabel()); // Empty label for the corner
        JLabel schoolYearLabel = new JLabel("School Year:");
        JLabel conductLabel = new JLabel("Conduct:");
        JLabel attendanceScoreLabel = new JLabel("Attendance Score:");

        conductPanel.add(schoolYearLabel);
        conductPanel.add(conductLabel);
        conductPanel.add(attendanceScoreLabel);

        // Add the row labels and text fields
        conductFieldsList = new ArrayList<>();

        String[] yearLabels = {"First", "Second", "Third"};
        for (String yearLabel : yearLabels) {
            JLabel year = new JLabel(yearLabel + " School Year:");
            JTextField schoolYearTextField = new JTextField(20);
            JTextField conductTextField = new JTextField(20);
            JTextField attendanceScoreTextField = new JTextField(20);

            JTextField[] yearFields = {schoolYearTextField, conductTextField, attendanceScoreTextField};
            conductFieldsList.add(yearFields);

            conductPanel.add(year);
            conductPanel.add(schoolYearTextField);
            conductPanel.add(conductTextField);
            conductPanel.add(attendanceScoreTextField);
        }

        // Create a submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new SubmitButtonListener(menuFrame, this, usernameField, classnameField, emailField, dateOfBirthField, numberphoneField, addressField, positionField, teachernameField, partentsnameField, partensnumberphoneField, schoolYearField, schoolYearList, scoreFields1, scoreFields2, scoreFields3, conductFieldsList));

        // Create the main panel and add scroll pane
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(personalInfoPanel);
        mainPanel.add(firstYearScoresPanel);
        mainPanel.add(Box.createVerticalStrut(20)); // Add spacing
        mainPanel.add(secondYearScoresPanel);
        mainPanel.add(Box.createVerticalStrut(20)); // Add spacing
        mainPanel.add(thirdYearScoresPanel);
        mainPanel.add(Box.createVerticalStrut(20)); // Add spacing
        mainPanel.add(conductPanel);
        mainPanel.add(submitButton);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Add the main panel to the frame
        add(scrollPane);

        setTitle("Student Information Form");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close this window without affecting the MenuFrame
        pack();
        requestFocusInWindow();
        setVisible(true);
    }

    private JPanel createScoresPanel(String year, ArrayList<JTextField[]> scoreFields) {
        JPanel scoresPanel = new JPanel();
        scoresPanel.setLayout(new BoxLayout(scoresPanel, BoxLayout.Y_AXIS));

        JLabel yearLabel = new JLabel(year + " Scores");
        scoresPanel.add(yearLabel);

        JPanel scoresGrid = new JPanel(new GridLayout(15, 5)); // Adjusted to 15 rows

        // Add school year input at the top
        JLabel schoolYearLabel = new JLabel("School Year:");
        schoolYearField = new JTextField(10);
        schoolYearList.add(schoolYearField);

        scoresGrid.add(schoolYearLabel);
        scoresGrid.add(schoolYearField);
        for (int i = 0; i < 3; i++) {
            scoresGrid.add(new JLabel()); // Add empty labels for alignment
        }

        String[] columnHeaders = {"Subject", "15 minutes", "1 hour", "Midterm", "Final term"};
        for (String columnHeader : columnHeaders) {
            scoresGrid.add(new JLabel(columnHeader));
        }

        // Add scoreFields to the appropriate list based on the year
        if (year.equals("First Year")) {
            scoreFields1 = new ArrayList<>();
        } else if (year.equals("Second Year")) {
            scoreFields2 = new ArrayList<>();
        } else if (year.equals("Third Year")) {
            scoreFields3 = new ArrayList<>();
        }

        String[] subjects = {"Literature", "Math", "English", "History", "Geography", "Physics", "Chemistry", "Biology", "Citizen Education", "National Defense And Security Education", "Technology", "Information Technology", "Physical Education"};

        for (String subject : subjects) {
            scoresGrid.add(new JLabel(subject));

            JTextField[] fields = new JTextField[4];
            for (int i = 0; i < 4; i++) {
                fields[i] = new JTextField(3);
                scoresGrid.add(fields[i]);
            }

            // Add fields to the appropriate scoreFields list
            if (year.equals("First Year")) {
                scoreFields1.add(fields);
            } else if (year.equals("Second Year")) {
                scoreFields2.add(fields);
            } else if (year.equals("Third Year")) {
                scoreFields3.add(fields);
            }
        }

        scoresPanel.add(scoresGrid);
        return scoresPanel;
    }
}

