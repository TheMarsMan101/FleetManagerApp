package com.fleet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FleetManagerApp extends JFrame {

    private static String getAppDataFolder() {
        String home = System.getProperty("user.home");
        String[] docFolderNames = {"Documents", "My Documents", "documents"};
        for (String folder : docFolderNames) {
            File f = new File(home, folder);
            if (f.exists() && f.isDirectory()) {
                File appFolder = new File(f, "FleetManagerApp");
                if (!appFolder.exists()) appFolder.mkdir();
                return appFolder.getAbsolutePath();
            }
        }
        File appFolder = new File(home, "FleetManagerApp");
        if (!appFolder.exists()) appFolder.mkdir();
        return appFolder.getAbsolutePath();
    }

    private static final String DOCS_FOLDER = getAppDataFolder();
    private static final String VEHICLES_FILE = DOCS_FOLDER + File.separator + "vehicles.csv";
    private static final String PEOPLE_FILE = DOCS_FOLDER + File.separator + "people.csv";
    private static final String ASSIGNMENTS_FILE = DOCS_FOLDER + File.separator + "assignments.csv";

    private List<Vehicle> vehicles;
    private List<Person> people;
    private List<Assignment> assignments;

    private DefaultTableModel vehicleTableModel;
    private DefaultTableModel personTableModel;
    private DefaultTableModel assignmentTableModel;

    private javax.swing.Timer autosaveTimer;

    public FleetManagerApp() {
        vehicles = new ArrayList<>();
        people = new ArrayList<>();
        assignments = new ArrayList<>();
        loadData();

        setTitle("Municipal Fleet Manager (Data: " + DOCS_FOLDER + ")");
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Vehicles", buildVehiclePanel());
        tabbedPane.add("People", buildPeoplePanel());
        tabbedPane.add("Assignments", buildAssignmentPanel());
        add(tabbedPane);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { saveData(); }
        });

        autosaveTimer = new javax.swing.Timer(300000, evt -> saveData());
        autosaveTimer.setRepeats(true);
        autosaveTimer.start();
    }

    private JPanel buildVehiclePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] cols = {"ID", "Make", "Model", "Year", "Color", "Plate", "Fuel", "Seats", "Type", "Notes"};
        vehicleTableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(vehicleTableModel);
        refreshVehicleTable();
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton add = new JButton("Add Vehicle");
        JButton del = new JButton("Delete Vehicle");
        bottom.add(add); bottom.add(del);

        add.addActionListener(e -> {
            AddVehicleDialog dialog = new AddVehicleDialog(this);
            dialog.setVisible(true);
            if (dialog.isOk()) {
                Vehicle v = dialog.getVehicle();
                for (Vehicle vv : vehicles)
                    if (vv.getId().equals(v.getId())) {
                        JOptionPane.showMessageDialog(this, "Duplicate vehicle ID.");
                        return;
                    }
                vehicles.add(v);
                refreshVehicleTable();
            }
        });
        del.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String vid = vehicleTableModel.getValueAt(row, 0).toString();
                vehicles.removeIf(v -> v.getId().equals(vid));
                assignments.removeIf(a -> a.getVehicleId().equals(vid));
                refreshVehicleTable();
                refreshAssignmentTable();
            }
        });

        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildPeoplePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] cols = {"ID", "Name", "Department", "Access Level", "Phone", "Email", "Notes"};
        personTableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(personTableModel);
        refreshPersonTable();
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton add = new JButton("Add Person");
        JButton del = new JButton("Delete Person");
        bottom.add(add); bottom.add(del);

        add.addActionListener(e -> {
            AddPersonDialog dialog = new AddPersonDialog(this);
            dialog.setVisible(true);
            if (dialog.isOk()) {
                Person p = dialog.getPerson();
                for (Person pp : people)
                    if (pp.getId().equals(p.getId())) {
                        JOptionPane.showMessageDialog(this, "Duplicate person ID.");
                        return;
                    }
                people.add(p);
                refreshPersonTable();
            }
        });
        del.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String pid = personTableModel.getValueAt(row, 0).toString();
                people.removeIf(p -> p.getId().equals(pid));
                assignments.removeIf(a -> a.getPersonId().equals(pid));
                refreshPersonTable();
                refreshAssignmentTable();
            }
        });

        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildAssignmentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] cols = {"Person", "Vehicle", "Estimated Length", "Purpose"};
        assignmentTableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(assignmentTableModel);
        refreshAssignmentTable();
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton add = new JButton("Add Assignment");
        JButton unassign = new JButton("Unassign (Check-In)");
        bottom.add(add); bottom.add(unassign);

        add.addActionListener(e -> {
            AddAssignmentDialog dialog = new AddAssignmentDialog(this, people, vehicles, assignments);
            dialog.setVisible(true);
            if (dialog.isOk()) {
                String pid = dialog.getSelectedPersonId();
                String vid = dialog.getSelectedVehicleId();
                if (pid == null || vid == null) {
                    JOptionPane.showMessageDialog(this, "Select both person and vehicle.");
                    return;
                }
                if (isPersonAssigned(pid)) {
                    JOptionPane.showMessageDialog(this, "Person already assigned.");
                    return;
                }
                if (isVehicleAssigned(vid)) {
                    JOptionPane.showMessageDialog(this, "Vehicle already assigned.");
                    return;
                }
                assignments.add(new Assignment(pid, vid, dialog.getEstimatedLength(), dialog.getPurpose()));
                refreshAssignmentTable();
            }
        });

        unassign.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String personDisplay = assignmentTableModel.getValueAt(row, 0).toString();
                String vehicleDisplay = assignmentTableModel.getValueAt(row, 1).toString();
                String pid = extractIdFromPersonDisplay(personDisplay);
                String vid = extractIdFromVehicleDisplay(vehicleDisplay);
                assignments.removeIf(a -> a.getPersonId().equals(pid) && a.getVehicleId().equals(vid));
                refreshAssignmentTable();
            }
        });

        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshPersonTable() {
        personTableModel.setRowCount(0);
        for (Person p : people)
            personTableModel.addRow(new Object[]{
                p.getId(), p.getName(), p.getDepartment(), p.getAccessLevel(),
                p.getPhone(), p.getEmail(), p.getNotes()
            });
    }

    private void refreshVehicleTable() {
        vehicleTableModel.setRowCount(0);
        for (Vehicle v : vehicles)
            vehicleTableModel.addRow(new Object[]{
                v.getId(), v.getMake(), v.getModel(), v.getYear(), v.getColor(),
                v.getPlate(), v.getFuelType(), v.getSeats(), v.getType(), v.getNotes()
            });
    }

    private void refreshAssignmentTable() {
        assignmentTableModel.setRowCount(0);
        for (Assignment a : assignments) {
            assignmentTableModel.addRow(new Object[]{
                getPersonDisplay(a.getPersonId()),
                getVehicleDisplay(a.getVehicleId()),
                a.getEstimatedLength(),
                a.getPurpose()
            });
        }
    }

    private boolean isPersonAssigned(String personId) {
        if (personId == null) return false;
        personId = extractIdFromPersonDisplay(personId);
        for (Assignment a : assignments)
            if (a.getPersonId().equals(personId)) return true;
        return false;
    }

    private boolean isVehicleAssigned(String vehicleId) {
        if (vehicleId == null) return false;
        vehicleId = extractIdFromVehicleDisplay(vehicleId);
        for (Assignment a : assignments)
            if (a.getVehicleId().equals(vehicleId)) return true;
        return false;
    }

    private String getPersonDisplay(String id) {
        for (Person p : people)
            if (p.getId().equals(id))
                return p.getName() + " (" + p.getId() + ")";
        return id;
    }

    private String getVehicleDisplay(String id) {
        for (Vehicle v : vehicles)
            if (v.getId().equals(id))
                return v.getPlate() + " (" + v.getId() + ")";
        return id;
    }

    private String extractIdFromPersonDisplay(String display) {
        if (display != null && display.endsWith(")")) {
            int i = display.lastIndexOf('(');
            if (i > 0) return display.substring(i + 1, display.length() - 1);
        }
        return display;
    }

    private String extractIdFromVehicleDisplay(String display) {
        if (display != null && display.endsWith(")")) {
            int i = display.lastIndexOf('(');
            if (i > 0) return display.substring(i + 1, display.length() - 1);
        }
        return display;
    }

    private void loadData() {
        try {
            vehicles = CsvUtils.loadVehicles(VEHICLES_FILE);
            people = CsvUtils.loadPeople(PEOPLE_FILE);
            assignments = CsvUtils.loadAssignments(ASSIGNMENTS_FILE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load data: " + e.getMessage());
        }
    }

    private void saveData() {
        try {
            CsvUtils.saveVehicles(VEHICLES_FILE, vehicles);
            CsvUtils.savePeople(PEOPLE_FILE, people);
            CsvUtils.saveAssignments(ASSIGNMENTS_FILE, assignments);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FleetManagerApp().setVisible(true);
        });
    }
}
