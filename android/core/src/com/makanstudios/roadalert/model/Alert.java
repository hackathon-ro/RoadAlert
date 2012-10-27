
package com.makanstudios.roadalert.model;

public class Alert {

    public long id;

    public long lat;

    public long lon;

    public long timestamp;

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

    @Override
    public String toString() {
        return "[" + id + "] " + lat + " - " + lon + " (" + timestamp + ")";
    }
}
