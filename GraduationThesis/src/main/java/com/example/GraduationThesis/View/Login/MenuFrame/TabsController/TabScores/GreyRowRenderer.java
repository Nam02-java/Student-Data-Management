package com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabScores;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class GreyRowRenderer extends DefaultTableCellRenderer {

    private final Object[] header = {"ID", "Student Name", "Subject", "School Year", "15 minutes", "1 hour", "Mid term", "Final exam", "GPA"};

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (isHeaderRow(table, row)) {
            c.setBackground(Color.LIGHT_GRAY);
        } else {
            c.setBackground(Color.WHITE);
        }

        return c;
    }

    private boolean isHeaderRow(JTable table, int row) {
        for (int col = 0; col < header.length; col++) {
            if (header[col] != null && !header[col].equals(table.getValueAt(row, col))) {
                return false;
            }
        }
        return true;
    }
}

