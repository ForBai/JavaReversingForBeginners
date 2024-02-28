package me.forbai;

import javax.swing.*;
import java.awt.*;

public class Main {


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JDialog dialog = createDialog();
        dialog.setVisible(true);
    }

    private static JDialog createDialog() {
        JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(400, 200);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter your license key:"));
        JTextField textField = new JTextField(20);
        panel.add(textField);
        panel.add(createSubmitButton(textField, dialog));

        dialog.add(panel);
        return dialog;
    }

    private static JButton createSubmitButton(JTextField textField, JDialog dialog) {
        JButton button = new JButton("Submit");
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.GREEN);
        button.addActionListener(e -> {
            if (textField.getText().equals("1234")) {
                dialog.dispose();
                JOptionPane.showMessageDialog(null, "Correct license key!");
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect license key!");
            }
        });
        return button;
    }

}