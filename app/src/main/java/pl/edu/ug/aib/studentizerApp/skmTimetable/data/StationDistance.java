package pl.edu.ug.aib.studentizerApp.skmTimetable.data;

import java.io.Serializable;

/**
 * Created by Konrad on 22.05.2015.
 */
public class StationDistance implements Serializable {
    public int id;
    public double distance;

    public StationDistance(int id, double distance){
        this.id = id;
        this.distance = distance;
    }

    public StationDistance(){

    }
}
