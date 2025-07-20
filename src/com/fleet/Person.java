package com.fleet;

public class Person {
    private String id, name, department, accessLevel, phone, email, notes;

    public Person(String id, String name, String department, String accessLevel, String phone, String email, String notes) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.accessLevel = accessLevel;
        this.phone = phone;
        this.email = email;
        this.notes = notes;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public String getAccessLevel() { return accessLevel; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getNotes() { return notes; }

    @Override
    public String toString() {
        return id + ": " + name + " (" + department + ")";
    }
}
