package com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabPosition;

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

public class InitializeTabPosition extends JPanel {
    private JTable table;

    public InitializeTabPosition() {
        setLayout(new BorderLayout());

        // create table
        String[] columns = {"ID", "Student Name", "ClassName", "Position", "Teacher Name"};

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
        model.setRowCount(0); // delete current data in the table
        List<Map<String, Object>> data = TabPositionAction.Action();
        if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {
            data.forEach(row -> model.addRow(new Object[]{row.get("ID"), row.get("Student Name"), row.get("Class Name"), row.get("Position"), row.get("Teacher Name"), "Delete"}));
        } else {
            data.forEach(row -> model.addRow(new Object[]{row.get("ID"), row.get("Student Name"), row.get("Class Name"), row.get("Position"), row.get("Teacher Name")}));
        }
    }

    public void deleteRecord(int rowIndex) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.removeRow(rowIndex);
    }

    public JTable getTable() {
        return table;
    }
}
