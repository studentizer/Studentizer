package pl.edu.ug.aib.studentizerApp.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import pl.edu.ug.aib.studentizerApp.R;
import pl.edu.ug.aib.studentizerApp.skmTimetable.backgroundTasks.RestBackgroundTrainLeft;
import pl.edu.ug.aib.studentizerApp.skmTimetable.adapter.TrainsListAdapter;
import pl.edu.ug.aib.studentizerApp.skmTimetable.backgroundTasks.RestBackgroundTrainRight;
import pl.edu.ug.aib.studentizerApp.skmTimetable.data.Station;
import pl.edu.ug.aib.studentizerApp.skmTimetable.data.StationsList;
import pl.edu.ug.aib.studentizerApp.skmTimetable.data.Train;
import pl.edu.ug.aib.studentizerApp.skmTimetable.data.TrainsList;
import pl.edu.ug.aib.studentizerApp.skmTimetable.gps.GeolocationService;
import pl.edu.ug.aib.studentizerApp.skmTimetable.gps.GeolocationUtilities;
import pl.edu.ug.aib.studentizerApp.skmTimetable.utilities.DirectionHelpers;

@EFragment(R.layout.fragment_timetable)
public class TimetableFragment extends Fragment {
    OnBgTaskListener mCallback;

    //Container activity must implement that interface
    public interface OnBgTaskListener{
        public void onBgTaskLeftLaunched();//TrainsList trainsListLeft);
        public void onBgTaskRightLaunched(TrainsList trainsListRight);
    }

    //json single output data model
//    @Extra
    Train train;

    //adapter
    @Bean
    TrainsListAdapter adapterLeft;
    @Bean
    TrainsListAdapter adapterRight;

    //to get current hour
    Date date = new Date();
    Calendar calendar = GregorianCalendar.getInstance();
    int currentHour;


    //Prepare value for spinner
    StationsList list = new StationsList();
    List<Station> allDirections = list.getAllDirections();
    List<Station> allStations = list.getAllStations();

    //Hashmaps, Arrays for Spinners
    String[] endSpinnerArray = new String[ allDirections.size() ];
    String[] startSpinnerArray = new String [ allStations.size() ];
    HashMap<String, String> endSpinnerMap = new HashMap<String, String>();
    HashMap<String, String> startSpinnerMap = new HashMap<String, String>();

    Station closestStation = new Station(); //used in geolocation service
    DirectionHelpers direcionHelpers = new DirectionHelpers();

    //listview from fragment_timetable
    @ViewById(R.id.listTrainLeftLstView)
    ListView listTrainLeftLstView;
    @ViewById(R.id.listTrainRightLstView)
    ListView listTrainRightLstView;

    @ViewById
    Button refreshBtn;
    @ViewById
    Spinner startIdSpinner;
    @ViewById
    Spinner endIdSpinner;
    @ViewById
    TextView text;
    @ViewById
    TextView leftTxtView;
    @ViewById
    TextView rightTxtView;
    @ViewById
    LinearLayout leftLayout;
    @ViewById
    LinearLayout rightLayout;

    ProgressDialog ringProgressDialog;

    public double lat;
    public double lon;

    public void setString(String str){
        leftTxtView.setText(str);
    }

    @AfterViews
    void init() {
        //initialize current hour
        calendar.setTime(date);
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        //Prepare values to a spinner
        prepareValuesToSpinner(this.allDirections, this.endSpinnerArray, this.endSpinnerMap);
        prepareValuesToSpinner(this.allStations, this.startSpinnerArray, this.startSpinnerMap);

        //Set values to a spinner
        setDataInSpinner(this.endIdSpinner, this.endSpinnerArray);
        setDataInSpinner(this.startIdSpinner, this.startSpinnerArray);

        //Get closest station
        GeolocationService.LocationResult locationResult = new GeolocationService.LocationResult(){
            @Override
            public void gotLocation(Location location){
                //Got the location!
                lat = location.getLatitude();
                lon = location.getLongitude();

                //initialize class with geolocation utilities
                GeolocationUtilities geolocationUtilities = new GeolocationUtilities();

                //get closest station of class Station
                closestStation = geolocationUtilities.getClosestStation(lat, lon);

                //set spinner with starting station
                int spinnerPosition = getSelectedPosition(startSpinnerArray, closestStation.name);
                startIdSpinner.setSelection(spinnerPosition, true);

                //set adapter
                listTrainLeftLstView.setAdapter(adapterLeft);
                listTrainRightLstView.setAdapter(adapterRight);

                //region fill listviews with timetable from closest station to closest directions

                //direction iteration before/after the closest station
                int directionAfterCurrentIter = direcionHelpers.getDirectionAfterCurrent(allStations, closestStation.id);
                int directionBeforeCurrentIter = direcionHelpers.getDirectionBeforeCurrent(allStations, closestStation.id);

                //REST background task for left listview
                if(directionBeforeCurrentIter > 0){
                    Station endStationLeft = direcionHelpers.getDirection(allStations, directionBeforeCurrentIter);
                    leftTxtView.setText("Kierunek:\n" + endStationLeft.name);
                    //restBackgroundTrainLeft.getTrains(closestStation.id, endStationLeft.id, currentHour);
                }
                //REST background task for right listview
                if(directionAfterCurrentIter > 0){
                    Station endStationRight = direcionHelpers.getDirection(allStations, directionAfterCurrentIter);
                    rightTxtView.setText("Kierunek:\n" + endStationRight.name);
                    //restBackgroundTrainRight.getTrains(closestStation.id, endStationRight.id, currentHour);
                }
                //endregion


                //toast with GPS accuracy
                Toast.makeText(getActivity().getApplicationContext(),
                        "Znaleziono najbliższą stację\nDokładność GPS: " + String.valueOf(Math.round(location.getAccuracy())) + " m",
                        Toast.LENGTH_SHORT).show();
            }
        };

        GeolocationService geolocationService = new GeolocationService();
        geolocationService.getLocation(getActivity().getApplicationContext(), locationResult);

    }



    //region Spinner methods
    private int getSelectedPosition(String[] dataArray, String stationName){
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, dataArray);

        return spinnerArrayAdapter.getPosition(stationName);
    }

    private void prepareValuesToSpinner(List<Station> stations, String[] spinnerArray, HashMap<String, String> spinnerMap){
        for(int i = 0; i < stations.size(); i++){
            spinnerMap.put(Integer.toString(stations.get(i).id), stations.get(i).name);
            spinnerArray[i] = stations.get(i).name;
        }
    }

    private void setDataInSpinner(Spinner spinner, String[] dataArray) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, dataArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        spinner.setAdapter(spinnerArrayAdapter);
        spinnerArrayAdapter.notifyDataSetChanged();
    }

    private String getDataFromSpinner(Spinner spinner, HashMap spinnerMap){
        String name = spinner.getSelectedItem().toString();
        String id = "NotFound";

        //Get the keys
        Set<String> keys = spinnerMap.keySet();
        //Loop through the keys
        for(String key : keys){
            //Match found -> finish the loop
            if(spinnerMap.get(key) == name){
                id = key;
                break;
            }
        }

        return id;
    }
    //endregion

    //region CONTROLS METHODS (e.g. button clicks)
    @Click
    void refreshBtnClicked(){
        //set progress dialog
//        ringProgressDialog.setMessage("Ładowanie rozkładu...");
//        ringProgressDialog.show();
//
//        //delete linear layout at the right side
//        rightLayout.setVisibility(View.GONE);
//
//        String endId = getDataFromSpinner(this.endIdSpinner, this.endSpinnerMap);
//        String startId = getDataFromSpinner(this.startIdSpinner, this.startSpinnerMap);
//
//        //get value from lower spinner
//        String name = getDataFromSpinner(endIdSpinner, endSpinnerMap);
//        int iter = direcionHelpers.getDirectionIteration(allStations, Integer.valueOf(name));
//        leftTxtView.setText("Kierunek:\n" + direcionHelpers.getDirection(allStations, iter).name);

        //restBackgroundTrainLeft.getTrains(Integer.parseInt(startId), Integer.parseInt(endId), currentHour);
        mCallback.onBgTaskLeftLaunched();

    }
    //endregion

    //region BACKGROUND TASK METHODS
    public void updateTrainsLeft(TrainsList trainsList){
        if(trainsList != null){
            adapterLeft.update(trainsList);
        }

        if(ringProgressDialog.isShowing())
            ringProgressDialog.dismiss();
    }

    public void updateTrainsRight(TrainsList trainsList){
        if(trainsList != null) {
            adapterRight.update(trainsList);
        }

//        if(ringProgressDialog.isShowing())
//            ringProgressDialog.dismiss();
    }

    public void showError(Exception e){
        ringProgressDialog.dismiss();

        Toast.makeText(getActivity().getApplicationContext(), "Błąd", Toast.LENGTH_SHORT).show();
        e.printStackTrace(); //debug
    }

    public void showWarning(){
        ringProgressDialog.dismiss();

        Toast.makeText(getActivity().getApplicationContext(), "Stacja początkowa nie może być\nrówna stacji końcowej", Toast.LENGTH_SHORT).show();
    }
    //endregion


    //region ON... METHODS
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //setContentView(R.layout.fragment_timetable);
//
//    }
////    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////        // Inflate the menu; this adds items to the action bar if it is present.
////        //getMenuInflater().inflate(R.menu.login, menu);
////        return true;
////    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //launch ProgressDialog
//        ringProgressDialog = new ProgressDialog(getActivity().getApplicationContext());
//        ringProgressDialog.setMessage("Ładowanie rozkładów\nz najbliższej stacji");
//        ringProgressDialog.setIndeterminate(true);
//
//        if(!ringProgressDialog.isShowing())
//            ringProgressDialog.show();

        return inflater.inflate(R.layout.fragment_timetable, container, false);
    }


    //avoid app-crashing during getting the GPS location
    @Override
    public void onPause(){
        super.onPause();
        GeolocationService geolocationService = new GeolocationService();
        geolocationService.cancelTimer();
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnBgTaskListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnBgTaskListener");
        }
    }


    //endregion
}
