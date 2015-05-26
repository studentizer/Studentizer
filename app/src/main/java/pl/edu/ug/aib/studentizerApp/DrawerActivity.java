package pl.edu.ug.aib.studentizerApp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.OptionsItem;

import java.util.List;

import pl.edu.ug.aib.studentizerApp.fragment.DashboardFragment;
import pl.edu.ug.aib.studentizerApp.fragment.TimetableFragment;
import pl.edu.ug.aib.studentizerApp.navigationDrawer.DrawerHandler;
import pl.edu.ug.aib.studentizerApp.skmTimetable.backgroundTasks.RestBackgroundTrainLeft;
import pl.edu.ug.aib.studentizerApp.skmTimetable.backgroundTasks.RestBackgroundTrainRight;
import pl.edu.ug.aib.studentizerApp.skmTimetable.data.TrainsList;

@EActivity(R.layout.activity_drawer)
public class DrawerActivity extends ActionBarActivity implements TimetableFragment.BgTask {

    //region implement the Background Task interface

    //communication activity -> fragment (also a RestBackgroundTrainLeft method)
    public void updateTrainsLeft(TrainsList trainsList){
        //TODO: use try...catch
        TimetableFragment timetableFragment = (TimetableFragment) drawerHandler.getFragment();

        if(timetableFragment != null){
            timetableFragment.updateTrainsLeft(trainsList);
        }
    }

    public void updateTrainsRight(TrainsList trainsList){
        //TODO: use try...catch
        TimetableFragment timetableFragment = (TimetableFragment) drawerHandler.getFragment();

        if(timetableFragment != null){
            timetableFragment.updateTrainsRight(trainsList);
        }
    }

    //communication fragment -> activity
    public void getBgTaskLeft(int startId, int endId, int hour){
        restBackgroundTrainLeft.getTrains(startId, endId, hour);
    }

    public void getBgTaskRight(int startId, int endId, int hour){
        restBackgroundTrainRight.getTrains(startId, endId, hour);
    }
    //endregion

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
    public void showError(Exception e){
        Toast.makeText(getApplicationContext(), "Błąd", Toast.LENGTH_SHORT).show();
        e.printStackTrace(); //debug
    }

    public void showWarning(){
        Toast.makeText(getApplicationContext(), "Stacja początkowa nie może być\nrówna stacji końcowej", Toast.LENGTH_SHORT).show();
    }
    //endregion

    //region ON ... methods
//    @Override
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.fragment_dashboard);
//
//        // Check that the activity is using the layout version with
//        // the fragment_container FrameLayout
//        if (findViewById(R.id.content_frame) != null){
//            // However, if we're being restored from a previous state,
//            // then we don't need to do anything and should return or else
//            // we could end up with overlapping fragments.
//            if (savedInstanceState != null) {
//                return;
//            }
//
//            // Create a new Fragment to be placed in the activity layout
//            DashboardFragment firstFragment = new DashboardFragment();
//
//            // In case this activity was started with special instructions from an
//            // Intent, pass the Intent's extras to the fragment as arguments
//            firstFragment.setArguments(getIntent().getExtras());
//
//            // Add the fragment to the 'fragment_container' FrameLayout
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.content_frame, firstFragment).commit();
//        }
//
//
//    }
    //endregion

}