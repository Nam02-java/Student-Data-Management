package com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabScores;

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

public class InitializeTabScores extends JPanel {

    private JTable table;

    public InitializeTabScores() {
        setLayout(new BorderLayout());

        // create table
        String[] columns = {"ID", "Student Name", "Subject", "School Year", "15 minutes", "1 hour", "Mid term", "Final exam", "GPA"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {

                /**
                 * Disable editing
                 * Column ID score, student name, GPA are three tables that cannot be edited in the scores tab
                 */
                return !(column == 0 || column == 1 || column == 2 || column == 8);
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
        model.setRowCount(0); // delete current data in the table

        List<Map<String, Object>> data = TabScoresAction.Action();
        if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {
            data.forEach(row -> model.addRow(new Object[]{
                    row.get("ID"), row.get("Student Name"), row.get("Subject"),
                    row.get("School Year"),
                    row.get("15 minutes"), row.get("1 hour"), row.get("Mid term"), row.get("Final exam"), row.get("GPA"),
                    "Delete"}));
        } else {
            data.forEach(row -> model.addRow(new Object[]{
                    row.get("ID"), row.get("Student Name"), row.get("Subject"),
                    row.get("School Year"),
                    row.get("15 minutes"), row.get("1 hour"), row.get("Mid term"), row.get("Final exam"), row.get("GPA")}));
        }
    }

    public void deleteRecord(int rowIndex) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setValueAt("", rowIndex, 3); // School Year column
        model.setValueAt("", rowIndex, 4); // 15 minutes column
        model.setValueAt("", rowIndex, 5); // 1 hour column
        model.setValueAt("", rowIndex, 6); // Mid term column
        model.setValueAt("", rowIndex, 7); // Final exam column
        model.setValueAt("0.0", rowIndex, 8); // GPA column
    }

    public JTable getTable() {
        return table;
    }
}
