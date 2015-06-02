package pl.edu.ug.aib.studentizerApp.skmTimetable.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Konrad on 22.05.2015.
 */
public class Station implements Serializable {
    //"Id":1,"Name":"SŁUPSK","Latitude":54.467735,"Longitude":17.016978
    public int id;
    public String name;
    public double latitude;
    public double longitude;

    public Station (int id, String name, double latitude, double longitude){
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Station(){

    }
}