package com.example.bustrackingapplication;

public class FirebaseReceiveData {
    private String number;
    private String from;
    private String to;
    private double latitude;
    private double longitude;
    private String type;

    public FirebaseReceiveData(String number, String from,
                               String to, double latitude, double longitude, String type) {
        this.type = type;
        this.number = number;
        this.from = from;
        this.to = to;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public String getType() { return type; }

    public String getNumber() {
        return number;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
