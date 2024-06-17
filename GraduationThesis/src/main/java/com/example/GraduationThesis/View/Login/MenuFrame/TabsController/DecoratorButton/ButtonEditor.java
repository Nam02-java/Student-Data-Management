package com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton;

import com.example.GraduationThesis.Service.LazySingleton.Password.PasswordManager;
import com.example.GraduationThesis.View.Login.LoginFrame;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.Action.*;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabConduct.InitializeTabConduct;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabPersonalInformation.InitializeTabPersonalInformation;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabGeneralInformation.InitializeTabGeneralInformation;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabPosition.InitializeTabPosition;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabScores.InitializeTabScores;
import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabUsersData.InitializeTabUserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonEditor extends DefaultCellEditor {

    protected JButton button;
    private InitializeTabGeneralInformation tabGeneralInformation;
    private InitializeTabPosition tabPosition;
    private InitializeTabScores tabScores;
    private InitializeTabConduct tabConduct;
    private InitializeTabPersonalInformation tabPersonalInformation;
    private InitializeTabUserData tabUserData;
    private ActionType actionType;
    private JFrame frame;
    private ActionInterface deleteAction = new DeleteActionInterfaceImplements();
    private ActionInterface deleteActionTabScores = new DeleteTabScoresImplements();
    private ActionInterface deleteTabConductImplements = new DeleteTabConductImplements();
    private ActionInterface adminAuthorizationAction = new AdminAuthorizationImplements();


    /**
     * this constructor for Tab General Information
     * it has delete student button
     */
    public ButtonEditor(JCheckBox checkBox, InitializeTabGeneralInformation tabGeneralInformation, ActionType actionType) {
        super(checkBox);
        this.tabGeneralInformation = tabGeneralInformation;
        this.actionType = actionType; // Thêm biến actionType
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    /**
     * this constructor for Tab Position
     * it has delete student button
     */
    public ButtonEditor(JCheckBox checkBox, InitializeTabPosition tabPosition, ActionType actionType) {
        super(checkBox);
        this.tabPosition = tabPosition;
        this.actionType = actionType;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    /**
     * this constructor for Tab Scores
     * it has delete student button
     */
    public ButtonEditor(JCheckBox checkBox, InitializeTabScores tabScores, ActionType actionType) {
        super(checkBox);
        this.tabScores = tabScores;
        this.actionType = actionType;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    /**
     * this constructor for Tab Conduct
     * it has delete student button
     */
    public ButtonEditor(JCheckBox checkBox, InitializeTabConduct tabConduct, ActionType actionType) {
        super(checkBox);
        this.tabConduct = tabConduct;
        this.actionType = actionType;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    /**
     * this constructor for Tab Personal Information
     * it has delete student button
     */
    public ButtonEditor(JCheckBox checkBox, InitializeTabPersonalInformation tabPersonalInformation, ActionType actionType) {
        super(checkBox);
        this.tabPersonalInformation = tabPersonalInformation;
        this.actionType = actionType;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }


    /**
     * this constructor for Tab User Data
     * it has delete user button
     * it has admin authorization button
     * so it needs special config with frame
     */
    public ButtonEditor(JFrame frame, JCheckBox checkBox, InitializeTabUserData tabUserData, ActionType actionType) {
        super(checkBox);
        this.frame = frame;
        this.tabUserData = tabUserData;
        this.actionType = actionType;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }


    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        // delete all old actionlistener
        ActionListener[] actionListeners = button.getActionListeners();
        for (ActionListener listener : actionListeners) {
            button.removeActionListener(listener);
        }

        // config the button with the value passed in and returns the node
        button.setText((value == null) ? "" : value.toString());

        if (tabGeneralInformation != null) {

            button.addActionListener(e -> {

                /**
                 * special line code for fix bug
                 * Exception in thread "AWT-EventQueue-0" java.lang.ArrayIndexOutOfBoundsException: 'number index' >= 'number index'
                 * number index example : 6
                 */
                stopCellEditing(table);

                int confirmDialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this student?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirmDialogResult == JOptionPane.YES_OPTION) {

                    // get the value of column "ID" from the selected row
                    Object idObject = table.getValueAt(row, 0);
                    Integer id = (Integer) idObject;

                    // delete record in mysql database
                    deleteAction.delete(id, actionType);

                    // delete record in table
                    tabGeneralInformation.deleteRecord(row);

                }
            });

        } else if (tabPosition != null) {
            button.addActionListener(e -> {

                /**
                 * special line code for fix bug
                 * Exception in thread "AWT-EventQueue-0" java.lang.ArrayIndexOutOfBoundsException: 'number index' >= 'number index'
                 * number index example : 6
                 */
                stopCellEditing(table);

                int confirmDialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this student?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirmDialogResult == JOptionPane.YES_OPTION) {

                    // get the value of column "ID" from the selected row
                    Object idObject = table.getValueAt(row, 0);
                    Integer id = (Integer) idObject;

                    // delete record in mysql database
                    deleteAction.delete(id, actionType);

                    // delete record in table
                    tabPosition.deleteRecord(row);
                }
            });

        } else if (tabScores != null) {
            button.addActionListener(e -> {

                /**
                 * special line code for fix bug
                 * Exception in thread "AWT-EventQueue-0" java.lang.ArrayIndexOutOfBoundsException: 'number index' >= 'number index'
                 * number index example : 6
                 */
                stopCellEditing(table);

                DefaultTableModel model = (DefaultTableModel) table.getModel();
                Object subjectName = model.getValueAt(row, 2); //subject name column
                Object studentName = model.getValueAt(row, 1); //subject name column

                int confirmDialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete all scores of " + subjectName + " subject of"
                        + studentName + " ?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirmDialogResult == JOptionPane.YES_OPTION) {

                    // get the value of column "ID" from the selected row
                    Object idObject = table.getValueAt(row, 0);
                    Integer id = (Integer) idObject;

                    tabScores.deleteRecord(row);

                    deleteActionTabScores.deleteTabScores(id, table, row);

                }
            });

        } else if (tabConduct != null) {
            button.addActionListener(e -> {

                /**
                 * special line code for fix bug
                 * Exception in thread "AWT-EventQueue-0" java.lang.ArrayIndexOutOfBoundsException: 'number index' >= 'number index'
                 * number index example : 6
                 */
                stopCellEditing(table);

                DefaultTableModel model = (DefaultTableModel) table.getModel();
                Object studentName = model.getValueAt(row, 1); //subject name column

                int confirmDialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete all scores conduct of student" + studentName + " ?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirmDialogResult == JOptionPane.YES_OPTION) {

                    // get the value of column "ID" from the selected row
                    Object idObject = table.getValueAt(row, 0);
                    Integer id = (Integer) idObject;

                    tabConduct.deleteRecord(row);

                    deleteTabConductImplements.deleteTabConduct(id, table, row);

                }
            });

        } else if (tabPersonalInformation != null) {
            button.addActionListener(e -> {

                /**
                 * special line code for fix bug
                 * Exception in thread "AWT-EventQueue-0" java.lang.ArrayIndexOutOfBoundsException: 'number index' >= 'number index'
                 * number index example : 6
                 */
                stopCellEditing(table);


                int confirmDialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this student ?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirmDialogResult == JOptionPane.YES_OPTION) {

                    // get the value of column "ID" from the selected row
                    Object idObject = table.getValueAt(row, 0);
                    Integer id = (Integer) idObject;

                    // delete record in mysql database
                    deleteAction.delete(id, actionType);

                    // delete record in table
                    tabPersonalInformation.deleteRecord(row);
                }
            });
        } else if (tabUserData != null) {
            String columnName = table.getColumnName(column);


            if ("Delete".equals(columnName)) {
                button.addActionListener(e -> {

                    /**
                     * special line code for fix bug
                     * Exception in thread "AWT-EventQueue-0" java.lang.ArrayIndexOutOfBoundsException: 'number index' >= 'number index'
                     * number index example : 6
                     */
                    stopCellEditing(table);

                    // get user name for popup
                    Object usernameObject = table.getValueAt(row, 1);


                    int confirmDialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete user " + usernameObject + " ?"
                            , "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                    if (confirmDialogResult == JOptionPane.YES_OPTION) {
                        DefaultTableModel model = (DefaultTableModel) table.getModel();

                        // get the value of column "Numberphone" from the selected row
                        Object numberPhoneObject = table.getValueAt(row, 2);
                        String numberphone = (String) numberPhoneObject;

                        // delete record in mysql database
                        for (int i = 0; i < model.getRowCount(); i++) {
                            if (table.getValueAt(i, 2).equals(numberphone)) {
                                deleteAction.delete(numberphone, actionType);
                                tabUserData.deleteRecord(i);
                                i--;
                            }
                        }
                    }

                });
            } else if ("Admin Authorization".equals(columnName)) {
                button.addActionListener(e -> {

                    /**
                     * special line code for fix bug
                     * Exception in thread "AWT-EventQueue-0" java.lang.ArrayIndexOutOfBoundsException: 'number index' >= 'number index'
                     * number index example : 6
                     */
                    stopCellEditing(table);

                    Object roleAdminObject = table.getValueAt(row, 4);
                    if ("Admin".equals(roleAdminObject)) {
                        JOptionPane.showMessageDialog(null, "This user is already an admin and cannot be authorized again.");
                        return;
                    }

                    Object usernameObject = table.getValueAt(row, 1);
                    int confirmDialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to authorize ADMIN for the " +
                            usernameObject + " user? you will also lose the ADMIN ROLE", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    if (confirmDialogResult == JOptionPane.YES_OPTION) {

                        JPasswordField passwordField1 = new JPasswordField();
                        JPasswordField passwordField2 = new JPasswordField();
                        Object[] message = {"Enter your password:", passwordField1, "Confirm your password:", passwordField2};
                        int option = JOptionPane.showConfirmDialog(null, message, "Password Verification", JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION) {
                            char[] password1 = passwordField1.getPassword();
                            char[] password2 = passwordField2.getPassword();

                            // convert password from char[] to String for comparison
                            String passwordString1 = new String(password1);
                            String passwordString2 = new String(password2);

                            if (passwordString1.equals(passwordString2)) {
                                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

                                boolean isMatch = passwordEncoder.matches(passwordString2, PasswordManager.getInstance().getPassword());
                                System.out.println(PasswordManager.getInstance().getPassword());
                                if (isMatch) {

                                    // get number phone
                                    Object numberPhoneObject = table.getValueAt(row, 2);

                                    String numberPhone = (String) numberPhoneObject;

                                    // admin authorization action
                                    adminAuthorizationAction.adminAuthorization(numberPhone);

                                    // need to log out and login again
                                    frame.dispose();
                                    new LoginFrame();

                                } else {
                                    // if the password does not match the original password
                                    JOptionPane.showMessageDialog(null, "Incorrect password. Authorization aborted.", "Password Verification Failed", JOptionPane.ERROR_MESSAGE);
                                }


                            } else {
                                // if the password1 does not match the password2
                                JOptionPane.showMessageDialog(null, "Passwords do not match. Authorization aborted.", "Password Verification Failed", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                });
            }
        }

        fireEditingStopped();
        return button;
    }


    @Override
    public Object getCellEditorValue() {
        // returns the value of the button
        return button.getText();
    }

    /**
     * special method for fix bug
     * Exception in thread "AWT-EventQueue-0" java.lang.ArrayIndexOutOfBoundsException: 'number index' >= 'number index'
     * number index example : 6
     */
    public void stopCellEditing(JTable table) {
        /**
         * basically, when you press delete on line last index number
         * so after you deleted that record with last index number,
         * swing still misunderstood that you was still editing line last index number
         * but now the last index no longer exists
         * this causes the error
         * Exception in thread "AWT-EventQueue-0" java.lang.ArrayIndexOutOfBoundsException: 'number index' >= 'number index'
         */
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }
    }
}
