package com.fleet;

import javax.swing.*;
import java.awt.*;

public class AddVehicleDialog extends JDialog {
    private JTextField idField = new JTextField(6);
    private JTextField makeField = new JTextField(8);
    private JTextField modelField = new JTextField(8);
    private JTextField yearField = new JTextField(6);
    private JTextField colorField = new JTextField(8);
    private JTextField plateField = new JTextField(8);
    private JTextField fuelTypeField = new JTextField(8);
    private JTextField seatsField = new JTextField(4);
    private JComboBox<String> typeBox = new JComboBox<>(new String[]{"Passenger", "Cargo", "Commercial", "Utility"});
    private JTextField notesField = new JTextField(14);
    private boolean ok = false;

    public AddVehicleDialog(Frame owner) {
        super(owner, "Add Vehicle", true);
        setLayout(new GridLayout(0, 2, 3, 3));
        add(new JLabel("ID:")); add(idField);
        add(new JLabel("Make:")); add(makeField);
        add(new JLabel("Model:")); add(modelField);
        add(new JLabel("Year:")); add(yearField);
        add(new JLabel("Color:")); add(colorField);
        add(new JLabel("Plate:")); add(plateField);
        add(new JLabel("Fuel Type:")); add(fuelTypeField);
        add(new JLabel("Seats:")); add(seatsField);
        add(new JLabel("Type:")); add(typeBox);
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

    public Vehicle getVehicle() {
        int seats = 0;
        try {
            if (!seatsField.getText().trim().isEmpty())
                seats = Integer.parseInt(seatsField.getText().trim());
        } catch (NumberFormatException ex) {
            seats = 0;
        }
        return new Vehicle(
            idField.getText(),
            makeField.getText(),
            modelField.getText(),
            yearField.getText(),
            colorField.getText(),
            plateField.getText(),
            fuelTypeField.getText(),
            seats,
            (String) typeBox.getSelectedItem(),
            notesField.getText()
        );
    }
}
