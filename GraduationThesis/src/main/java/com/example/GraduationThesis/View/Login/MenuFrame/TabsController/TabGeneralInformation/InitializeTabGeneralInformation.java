package com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabGeneralInformation;


import com.example.GraduationThesis.Model.Enitity.Users.Roles.ERole;
import com.example.GraduationThesis.Service.LazySingleton.ListRoles.ListRolesManager;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ActionType;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ButtonEditor;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

public class InitializeTabGeneralInformation extends JPanel {

    public JTable table;

    public InitializeTabGeneralInformation() {
        setLayout(new BorderLayout());

        // all columns in table
        String[] columns = {"ID", "Student Name", "Class Name", "Position", "Teacher Name", "Address", "Number Phone", "GPA"};

        // disable editing of edit and gpa columns
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return !(column == 0 || column == 7);
            }
        };

        table = new JTable(model);


        table.setEnabled(false);
        // Thêm cột cho nút "Delete"
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

        // Thêm bảng vào JScrollPane để có thanh cuộn khi cần thiết
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);


        // Lấy dữ liệu từ API và cập nhật bảng
        updateData();
    }

    public void updateData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        // delete data in table
        model.setRowCount(0);

        List<Map<String, Object>> data = TabGeneralInformationAction.Action();

        if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {

            data.forEach(row -> model.addRow(new Object[]{row.get("ID"), row.get("Student Name"), row.get("Class Name"),
                    row.get("Position"), row.get("Teacher Name"), row.get("Address"), row.get("Number Phone"),
                    row.get("GPA"), "Delete"}));
        } else {

            data.forEach(row -> model.addRow(new Object[]{row.get("ID"), row.get("Student Name"), row.get("Class Name"),
                    row.get("Position"), row.get("Teacher Name"), row.get("Address"), row.get("Number Phone"), row.get("GPA")}));
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
