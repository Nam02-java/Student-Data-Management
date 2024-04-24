package com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabPersonalInformation;

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

public class InitializeTabPersonalInformation extends JPanel {

    public JTable table;

    public InitializeTabPersonalInformation() {
        setLayout(new BorderLayout());

        // all columns in table
        String[] columns = {"ID", "Student Name", "Email", "Birth of Date", "Address", "Number Phone", "Parents Name", "Parents Number Phone"};

        // disable editing of edit and gpa columns
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return !(column == 0);
            }
        };

        table = new JTable(model);


        table.setEnabled(false);
        // Add a column for the "Delete" button
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

        // Add a panel to the JScrollPane to have scroll bars when needed
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);


        // Get data from the API and update the table
        updateData();
    }

    public void updateData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        // delete data in table
        model.setRowCount(0);

        List<Map<String, Object>> data = TabPersonnalInformationAction.Action();

        if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {

            data.forEach(row -> model.addRow(new Object[]{row.get("ID"), row.get("Student Name"), row.get("Email"),
                    row.get("Birth of Date"), row.get("Address"), row.get("Number Phone"), row.get("Parents Name"),
                    row.get("Parents Number Phone"), "Delete"}));
        } else {

            data.forEach(row -> model.addRow(new Object[]{row.get("ID"), row.get("Student Name"), row.get("Email"),
                    row.get("Birth of Date"), row.get("Address"), row.get("Number Phone"), row.get("Parents Name"),
                    row.get("Parents Number Phone")}));
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


}
