package com.fleet;

import javax.swing.*;
import java.awt.*;

public class AddPersonDialog extends JDialog {
    private JTextField idField = new JTextField(6);
    private JTextField nameField = new JTextField(14);
    private JTextField deptField = new JTextField(10);
    private JTextField accessField = new JTextField(8);
    private JTextField phoneField = new JTextField(10);
    private JTextField emailField = new JTextField(14);
    private JTextField notesField = new JTextField(14);
    private boolean ok = false;

    public AddPersonDialog(Frame owner) {
        super(owner, "Add Person", true);
        setLayout(new GridLayout(0, 2, 3, 3));
        add(new JLabel("ID:")); add(idField);
        add(new JLabel("Name:")); add(nameField);
        add(new JLabel("Department:")); add(deptField);
        add(new JLabel("Access Level:")); add(accessField);
        add(new JLabel("Phone:")); add(phoneField);
        add(new JLabel("Email:")); add(emailField);
        add(new JLabel("Notes:")); add(notesField);

        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Cancel");
        add(okBtn); add(cancelBtn);

        okBtn.addActionListener(e -> {
            ok = true;
            setVisible(false);
        });
        cancelBtn.addActionListener(e -> setVisible(false));
        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isOk() { return ok; }

    public Person getPerson() {
        return new Person(
            idField.getText(),
            nameField.getText(),
            deptField.getText(),
            accessField.getText(),
            phoneField.getText(),
            emailField.getText(),
            notesField.getText()
        );
    }
}
