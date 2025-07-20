package com.fleet;

public class Vehicle {
    private String id, make, model, year, color, plate, fuelType, type, notes;
    private int seats;

    public Vehicle(String id, String make, String model, String year, String color,
                   String plate, String fuelType, int seats, String type, String notes) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.plate = plate;
        this.fuelType = fuelType;
        this.seats = seats;
        this.type = type;
        this.notes = notes;
    }

    public String getId() { return id; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public String getYear() { return year; }
    public String getColor() { return color; }
    public String getPlate() { return plate; }
    public String getFuelType() { return fuelType; }
    public int getSeats() { return seats; }
    public String getType() { return type; }
    public String getNotes() { return notes; }

    @Override
    public String toString() {
        return id + ": " + year + " " + make + " " + model + " (" + plate + ")";
    }
}
