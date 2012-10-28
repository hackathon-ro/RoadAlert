
package com.makanstudios.roadalert.model;

public class Alert {

    public long id;

    public long lat;

    public long lon;

    public long timestamp;

    public String deviceId;

    public Alert() {
    }

    public Alert(long lat, long lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public Alert(long id, long lat, long lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    public Alert(long id, long lat, long lon, long timestamp, String deviceId) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.timestamp = timestamp;
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + lat + " - " + lon + " (" + timestamp + ")";
    }
}
