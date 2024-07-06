package com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabScores;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class RedLineCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Check if this is the start of a "15 minutes" column
        if (column % 5 == 4) {
            label.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.RED));
        } else {
            label.setBorder(null); // No border for other columns
        }

        return label;
    }
}
