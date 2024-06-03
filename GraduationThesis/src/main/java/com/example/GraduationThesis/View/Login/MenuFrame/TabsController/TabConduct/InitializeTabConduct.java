package com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabConduct;

import com.example.GraduationThesis.Model.Enitity.Users.Roles.ERole;
import com.example.GraduationThesis.Service.LazySingleton.ListRoles.ListRolesManager;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ActionType;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ButtonEditor;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ButtonRenderer;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabPersonalInformation.TabPersonnalInformationAction;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabScores.TabScoresAction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class InitializeTabConduct extends JPanel {

    private JTable table;
    private List<String> studentNames;

    public InitializeTabConduct() {
        setLayout(new BorderLayout());

        // create table
        String[] columns = {"ID", "Student Name", "School Year", "Conduct", "Attendance_Score"};

        // disable editing of edit and gpa columns
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return !(column == 0 || column == 1);
            }
        };

        table = new JTable(model);

        table.setEnabled(false);

        if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {

            columns = Arrays.copyOf(columns, columns.length + 1);
            columns[columns.length - 1] = "Delete";

            model.addColumn("Delete");

            TableColumn deleteButtonColumn = table.getColumnModel().getColumn(model.getColumnCount() - 1);
            deleteButtonColumn.setCellRenderer(new ButtonRenderer());

            /**
             * Enum type
             * this tab is for student so we need delete student
             * set DELETE_STUDENT like a flag
             */
            ActionType actionType = ActionType.DELETE_STUDENT;

            deleteButtonColumn.setCellEditor(new ButtonEditor(new JCheckBox(), this, actionType));

            table.setEnabled(true);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {

            // Search tool
            JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JComboBox<String> searchBox = new JComboBox<>();
            searchBox.setEditable(true);
            searchBox.setBounds(100, 20, 165, 25);
            JButton searchButton = new JButton("Search By Name");

            searchPanel.add(searchBox);
            searchPanel.add(searchButton);

            add(searchPanel, BorderLayout.NORTH);

            studentNames = new ArrayList<>();
            updateData();

            JTextField searchText = (JTextField) searchBox.getEditor().getEditorComponent();
            searchText.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    String input = searchText.getText();
                    searchBox.removeAllItems();
                    if (!input.isEmpty()) {
                        for (String suggestion : studentNames) {
                            if (suggestion.toLowerCase().startsWith(input.toLowerCase())) {
                                searchBox.addItem(suggestion);
                            }
                        }
                        searchText.setText(input);
                        searchBox.setPopupVisible(true);
                    }
                }
            });

            // Add action listener to search button
            searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String query = searchText.getText().trim();
                    if (!query.isEmpty()) {
                        filterTableByStudentName(query);
                    } else {
                        updateData();
                    }
                }
            });
        }
    }

    public void updateData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Delete current data in the table

        // get data from api
        List<Map<String, Object>> data = TabConductAction.Action();


        if (studentNames != null) {
            studentNames.clear(); // Clear the previous list of student names
        }

        AtomicInteger count = new AtomicInteger(0);
        if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {

            // iterate over each object in the list and add it to the table
            data.forEach(row -> {

                int studentID = (int) row.get("ID");
                String studentName = (String) row.get("Student Name");

                List<Map<String, Object>> conducts = (List<Map<String, Object>>) row.get("Conducts");

                conducts.forEach(conduct -> {

                    String schoolYear = (String) conduct.get("School_Year");
                    String conductToString = (String) conduct.get("Conduct");
                    String attendanceScore = (String) conduct.get("Attendance_Score");

                    if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {
                        model.addRow(new Object[]{studentID, studentName, schoolYear, conductToString, attendanceScore, "Delete"});
                        if (studentNames != null) {
                            if (count.get() % 3 == 0) {
                                studentNames.add((String) row.get("Student Name"));
                            }
                        }
                        count.getAndIncrement();
                    } else {
                        model.addRow(new Object[]{studentID, studentName, schoolYear, conductToString, attendanceScore});
                    }
                });
            });

        } else {

            // iterate over each object in the list and add it to the table
            data.forEach(row -> {

                int studentID = (int) row.get("ID");
                String studentName = (String) row.get("Student Name");

                List<Map<String, Object>> conducts = (List<Map<String, Object>>) row.get("Conducts");

                conducts.forEach(conduct -> {

                    String schoolYear = (String) conduct.get("School_Year");
                    String conductToString = (String) conduct.get("Conduct");
                    String attendanceScore = (String) conduct.get("Attendance_Score");

                    model.addRow(new Object[]{studentID, studentName, schoolYear, conductToString, attendanceScore});
                });
            });
        }
    }


    public void deleteRecord(int rowIndex) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setValueAt("", rowIndex, 2);
        model.setValueAt("", rowIndex, 3);
        model.setValueAt("", rowIndex, 4);
    }

    public JTable getTable() {
        return table;
    }

    private void filterTableByStudentName(String studentName) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear current table data

        List<Map<String, Object>> data = TabConductAction.Action();

        for (Map<String, Object> row : data) {
            if (row.get("Student Name").equals(studentName)) {
                List<Map<String, Object>> conducts = (List<Map<String, Object>>) row.get("Conducts");

                int yearCount = 0; // Counter for the number of years added

                for (Map<String, Object> conduct : conducts) {
                    if (yearCount >= 3) {
                        break; // Stop after adding three years of data
                    }

                    String schoolYear = (String) conduct.get("School_Year");
                    String conductToString = (String) conduct.get("Conduct");
                    String attendanceScore = (String) conduct.get("Attendance_Score");

                    if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {
                        model.addRow(new Object[]{
                                row.get("ID"),
                                studentName,
                                schoolYear,
                                conductToString,
                                attendanceScore,
                                "Delete"
                        });
                    } else {
                        model.addRow(new Object[]{
                                row.get("ID"),
                                studentName,
                                schoolYear,
                                conductToString,
                                attendanceScore
                        });
                    }

                    yearCount++;
                }
            }
        }
    }
}