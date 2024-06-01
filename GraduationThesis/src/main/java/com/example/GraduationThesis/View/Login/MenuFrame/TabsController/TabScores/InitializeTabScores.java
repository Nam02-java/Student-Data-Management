package com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabScores;

import com.example.GraduationThesis.Model.Enitity.Users.Roles.ERole;
import com.example.GraduationThesis.Service.LazySingleton.ListRoles.ListRolesManager;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ActionType;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ButtonEditor;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ButtonRenderer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.ArrayList;



public class InitializeTabScores extends JPanel {

    private JTable table;
    private JTextField searchField;
    private List<String> studentNames;

    public InitializeTabScores() {
        setLayout(new BorderLayout());

        // create table
        String[] columns = {"ID", "Student Name", "Subject", "School Year", "15 minutes", "1 hour", "Mid term", "Final exam", "GPA"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only allow editing in School Year column for specified rows
                if (column == 3) {
                    return row % 13 == 0;
                }
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

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 25));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTableByQuery(searchField.getText().trim());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTableByQuery(searchField.getText().trim());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTableByQuery(searchField.getText().trim());
            }
        });

        searchPanel.add(searchField, BorderLayout.EAST);

        add(searchPanel, BorderLayout.NORTH);

        studentNames = new ArrayList<>();
        updateData();
    }

    public void updateData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // delete current data in the table

        List<Map<String, Object>> data = TabScoresAction.Action();
        studentNames.clear(); // Clear the previous list of student names
        AtomicInteger count = new AtomicInteger(0);
        if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {
            data.forEach(row -> {
                model.addRow(new Object[]{
                        row.get("ID"), row.get("Student Name"), row.get("Subject"),
                        row.get("School Year"),
                        row.get("15 minutes"), row.get("1 hour"), row.get("Mid term"), row.get("Final exam"), row.get("GPA"),
                        "Delete"});
                if (count.get() % 39 == 0) {
                    studentNames.add((String) row.get("Student Name"));
                }
                count.getAndIncrement();
            });
        } else {
            data.forEach(row -> {
                model.addRow(new Object[]{
                        row.get("ID"), row.get("Student Name"), row.get("Subject"),
                        row.get("School Year"),
                        row.get("15 minutes"), row.get("1 hour"), row.get("Mid term"), row.get("Final exam"), row.get("GPA")});
                studentNames.add((String) row.get("Student Name"));
            });
        }
        highlightSchoolYearColumn();
    }

    private void highlightSchoolYearColumn() {
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Color rows 0, 13, 26, 39, etc.
                if (row % 13 == 0) {
                    cell.setBackground(Color.RED);
                    cell.setForeground(Color.WHITE);
                } else {
                    // Reset to default colors if not the specified rows
                    cell.setBackground(table.getBackground());
                    cell.setForeground(table.getForeground());
                }

                return cell;
            }
        });
    }

    private void filterTableByQuery(String query) {
        query = query.toLowerCase(); // Convert query to lower case for case-insensitive search
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear all current rows
        List<Map<String, Object>> data = TabScoresAction.Action();
        for (Map<String, Object> row : data) {
            String studentName = ((String) row.get("Student Name")).toLowerCase(); // Convert student name to lower case
            if (studentName.contains(query)) { // Check if student name contains the query
                if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {
                    model.addRow(new Object[]{
                            row.get("ID"), row.get("Student Name"), row.get("Subject"),
                            row.get("School Year"),
                            row.get("15 minutes"), row.get("1 hour"), row.get("Mid term"), row.get("Final exam"), row.get("GPA"),
                            "Delete"});
                } else {
                    model.addRow(new Object[]{
                            row.get("ID"), row.get("Student Name"), row.get("Subject"),
                            row.get("School Year"),
                            row.get("15 minutes"), row.get("1 hour"), row.get("Mid term"), row.get("Final exam"), row.get("GPA")});
                }
            }
        }
    }

    public void deleteRecord(int rowIndex) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
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
