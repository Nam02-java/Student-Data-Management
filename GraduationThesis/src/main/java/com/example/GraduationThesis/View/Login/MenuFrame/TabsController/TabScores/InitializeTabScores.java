package com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabScores;

import com.example.GraduationThesis.Model.Enitity.Users.Roles.ERole;
import com.example.GraduationThesis.Service.LazySingleton.ListRoles.ListRolesManager;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ActionType;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ButtonEditor;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ButtonRenderer;

import javax.swing.*;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.ArrayList;

public class InitializeTabScores extends JPanel {

    private JTable table;
    private List<String> studentNames;

    public InitializeTabScores() {
        setLayout(new BorderLayout());

        // create table
        String[] columns = {"ID", "Student Name", "Subject", "School Year", "15 minutes", "1 hour", "Mid term", "Final exam", "GPA"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                Object subject = getValueAt(row, 2); // Get the value of "Subject" column
                // Only allow editing in School Year column for specified rows, ignore separator rows
                if (column == 3 && row % 40 != 39) {
                    // Allow editing for rows where Subject is "Literature" or it's the first row of each group
                    return (row % 13 == 0) || (subject != null && subject.equals("Literature"));
                }

                // Disable editing for other columns and separator rows
                return !(column == 0 || column == 1 || column == 2 || column == 8) && row % 40 != 39;
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
        model.setRowCount(0); // delete current data in the table

        List<Map<String, Object>> data = TabScoresAction.Action();

        if (studentNames != null) {
            studentNames.clear(); // Clear the previous list of student names
        }

        AtomicInteger count = new AtomicInteger(0);
        if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {
            data.forEach(row -> {
                model.addRow(new Object[]{
                        row.get("ID"), row.get("Student Name"), row.get("Subject"),
                        row.get("School Year"),
                        row.get("15 minutes"), row.get("1 hour"), row.get("Mid term"), row.get("Final exam"), row.get("GPA"),
                        "Delete"});
                if (studentNames != null) {
                    if (count.get() % 39 == 0) {
                        studentNames.add((String) row.get("Student Name"));
                    }
                }
                count.getAndIncrement();

                // Add a separating row after every 39 rows
                if (count.get() % 39 == 0) {
                    model.addRow(new Object[]{"", "", "", "", "", "", "", "", "", ""});
                }
            });
        } else {
            data.forEach(row -> {
                model.addRow(new Object[]{
                        row.get("ID"), row.get("Student Name"), row.get("Subject"),
                        row.get("School Year"),
                        row.get("15 minutes"), row.get("1 hour"), row.get("Mid term"), row.get("Final exam"), row.get("GPA")});
            });
        }
        highlightSchoolYearColumn();
    }

    private void highlightSchoolYearColumn() {
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                DefaultTableModel model = (DefaultTableModel) table.getModel();
                Object subject = model.getValueAt(row, 2); // Get the value of "Subject" column

                if (subject != null && subject.equals("Literature")) {
                    // If the subject is "Literature", highlight the "School Year" column
                    cell.setBackground(Color.RED);
                    cell.setForeground(Color.WHITE);
                } else {
                    // Otherwise, reset to default colors
                    cell.setBackground(table.getBackground());
                    cell.setForeground(table.getForeground());
                }

                return cell;
            }
        });
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

    private void filterTableByStudentName(String studentName) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        List<Map<String, Object>> data = TabScoresAction.Action();

        for (Map<String, Object> row : data) {
            if (row.get("Student Name").equals(studentName)) {
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
        highlightSchoolYearColumn();
    }
}