package com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabScores;

import com.example.GraduationThesis.Model.Enitity.Users.Roles.ERole;
import com.example.GraduationThesis.Service.LazySingleton.ListRoles.ListRolesManager;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ActionType;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ButtonEditor;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ButtonRenderer;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
public class InitializeTabScores extends JPanel {

    private JTable table;
    private JComboBox<String> searchBox;
    private List<String> studentNames;
    private Map<String, List<String>> studentNameToSchoolYears; // Map to store student name and their school years
    private boolean isUpdatingSchoolYear; // Flag to prevent recursive updates
    private List<List<Integer>> blocks; // List of blocks, each block contains indices of 13 rows
    private JLabel currentSchoolYearLabel;
    private JTextField editSchoolYearField;
    private List<String> currentSchoolYears;
    private int currentSchoolYearIndex;

    public InitializeTabScores() {
        setLayout(new BorderLayout());

        String[] columns = {"Student Name", "Subject", "School Year", "15 minutes", "1 hour", "Mid term", "Final exam", "GPA"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Disable editing for specific columns
                return column != 0 && column != 7; // Column 0 is "Student Name", column 7 is "GPA"
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

        // Adding search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchBox = new JComboBox<>();
        searchBox.setEditable(true);
        searchBox.setBounds(100, 20, 165, 25);
        JButton searchButton = new JButton("Search");

        searchPanel.add(searchBox);
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.NORTH);

        studentNames = new ArrayList<>();
        studentNameToSchoolYears = new HashMap<>();
        loadStudentNames(); // Load the list of student names and school years

        JTextField searchText = (JTextField) searchBox.getEditor().getEditorComponent();
        searchText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String input = searchText.getText();
                searchBox.removeAllItems();
                if (!input.isEmpty()) {
                    Set<String> suggestions = new HashSet<>();
                    for (String suggestion : studentNames) {
                        if (suggestion.toLowerCase().startsWith(input.toLowerCase()) && !suggestions.contains(suggestion)) {
                            suggestions.add(suggestion);
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
                String selectedItem = (String) searchBox.getSelectedItem();
                if (selectedItem != null && !selectedItem.isEmpty()) {
                    String[] parts = selectedItem.split(" \\(");
                    String studentName = parts[0].trim();
                    currentSchoolYears = studentNameToSchoolYears.get(studentName);
                    if (currentSchoolYears != null && !currentSchoolYears.isEmpty()) {
                        currentSchoolYearIndex = 0;
                        updateTableForCurrentSchoolYear();
                    }
                }
            }
        });

        // Adding navigation panel
        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton leftButton = new JButton("<");
        JButton rightButton = new JButton(">");
        JButton editButton = new JButton("Edit");
        currentSchoolYearLabel = new JLabel();
        editSchoolYearField = new JTextField(10); // Initialize the text field but do not add to the panel yet

        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentSchoolYears != null && currentSchoolYearIndex > 0) {
                    if (navigationPanel.getComponent(1) instanceof JTextField) {
                        String newSchoolYear = editSchoolYearField.getText();
                        currentSchoolYears.set(currentSchoolYearIndex, newSchoolYear);
                        updateTableForCurrentSchoolYear();
                        navigationPanel.remove(editSchoolYearField);
                        navigationPanel.add(currentSchoolYearLabel, 1);
                        navigationPanel.revalidate();
                        navigationPanel.repaint();
                    }
                    currentSchoolYearIndex--;
                    updateTableForCurrentSchoolYear();
                }
            }
        });

        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentSchoolYears != null && currentSchoolYearIndex < currentSchoolYears.size() - 1) {
                    if (navigationPanel.getComponent(1) instanceof JTextField) {
                        String newSchoolYear = editSchoolYearField.getText();
                        currentSchoolYears.set(currentSchoolYearIndex, newSchoolYear);
                        updateTableForCurrentSchoolYear();
                        navigationPanel.remove(editSchoolYearField);
                        navigationPanel.add(currentSchoolYearLabel, 1);
                        navigationPanel.revalidate();
                        navigationPanel.repaint();
                    }
                    currentSchoolYearIndex++;
                    updateTableForCurrentSchoolYear();
                }
            }
        });


        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (navigationPanel.getComponent(1) instanceof JLabel) {
                    String currentText = currentSchoolYearLabel.getText();
                    editSchoolYearField.setText(currentText);
                    navigationPanel.remove(currentSchoolYearLabel);
                    navigationPanel.add(editSchoolYearField, 1);
                    navigationPanel.revalidate();
                    navigationPanel.repaint();
                    editSchoolYearField.requestFocus();
                } else {
                    String newSchoolYear = editSchoolYearField.getText();
                    if (!newSchoolYear.isEmpty()) {
                        currentSchoolYears.set(currentSchoolYearIndex, newSchoolYear);
                        updateTableForCurrentSchoolYear();
                    }
                    navigationPanel.remove(editSchoolYearField);
                    navigationPanel.add(currentSchoolYearLabel, 1);
                    navigationPanel.revalidate();
                    navigationPanel.repaint();
                }
            }
        });

        navigationPanel.add(leftButton);
        navigationPanel.add(currentSchoolYearLabel);
        navigationPanel.add(editButton);
        navigationPanel.add(rightButton);

        add(navigationPanel, BorderLayout.SOUTH);

        // Initialize blocks
        initializeBlocks();

        // Add TableModelListener to detect changes in the "School Year" column
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 2 && !isUpdatingSchoolYear) { // 2 is the index of "School Year" column
                    int editedRow = e.getFirstRow();
                    String newValue = (String) model.getValueAt(editedRow, 2);
                    updateSchoolYearColumn(newValue, editedRow);
                }
            }
        });

        // Ensure the table is empty upon initialization
        updateData();
    }

    private void initializeBlocks() {
        blocks = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int rowCount = model.getRowCount();
        int blockStart = 0;
        while (blockStart < rowCount) {
            List<Integer> block = new ArrayList<>();
            for (int i = blockStart; i < blockStart + 13 && i < rowCount; i++) {
                block.add(i);
            }
            blocks.add(block);
            blockStart += 13;
        }
    }

    public void updateData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear current data in the table
    }

    private void loadStudentNames() {
        List<Map<String, Object>> data = TabScoresAction.Action();
        studentNames.clear(); // Clear previous list of student names
        studentNameToSchoolYears.clear(); // Clear previous map of student names to school years

        data.forEach(row -> {
            String studentName = (String) row.get("Student Name");
            String schoolYear = (String) row.get("School Year");
            studentNames.add(studentName);
            studentNameToSchoolYears.computeIfAbsent(studentName, k -> new ArrayList<>()).add(schoolYear);
        });

        // Remove duplicates and sort the school years for each student
        studentNameToSchoolYears.forEach((key, value) -> {
            List<String> uniqueSchoolYears = new ArrayList<>(new HashSet<>(value));
            Collections.sort(uniqueSchoolYears);
            studentNameToSchoolYears.put(key, uniqueSchoolYears);
        });
    }

    public void deleteRecord(int rowIndex) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setValueAt("", rowIndex, 3); // 15 minutes column
        model.setValueAt("", rowIndex, 4); // 1 hour column
        model.setValueAt("", rowIndex, 5); // Mid term column
        model.setValueAt("", rowIndex, 6); // Final exam column
        model.setValueAt("0.0", rowIndex, 7); // GPA column
    }

    public JTable getTable() {
        return table;
    }

    private void filterTableByStudentNameAndSchoolYear(String studentName, String schoolYear) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear all current rows
        List<Map<String, Object>> data = TabScoresAction.Action();

        for (Map<String, Object> row : data) {
            if (row.get("Student Name").equals(studentName) && row.get("School Year").equals(schoolYear)) {
                if (ListRolesManager.getInstance().getRoles().contains(ERole.ROLE_ADMIN.toString())) {
                    model.addRow(new Object[]{
                            row.get("Student Name"), row.get("Subject"),
                            row.get("School Year"),
                            row.get("15 minutes"), row.get("1 hour"), row.get("Mid term"), row.get("Final exam"), row.get("GPA"),
                            "Delete"});
                } else {
                    model.addRow(new Object[]{
                            row.get("Student Name"), row.get("Subject"),
                            row.get("School Year"),
                            row.get("15 minutes"), row.get("1 hour"), row.get("Mid term"), row.get("Final exam"), row.get("GPA")});
                }
            }
        }
        addBlankRowsEvery13Rows();
    }

    private void updateSchoolYearColumn(String newValue, int editedRow) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        isUpdatingSchoolYear = true; // Set the flag to true to prevent recursive updates
        for (List<Integer> block : blocks) {
            if (block.contains(editedRow)) {
                for (int row : block) {
                    if (model.getValueAt(row, 2) != null && !model.getValueAt(row, 2).toString().isEmpty()) {
                        model.setValueAt(newValue, row, 2); // 2 is the index of "School Year" column
                    }
                }
                break;
            }
        }
        isUpdatingSchoolYear = false; // Reset the flag after updating
    }

    private void addBlankRowsEvery13Rows() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 13; i < model.getRowCount(); i += 14) {
            model.insertRow(i, new Object[]{""}); // Add an empty row
        }
    }

    private void hideColumn(JTable table, int columnIndex) {
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);
    }

    private void updateTableForCurrentSchoolYear() {
        if (currentSchoolYears != null && !currentSchoolYears.isEmpty()) {
            String currentSchoolYear = currentSchoolYears.get(currentSchoolYearIndex);
            currentSchoolYearLabel.setText(currentSchoolYear);
            String selectedItem = (String) searchBox.getSelectedItem();
            if (selectedItem != null && !selectedItem.isEmpty()) {
                String[] parts = selectedItem.split(" \\(");
                String studentName = parts[0].trim();
                filterTableByStudentNameAndSchoolYear(studentName, currentSchoolYear);
                hideColumn(table, 0); // Hide the "Student Name" column
                hideColumn(table, 2); // Hide the "School Year" column
            }
        }
    }
}



