package pl.edu.ug.aib.studentizerApp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.OptionsItem;

import java.util.List;

import pl.edu.ug.aib.studentizerApp.fragment.TimetableFragment;
import pl.edu.ug.aib.studentizerApp.navigationDrawer.DrawerHandler;
import pl.edu.ug.aib.studentizerApp.skmTimetable.backgroundTasks.RestBackgroundTrainLeft;
import pl.edu.ug.aib.studentizerApp.skmTimetable.backgroundTasks.RestBackgroundTrainRight;
import pl.edu.ug.aib.studentizerApp.skmTimetable.data.Train;
import pl.edu.ug.aib.studentizerApp.skmTimetable.data.TrainsList;

@EActivity(R.layout.activity_drawer)
public class DrawerActivity extends ActionBarActivity implements TimetableFragment.OnBgTaskListener {


    private TimetableFragment mTimetableFragment;


    public void onBgTaskLeftLaunched(){ //TrainsList trainsListLeft){
        //Do sth here to display
        TimetableFragment timetableFragment = (TimetableFragment)getSupportFragmentManager().findFragmentById(R.id.timetableFragment);

//            if (trainsListLeft != null)
//                timetableFragment.updateTrainsLeft(trainsListLeft);
        timetableFragment.setString("dupa");


    }

    public void onBgTaskRightLaunched(TrainsList trainsListRight){
        //Do sth here to display
        TimetableFragment timetableFragment = (TimetableFragment)getSupportFragmentManager().findFragmentById(R.id.timetableFragment);

        if(timetableFragment != null) {
            if (trainsListRight != null)
                timetableFragment.updateTrainsLeft(trainsListRight);
        }

    }

    //my background task where is applied method from rest client interface
    @Bean
    @NonConfigurationInstance
    RestBackgroundTrainLeft restBackgroundTrainLeft;
    @Bean
    @NonConfigurationInstance
    RestBackgroundTrainRight restBackgroundTrainRight;

    public final String LOG_TAG = this.getClass().getSimpleName();

    @Bean
    DrawerHandler drawerHandler;

    @AfterViews
    void init() {
        drawerHandler.init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerHandler.getDrawerToggle().syncState();
    }

    @OptionsItem(android.R.id.home)
    public boolean drawerToggleSelected(MenuItem item) {
        return drawerHandler.drawerToggleSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void setTitle(int titleId) {
        getSupportActionBar().setTitle(titleId);
    }


    //region BACKGROUND TASK METHODS
    public void updateTrainsLeft(TrainsList trainsList){

    }

    public void updateTrainsRight(TrainsList trainsList){

    }

    public void showError(Exception e){
        Toast.makeText(getApplicationContext(), "Błąd", Toast.LENGTH_SHORT).show();
        e.printStackTrace(); //debug
    }

    public void showWarning(){
        Toast.makeText(getApplicationContext(), "Stacja początkowa nie może być\nrówna stacji końcowej", Toast.LENGTH_SHORT).show();
    }
    //endregion

}