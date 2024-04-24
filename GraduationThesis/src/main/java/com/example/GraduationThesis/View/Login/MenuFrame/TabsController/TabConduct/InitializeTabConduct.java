package com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabConduct;

import com.example.GraduationThesis.Model.Enitity.Users.Roles.ERole;
import com.example.GraduationThesis.Service.LazySingleton.ListRoles.ListRolesManager;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ActionType;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ButtonEditor;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class InitializeTabConduct extends JPanel {

    private JTable table;

    public InitializeTabConduct() {
        setLayout(new BorderLayout());

        // create table
        String[] columns = {"ID", "Student Name", "Conduct2017_2018", "Conduct2018_2019", "Conduct2019_2020", "Attendance_Score"};

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

        updateData();
    }

    public void updateData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Delete current data in the table

        // get data from api
        List<Map<String, Object>> data = TabConductAction.Action();

        // iterate over each object in the list and add it to the table
        data.forEach(row -> {
            // get data of student
            int studentID = (int) row.get("ID");
            String studentName = (String) row.get("Student Name");

            List<Map<String, Object>> conducts = (List<Map<String, Object>>) row.get("Conducts");
            conducts.stream().findFirst().ifPresent(conduct -> {
                String conduct2017_2018 = (String) conduct.get("Conduct2017_2018");
                String conduct2018_2019 = (String) conduct.get("Conduct2018_2019");
                String conduct2019_2020 = (String) conduct.get("Conduct2019_2020");
                String attendanceScore = (String) conduct.get("Attendance_Score");

                if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {
                    model.addRow(new Object[]{studentID, studentName, conduct2017_2018, conduct2018_2019, conduct2019_2020, attendanceScore, "Delete"});

                } else {
                    model.addRow(new Object[]{studentID, studentName, conduct2017_2018, conduct2018_2019, conduct2019_2020, attendanceScore});
                }
            });
        });
    }


    public void deleteRecord(int rowIndex) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setValueAt("", rowIndex, 2);
        model.setValueAt("", rowIndex, 3);
        model.setValueAt("", rowIndex, 4);
        model.setValueAt("", rowIndex, 5);
    }

    public JTable getTable() {
        return table;
    }
}
