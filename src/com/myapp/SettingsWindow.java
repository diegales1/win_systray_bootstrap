package com.myapp;

import javax.swing.*;
import java.awt.*;

public class SettingsWindow extends JFrame {

    private JTextField appNameField;
    private JTextField intervalField;
    private JTextField updateUrlField;

    public SettingsWindow() {

        setTitle("Settings");

        setSize(400, 250);

        setLayout(new BorderLayout());

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        JPanel form = new JPanel();

        form.setLayout(new GridLayout(0, 2, 10, 10));

        form.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // App Name
        form.add(new JLabel("App Name"));

        appNameField = new JTextField(
            Config.get("app.name", "My Tray App")
        );

        form.add(appNameField);

        // Interval
        form.add(new JLabel("Interval (seconds)"));

        intervalField = new JTextField(
            String.valueOf(Config.getInt("interval.seconds", 5))
        );

        form.add(intervalField);

        // Update URL
        form.add(new JLabel("Update URL"));

        updateUrlField = new JTextField(
            Config.get("update.url", "")
        );

        form.add(updateUrlField);

        add(form, BorderLayout.CENTER);

        // Buttons
        JPanel buttons = new JPanel();

        JButton saveButton = new JButton("Save");

        JButton cancelButton = new JButton("Cancel");

        buttons.add(saveButton);

        buttons.add(cancelButton);

        add(buttons, BorderLayout.SOUTH);

        // Save action
        saveButton.addActionListener(e -> saveSettings());

        // Cancel action
        cancelButton.addActionListener(e -> setVisible(false));
    }

    private void saveSettings() {

        try {

            Config.set("app.name", appNameField.getText());

            Config.set(
                "interval.seconds",
                Integer.parseInt(intervalField.getText())
            );

            Config.set("update.url", updateUrlField.getText());

            Config.save();

            JOptionPane.showMessageDialog(
                this,
                "Settings saved"
            );

            Log.get().info("Settings updated");

            setVisible(false);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Invalid settings",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}