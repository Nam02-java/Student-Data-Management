package com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabGeneralInformation;


import com.example.GraduationThesis.Model.Enitity.Users.Roles.ERole;
import com.example.GraduationThesis.Service.LazySingleton.ListRoles.ListRolesManager;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ActionType;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ButtonEditor;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ButtonRenderer;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabPersonalInformation.TabPersonnalInformationAction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class InitializeTabGeneralInformation extends JPanel {

    public JTable table;
    private List<String> studentNames;
    public static Map<String, String> testing;
    private static final DecimalFormat df = new DecimalFormat("#.#");


    public InitializeTabGeneralInformation() {
        setLayout(new BorderLayout());

        // All columns in table
        String[] columns = {"ID", "Student Name", "Class Name", "Position", "Teacher Name", "Address", "Number Phone", "GPA"};

        // Disable editing of ID and GPA columns
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return !(column == 0 || column == 7);
            }
        };

        table = new JTable(model);
        table.setEnabled(false);

        // Add column for "Delete" button if user is admin
        if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {
            columns = Arrays.copyOf(columns, columns.length + 1);
            columns[columns.length - 1] = "Delete";
            model.addColumn("Delete");

            TableColumn deleteButtonColumn = table.getColumnModel().getColumn(model.getColumnCount() - 1);
            deleteButtonColumn.setCellRenderer(new ButtonRenderer());
            ActionType actionType = ActionType.DELETE_STUDENT;
            deleteButtonColumn.setCellEditor(new ButtonEditor(new JCheckBox(), this, actionType));
            table.setEnabled(true);
        }

        // Add table to JScrollPane for scrolling
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Add search panel if user is admin
        if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {
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

        updateData();
    }

    public void updateData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear previous data in table

        List<Map<String, Object>> data = TabGeneralInformationAction.Action();

        if (studentNames != null) {
            studentNames.clear(); // Clear previous list of student names
        }

        if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {
            data.forEach(row -> {
                model.addRow(new Object[]{row.get("ID"), row.get("Student Name"), row.get("Class Name"),
                        row.get("Position"), row.get("Teacher Name"), row.get("Address"), row.get("Number Phone"),
                        row.get("GPA"), "Delete"});
                studentNames.add((String) row.get("Student Name"));
            });
        } else {
            data.forEach(row -> model.addRow(new Object[]{row.get("ID"), row.get("Student Name"), row.get("Class Name"),
                    row.get("Position"), row.get("Teacher Name"), row.get("Address"), row.get("Number Phone"), row.get("GPA")}));
        }

        // Add data to testing map
        if (testing == null) {
            testing = new HashMap<>();
        }
        for (Map<String, Object> row : data) {
            String studentName = (String) row.get("Student Name");
            Double GPA = (Double) row.get("GPA"); // Ensure GPA is treated as a Double
            String formattedGpa = df.format(GPA); // Format GPA to one decimal place
            testing.put(studentName, formattedGpa);
        }
    }

    public void deleteRecord(int rowIndex) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.removeRow(rowIndex);
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    private void filterTableByStudentName(String studentName) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        List<Map<String, Object>> data = TabGeneralInformationAction.Action();

        for (Map<String, Object> row : data) {
            if (row.get("Student Name").equals(studentName)) {
                if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {
                    model.addRow(new Object[]{row.get("ID"), row.get("Student Name"), row.get("Class Name"),
                            row.get("Position"), row.get("Teacher Name"), row.get("Address"), row.get("Number Phone"),
                            row.get("GPA"), "Delete"});
                }
            }
        }
    }
}
