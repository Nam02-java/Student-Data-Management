package com.example.GraduationThesis;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class est {
    public static void main(String[] args) {
        // Tạo khung chính
        JFrame frame = new JFrame("Auto Suggest Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        // Tạo panel chính
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        // Hiển thị khung
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // Tạo nhãn cho ô tìm kiếm
        JLabel searchLabel = new JLabel("Nhập từ khóa:");
        searchLabel.setBounds(10, 20, 80, 25);
        panel.add(searchLabel);

        // Tạo ô tìm kiếm với JComboBox
        JComboBox<String> searchBox = new JComboBox<>();
        searchBox.setEditable(true);
        searchBox.setBounds(100, 20, 165, 25);
        panel.add(searchBox);

        // Danh sách từ gợi ý
        List<String> suggestions = new ArrayList<>();
        suggestions.add("Apple");
        suggestions.add("Banana");
        suggestions.add("Cherry");
        suggestions.add("Date");
        suggestions.add("Elderberry");
        suggestions.add("Fig");
        suggestions.add("Grape");

        // Thêm hành động khi người dùng gõ vào ô tìm kiếm
        JTextField searchText = (JTextField) searchBox.getEditor().getEditorComponent();
        searchText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String input = searchText.getText();
                searchBox.removeAllItems();
                if (!input.isEmpty()) {
                    for (String suggestion : suggestions) {
                        if (suggestion.toLowerCase().startsWith(input.toLowerCase())) {
                            searchBox.addItem(suggestion);
                        }
                    }
                    searchText.setText(input);
                    searchBox.setPopupVisible(true);
                }
            }
        });

        // Tạo nút tìm kiếm
        JButton searchButton = new JButton("Tìm kiếm");
        searchButton.setBounds(270, 20, 100, 25);
        panel.add(searchButton);

        // Thêm hành động cho nút tìm kiếm
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = (String) searchBox.getSelectedItem();
                // Hiển thị thông báo với từ khóa đã nhập
                JOptionPane.showMessageDialog(panel, "Bạn đã tìm kiếm: " + query);
            }
        });
    }
}

