package com.fleet;

import javax.swing.*;
import java.awt.*;

public class AddAssignmentDialog extends JDialog {
    private JComboBox<String> personBox;
    private JComboBox<String> vehicleBox;
    private JTextField lengthField;
    private JTextField purposeField;
    private boolean ok = false;

    public AddAssignmentDialog(Frame owner, java.util.List<Person> people, java.util.List<Vehicle> vehicles, java.util.List<Assignment> assignments) {
        super(owner, "Add Assignment", true);
        setLayout(new GridLayout(0, 2, 5, 5));

        personBox = new JComboBox<>();
        vehicleBox = new JComboBox<>();

        // Only show unassigned people/vehicles
        for (Person p : people) {
            boolean assigned = false;
            for (Assignment a : assignments)
                if (a.getPersonId().equals(p.getId()))
                    assigned = true;
            if (!assigned)
                personBox.addItem(p.getName() + " (" + p.getId() + ")");
        }
        for (Vehicle v : vehicles) {
            boolean assigned = false;
            for (Assignment a : assignments)
                if (a.getVehicleId().equals(v.getId()))
                    assigned = true;
            if (!assigned)
                vehicleBox.addItem(v.getPlate() + " (" + v.getId() + ")");
        }

        lengthField = new JTextField();
        purposeField = new JTextField();

        add(new JLabel("Person:")); add(personBox);
        add(new JLabel("Vehicle:")); add(vehicleBox);
        add(new JLabel("Estimated Length:")); add(lengthField);
        add(new JLabel("Purpose:")); add(purposeField);

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

    public String getSelectedPersonId() {
        String display = (String) personBox.getSelectedItem();
        if (display != null && display.endsWith(")")) {
            int i = display.lastIndexOf('(');
            if (i > 0) return display.substring(i + 1, display.length() - 1);
        }
        return null;
    }

    public String getSelectedVehicleId() {
        String display = (String) vehicleBox.getSelectedItem();
        if (display != null && display.endsWith(")")) {
            int i = display.lastIndexOf('(');
            if (i > 0) return display.substring(i + 1, display.length() - 1);
        }
        return null;
    }

    public String getEstimatedLength() {
        return lengthField.getText();
    }

    public String getPurpose() {
        return purposeField.getText();
    }
}
