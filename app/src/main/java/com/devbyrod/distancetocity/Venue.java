package com.devbyrod.distancetocity;

import android.location.Location;

/**
 * Created by Rodrigo on 10/31/2014.
 */
public class Venue {

    private Location location;
    private String name;
    private String category;
    private String address;
    private double distance;

    public Venue( String name, String category, String address, double lat, double lon, Location deviceLocation ){

        this.location = new Location("");
        this.location.setLatitude( lat );
        this.location.setLongitude( lon );

        this.name = name;
        this.category = category;
        this.address = address;
        this.distance = this.location.distanceTo( deviceLocation ) / 1000;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getAddress() {
        return address;
    }

    public double getDistance() {
        return distance;
    }
}
