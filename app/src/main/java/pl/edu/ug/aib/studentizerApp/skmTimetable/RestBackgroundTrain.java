package pl.edu.ug.aib.studentizerApp.skmTimetable;

import android.content.Context;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;

import pl.edu.ug.aib.studentizerApp.DrawerActivity;
import pl.edu.ug.aib.studentizerApp.FragmentActivity;
import pl.edu.ug.aib.studentizerApp.fragment.TimetableFragment;
import pl.edu.ug.aib.studentizerApp.skmTimetable.data.TrainsList;

/**
 * Created by Konrad on 19.05.2015.
 */
@EBean
public class RestBackgroundTrain {
    @RootContext
    TimetableFragment activity; //activity with listview
    @RestService
    SkmRestClient restClient; //now I'm using my awesome API

    //load trains timetable without blocking main thread
    //because it's so annoying :)
    @Background
    public void getTrains(int start, int end){
        try{
            TrainsList trainsList = restClient.getTrains(start, end);
            publishResult(trainsList);
        } catch(Exception e){
            publishError(e);
        }
    }

    //background updating
    @UiThread
    void publishResult(TrainsList trainsList){
        activity.updateTrains(trainsList);
    }

    //background updating when sth went soooo wrong
    @UiThread
    void publishError(Exception e){
        activity.showError(e);
    }


}
