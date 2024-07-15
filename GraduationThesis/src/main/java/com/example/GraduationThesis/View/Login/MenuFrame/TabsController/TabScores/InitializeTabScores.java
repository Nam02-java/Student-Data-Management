package com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabScores;

import com.example.GraduationThesis.Model.Enitity.Users.Roles.ERole;
import com.example.GraduationThesis.Service.LazySingleton.ListRoles.ListRolesManager;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ActionType;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ButtonEditor;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ButtonRenderer;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabGeneralInformation.InitializeTabGeneralInformation;

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
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
public class InitializeTabScores extends JPanel {

    private JTable table;
    private JComboBox<String> searchBox;
    private JList<String> nameList;
    private DefaultListModel<String> nameListModel;
    private Set<String> studentNames;
    private Map<String, List<String>> studentNameToSchoolYears;
    private boolean isUpdatingSchoolYear;
    private List<List<Integer>> blocks;
    private JLabel currentSchoolYearLabel;
    private JTextField editSchoolYearField;
    private List<String> currentSchoolYears;
    private int currentSchoolYearIndex;
    private JComboBox<String> filterBox;

    public InitializeTabScores() {
        setLayout(new BorderLayout());

        String[] columns = {"Student Name", "Subject", "School Year", "15 minutes", "1 hour", "Mid term", "Final exam", "GPA"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && column != 7;
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

        // Adding search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchBox = new JComboBox<>();
        searchBox.setEditable(true);
        searchBox.setBounds(100, 20, 165, 25);
        JButton searchButton = new JButton("Search");

        searchPanel.add(searchBox);
        searchPanel.add(searchButton);

        filterBox = new JComboBox<>();
        filterBox.setEditable(false);

        // Populate filterBox with unique addresses from testing map
        Set<String> uniqueAddresses = new HashSet<>(InitializeTabGeneralInformation.testing.values());
        for (String address : uniqueAddresses) {
            filterBox.addItem(address);
        }

        filterBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterNamesByAddress((String) filterBox.getSelectedItem());
            }
        });

        searchPanel.add(filterBox);

        add(searchPanel, BorderLayout.NORTH);

        // Adding name list panel
        JPanel nameListPanel = new JPanel(new BorderLayout());
        nameListModel = new DefaultListModel<>();
        nameList = new JList<>(nameListModel);
        nameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        nameListPanel.add(new JScrollPane(nameList), BorderLayout.CENTER);

        add(nameListPanel, BorderLayout.EAST);

        nameList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedName = nameList.getSelectedValue();
                    if (selectedName != null) {
                        searchBox.setSelectedItem(selectedName);
                        updateTableByStudentName(selectedName);
                    }
                }
            }
        });

        studentNames = new HashSet<>();
        studentNameToSchoolYears = new HashMap<>();
        loadStudentNames();

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
                } else {
                    // Display all student names if input is empty
                    for (String name : studentNames) {
                        searchBox.addItem(name);
                    }
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
        editSchoolYearField = new JTextField(10);

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
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 2 && !isUpdatingSchoolYear) {
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
        model.setRowCount(0);
    }

    private void loadStudentNames() {
        List<Map<String, Object>> data = TabScoresAction.Action();
        studentNames.clear();
        studentNameToSchoolYears.clear();
        nameListModel.clear();

        data.forEach(row -> {
            String studentName = (String) row.get("Student Name");
            String schoolYear = (String) row.get("School Year");
            String address = (String) row.get("Address"); // Add this line to get address from row data
            studentNames.add(studentName);
            studentNameToSchoolYears.computeIfAbsent(studentName, k -> new ArrayList<>()).add(schoolYear);
        });

        studentNameToSchoolYears.forEach((key, value) -> {
            List<String> uniqueSchoolYears = new ArrayList<>(new HashSet<>(value));
            Collections.sort(uniqueSchoolYears);
            studentNameToSchoolYears.put(key, uniqueSchoolYears);
        });

        // Populate the searchBox with unique student names
        for (String name : studentNames) {
            searchBox.addItem(name);
            nameListModel.addElement(name);
        }
    }

    private void filterTableByStudentNameAndSchoolYear(String studentName, String schoolYear) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
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
        isUpdatingSchoolYear = true;
        for (List<Integer> block : blocks) {
            if (block.contains(editedRow)) {
                for (int row : block) {
                    if (model.getValueAt(row, 2) != null && !model.getValueAt(row, 2).toString().isEmpty()) {
                        model.setValueAt(newValue, row, 2);
                    }
                }
                break;
            }
        }
        isUpdatingSchoolYear = false;
    }

    private void updateTableByStudentName(String studentName) {
        if (studentName != null && !studentName.isEmpty()) {
            currentSchoolYears = studentNameToSchoolYears.get(studentName);
            if (currentSchoolYears != null && !currentSchoolYears.isEmpty()) {
                currentSchoolYearIndex = 0;
                updateTableForCurrentSchoolYear();
            }
        }
    }

    private void addBlankRowsEvery13Rows() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 13; i < model.getRowCount(); i += 14) {
            model.insertRow(i, new Object[]{""});
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
                hideColumn(table, 0);
                hideColumn(table, 2);
            }
        }
    }

    // Method to delete record
    public void deleteRecord(int rowToDelete) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.removeRow(rowToDelete);
    }

    // Getter for JTable
    public JTable getTable() {
        return table;
    }

    private void filterNamesByAddress(String address) {
        nameListModel.clear();
        if (address == null || address.isEmpty()) {
            for (String name : studentNames) {
                nameListModel.addElement(name);
            }
        } else {
            for (Map.Entry<String, String> entry : InitializeTabGeneralInformation.testing.entrySet()) {
                if (entry.getValue().equals(address)) {
                    nameListModel.addElement(entry.getKey());
                }
            }
        }
    }
}
