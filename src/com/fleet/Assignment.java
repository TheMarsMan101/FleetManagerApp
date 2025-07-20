package com.fleet;

public class Assignment {
    private String personId;
    private String vehicleId;
    private String estimatedLength;
    private String purpose;

    public Assignment(String personId, String vehicleId, String estimatedLength, String purpose) {
        this.personId = personId;
        this.vehicleId = vehicleId;
        this.estimatedLength = estimatedLength;
        this.purpose = purpose;
    }

    public String getPersonId() { return personId; }
    public String getVehicleId() { return vehicleId; }
    public String getEstimatedLength() { return estimatedLength; }
    public String getPurpose() { return purpose; }
}
