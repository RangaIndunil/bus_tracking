package com.example.bustrackingapplication;

public class FirebaseReceiveData {
    private int id;
    private String number;
    private String from;
    private String to;
    private double latitude;
    private double longitude;

    public FirebaseReceiveData(String number, String from,
                               String to, double latitude, double longitude) {
        this.id = id;
        this.number = number;
        this.from = from;
        this.to = to;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public int getId(){
        return id;
    }

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
