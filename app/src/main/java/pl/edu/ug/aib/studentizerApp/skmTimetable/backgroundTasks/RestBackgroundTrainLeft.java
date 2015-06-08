package pl.edu.ug.aib.studentizerApp.skmTimetable.backgroundTasks;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;

import pl.edu.ug.aib.studentizerApp.DrawerActivity;
import pl.edu.ug.aib.studentizerApp.skmTimetable.SkmRestClient;
import pl.edu.ug.aib.studentizerApp.skmTimetable.data.TrainsList;

/**
 * Created by Konrad on 19.05.2015.
 */
@EBean
public class RestBackgroundTrainLeft {
    @RootContext
    DrawerActivity activity; //activity with listview
    @RestService
    SkmRestClient restClient; //now I'm using my awesome API

    //load trains timetable without blocking main thread
    //because it's so annoying :)
    @Background
    public void getTrains(int start, int end, int hour){
        try{
            TrainsList trainsList = restClient.getTrains(start, end, hour);
            if(start == end){
                publishWarning();
            } else{
                publishResult(trainsList);
            }
        } catch(Exception e){
            publishError(e);
        }
    }

    //background updating
    @UiThread
    void publishResult(TrainsList trainsList){
        activity.updateTrainsLeft(trainsList);
    }

    //background updating when sth went soooo wrong
    @UiThread
    void publishError(Exception e){
        activity.showError(e);
    }

    @UiThread
    void publishWarning(){
        activity.showWarning();
    }


}
