package pl.edu.ug.aib.studentizerApp.skmTimetable.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Konrad on 19.05.2015.
 */

@JsonIgnoreProperties(ignoreUnknown = true) //prevents crashes after changing json structure
public class Train implements Serializable{
//    "Id": "30x33x19",
//            "IdStart": "30",
//            "IdEnd": "33",
//            "TimetableDate": "2015-05-19",
//            "Hour": "03:",
//            "Minute": "12",
//            "Tip": "W1"

//    @JsonIgnore
//    @JsonProperty("Id")
//    public String idTimetable;
//
//    @JsonProperty("IdStart")
//    public int idStart;
//    @JsonProperty("IdEnd")
//    public int idEnd;
//    @JsonProperty("TimetableDate")
//    public String timetableDate;
    @JsonProperty("Hour")
    public String hour;
    @JsonProperty("Minute")
    public String minute;
    @JsonProperty("Tip")
    public String tip;
}
