package pl.edu.ug.aib.studentizerApp.skmTimetable;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import pl.edu.ug.aib.studentizerApp.skmTimetable.data.TrainsList;

/**
 * Created by Konrad on 19.05.2015.
 */

@Rest(rootUrl = "http://www.konradsopot.somee.com/",
        converters = { MappingJackson2HttpMessageConverter.class })
public interface SkmRestClient {
    //get timetable for specific idStart and idEnd
//    @Get("api/trains/?idStart={start}&idEnd={end}&today={today}")
//    TrainsList getTrains(int start, int end, boolean today);

    //method overload
    @Get("api/trains/?idStart={start}&idEnd={end}&hour={hour}")
    TrainsList getTrains(int start, int end, int hour);
}
