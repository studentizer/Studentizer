package pl.edu.ug.aib.studentizerApp.skmTimetable.gps;

import java.util.ArrayList;
import java.util.List;

import pl.edu.ug.aib.studentizerApp.skmTimetable.data.Station;
import pl.edu.ug.aib.studentizerApp.skmTimetable.data.StationDistance;
import pl.edu.ug.aib.studentizerApp.skmTimetable.data.StationsList;

/**
 * Created by Konrad on 22.05.2015.
 */
public class GeolocationUtilities {

    //TODO: wykorzystanie tej metody na znalezienie najbliższej stacji
    public Station getClosestStation(double latitude, double longitude){
        Station station = new Station();
        StationsList allStations = new StationsList();
        List<StationDistance> stationDistanceList = new ArrayList<StationDistance>();

        //get all stations to loop through
        List<Station> stationList = allStations.getAllStations();
        //temporary distance
        double tempDistance;

        //calculate distance
        for(int i = 0; i < stationList.size(); i++){
            tempDistance = getDistance(latitude, longitude, stationList.get(i).latitude, stationList.get(i).longitude);
            //fill list with distance
            stationDistanceList.add(new StationDistance(stationList.get(i).id, tempDistance));
        }

        //get closest distance & stationId
        StationDistance stationDistance = getMinDistance(stationDistanceList);

        //get closest Station by looking through stationId
        station = getStationById(stationDistance);

        return station;
    }

    public double getDistance(double startLat, double startLong, double endLat, double endLong){
        //My method from C#
//        private double GeoDistance(double latitude1, double longitude1, double latitude2, double longitude2)
//        {
//            //gets distance in kilometers
//            const double equatorial_diameter = 12756.274;
//            double a = (longitude2 - longitude1) * Math.Cos(latitude1 * Math.PI / 180);
//            double b = (latitude2 - latitude1);
//            return Math.Round(Math.Sqrt(Math.Pow(a, 2) + Math.Pow(b, 2)) * Math.PI * equatorial_diameter / 360, 4);
//        }
        final double equatorial_diameter = 12756.274;
        double a = (endLong - startLong) * Math.cos(startLat * Math.PI / 180);
        double b = (endLat - startLat);
        double distance = Math.round(Math.sqrt(Math.pow(a, 2.0) + Math.pow(b, 2.0)) * Math.PI * equatorial_diameter / 360);

        return distance;
    }

    public StationDistance getMinDistance(List<StationDistance> stationDistanceList){
        StationDistance stationDistance = new StationDistance();
        int tempId = 0;
        double tempMin = Double.MAX_VALUE;

        for(int i = 0; i < stationDistanceList.size(); i++){
            if(stationDistanceList.get(i).distance < tempMin) {
                tempId = stationDistanceList.get(i).id;
                tempMin = stationDistanceList.get(i).distance;
            }
        }

        stationDistance.id = tempId;
        stationDistance.distance = tempMin;

        return stationDistance;
    }

    public Station getStationById(StationDistance stationDistance){
        StationsList list = new StationsList();
        List<Station> stationList = list.getAllStations();

        //linear search
        for(int i = 0; i < stationList.size(); i++){
            if(stationDistance.id == stationList.get(i).id)
                return stationList.get(i);
        }

        return new Station(0, "no_match", 0.0, 0.0);
    }

}
