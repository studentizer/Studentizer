package pl.edu.ug.aib.studentizerApp.skmTimetable.utilities;

import java.util.List;

import pl.edu.ug.aib.studentizerApp.skmTimetable.data.Station;

/**
 * Created by Konrad on 23.05.2015.
 */
public class DirectionHelpers {
    public int getLastDirection(List<Station> directionList){
        return directionList.get(directionList.size() - 1).id;
    }

    public int getFirstDirection(List<Station> directionList){
        return directionList.get(0).id;
    }

    public int getDirectionIteration(List<Station> directionList, int match){
        int idx = 0;

        for(int i = 0; i < directionList.size(); i++){
            if(directionList.get(i).id == match){
                idx = i;
                return idx;
            }
        }

        return idx;
    }

    public int getDirectionId(List<Station> directionList, int stationIteration){
        //returns station ID of specified number of iteration
        return directionList.get(stationIteration).id;
    }

    public Station getDirection(List<Station> directionList, int stationIteration){
        //returns whole class of Station by specified number of iteration
        return directionList.get(stationIteration);
    }

    public int getDirectionBeforeCurrent(List<Station> directionList, int currentStationId){
        int first = getFirstDirection(directionList);
        int last = getLastDirection(directionList);

        if(currentStationId <= first || currentStationId >= last)
            return -1;

        //returns iteration of specified station ID
        return getDirectionIteration(directionList, currentStationId) - 1;
    }

    public int getDirectionAfterCurrent(List<Station> directionList, int currentStationId){
        int first = getFirstDirection(directionList);
        int last = getLastDirection(directionList);

        if(currentStationId <= first || currentStationId >= last)
            return -1;

        //returns iteration of specified station ID
        return  getDirectionIteration(directionList, currentStationId) + 1;
    }
}
