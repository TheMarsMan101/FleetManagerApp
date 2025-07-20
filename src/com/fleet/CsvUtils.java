package com.fleet;

import java.io.*;
import java.util.*;

public class CsvUtils {
    public static List<Vehicle> loadVehicles(String filename) throws IOException {
        List<Vehicle> vehicles = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) return vehicles;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);
                int seats = 0;
                try { seats = (p.length >= 8 && !p[7].isEmpty()) ? Integer.parseInt(p[7]) : 0; } catch (NumberFormatException ex) { seats = 0; }
                if (p.length >= 10) {
                    vehicles.add(new Vehicle(p[0], p[1], p[2], p[3], p[4], p[5], p[6], seats, p[8], p[9]));
                }
            }
        }
        return vehicles;
    }

    public static void saveVehicles(String filename, List<Vehicle> vehicles) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Vehicle v : vehicles) {
                pw.println(String.join(",",
                        v.getId(), v.getMake(), v.getModel(), v.getYear(), v.getColor(),
                        v.getPlate(), v.getFuelType(), String.valueOf(v.getSeats()), v.getType(),
                        v.getNotes() == null ? "" : v.getNotes().replace(",", ";")));
            }
        }
    }

    public static List<Person> loadPeople(String filename) throws IOException {
        List<Person> people = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) return people;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length >= 7) {
                    people.add(new Person(p[0], p[1], p[2], p[3], p[4], p[5], p[6]));
                }
            }
        }
        return people;
    }

    public static void savePeople(String filename, List<Person> people) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Person p : people) {
                pw.println(String.join(",",
                        p.getId(), p.getName(), p.getDepartment(), p.getAccessLevel(),
                        p.getPhone(), p.getEmail(), p.getNotes() == null ? "" : p.getNotes().replace(",", ";")));
            }
        }
    }

    public static List<Assignment> loadAssignments(String filename) throws IOException {
        List<Assignment> assignments = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) return assignments;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length >= 4)
                    assignments.add(new Assignment(p[0], p[1], p[2], p[3]));
                else if (p.length == 2)
                    assignments.add(new Assignment(p[0], p[1], "", ""));
            }
        }
        return assignments;
    }

    public static void saveAssignments(String filename, List<Assignment> assignments) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Assignment a : assignments) {
                // Save all 4 fields, escaping commas in purpose and length
                pw.println(
                        (a.getPersonId() == null ? "" : a.getPersonId()) + "," +
                        (a.getVehicleId() == null ? "" : a.getVehicleId()) + "," +
                        (a.getEstimatedLength() == null ? "" : a.getEstimatedLength().replace(",", ";")) + "," +
                        (a.getPurpose() == null ? "" : a.getPurpose().replace(",", ";"))
                );
            }
        }
    }
}
