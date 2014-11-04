package com.devbyrod.distancetocity;

/**
 * Created by Rodrigo on 11/3/2014.
 */
public class QueriedCity {

    public String city;
    public double lat = 0;
    public double lon = 0;

    public QueriedCity( String city, double lat, double lon ){

        this.city = city;
        this.lat = lat;
        this.lon = lon;
    }
}
