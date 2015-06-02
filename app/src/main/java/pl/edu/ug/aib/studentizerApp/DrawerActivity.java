package pl.edu.ug.aib.studentizerApp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
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
    Fragment fragment;
    FragmentManager fragmentManager;

    //region implement the Background Task interface

    //communication activity -> fragment (also a RestBackgroundTrainLeft method)
    public void updateTrainsLeft(TrainsList trainsList){
        //TODO: use try...catch
//        TimetableFragment timetableFragment = (TimetableFragment) drawerHandler.getFragment();
        TimetableFragment timetableFragment = (TimetableFragment) fragment;


        if(timetableFragment != null){
            timetableFragment.updateTrainsLeft(trainsList);
        }
    }

    public void updateTrainsRight(TrainsList trainsList){
        //TODO: use try...catch
//        TimetableFragment timetableFragment = (TimetableFragment) drawerHandler.getFragment();
        TimetableFragment timetableFragment = (TimetableFragment) fragment;


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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
//    }

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
    static final int SMS_REQUEST = 1;  // The request code



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case SMS_REQUEST:
                if(resultCode == Activity.RESULT_OK){

                }

                break;

            default:
                break;
        }
    }


    public void sendSms(String str){
        Uri uri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        //intent.putExtra("sms_body", "SKM ze stacji " + stationFrom + " do stacji " + leftTxtView.getText().toString() +
        //        " odjeżdża o " + selectedTime + ".");
        intent.putExtra("sms_body", str);
        //intent.putExtra("exit_on_sent", true);

        startActivityForResult(intent, SMS_REQUEST);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_timetable, menu);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == R.id.action_help)
//                Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT).show();
//
//
//        return true;
//    }

    //endregion

}