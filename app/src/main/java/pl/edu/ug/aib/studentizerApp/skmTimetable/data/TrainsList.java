package pl.edu.ug.aib.studentizerApp.skmTimetable.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Konrad on 19.05.2015.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrainsList {
    @JsonProperty("records")
    public List<Train> records = new ArrayList<Train>();
}
