package com.omenacle.bookaam.DataClasses;

public class Route{
    //Agency Key
    protected String a_k;
    //Route booking fare
    protected long price;
    //Route (Road to travel i.e From -> To)
    protected String route;
    //Travel Time
    protected String travel_time;


    public Route(){

        // Default constructor required for calls to DataSnapshot.getValue(Route.class)

    }

    public Route(String a_k, long price, String route, String travel_time) {

        this.a_k = a_k;
        this.price = price;
        this.route = route;
        this.travel_time = travel_time;

    }

    public String getA_k() {
        return a_k;
    }

    public void setA_k(String a_k) {
        this.a_k = a_k;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getTravel_time() {
        return travel_time;
    }

    public void setTravel_time(String travel_time) {
        this.travel_time = travel_time;
    }

}
