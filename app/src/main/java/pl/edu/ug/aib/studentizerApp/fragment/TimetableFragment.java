package pl.edu.ug.aib.studentizerApp.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Timer;

import pl.edu.ug.aib.studentizerApp.R;
import pl.edu.ug.aib.studentizerApp.skmTimetable.adapter.TrainsListAdapter;
import pl.edu.ug.aib.studentizerApp.skmTimetable.data.Station;
import pl.edu.ug.aib.studentizerApp.skmTimetable.data.StationsList;
import pl.edu.ug.aib.studentizerApp.skmTimetable.data.Train;
import pl.edu.ug.aib.studentizerApp.skmTimetable.data.TrainsList;
import pl.edu.ug.aib.studentizerApp.skmTimetable.gps.GeolocationService;
import pl.edu.ug.aib.studentizerApp.skmTimetable.gps.GeolocationUtilities;
import pl.edu.ug.aib.studentizerApp.skmTimetable.utilities.DirectionHelpers;

@EFragment(R.layout.fragment_timetable)
public class TimetableFragment extends Fragment {

    //region communication between fragments-activity (in both ways)
    BgTask mCallback; //call back from DrawerActivity
    Fragment fragment;

    //Container activity must implement that interface
    public interface BgTask{
        public void updateTrainsLeft(TrainsList trainsList);
        public void getBgTaskLeft(int startId, int endId, int hour);

        public void updateTrainsRight(TrainsList trainsList);
        public void getBgTaskRight(int startId, int endIt, int hour);

        public void sendSms(String str);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try{
            mCallback = (BgTask)activity;
        } catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + "must implement an interface!");
        }
    }
    //endregion

    //json single output data model
    //check if this is necessary: not necessary
    //@Extra
    //Train train;

    //region adapters
    @Bean
    TrainsListAdapter adapterLeft;
    @Bean
    TrainsListAdapter adapterRight;
    //endregion

    //region to get current hour
    Date date = new Date();
    Calendar calendar = GregorianCalendar.getInstance();
    int currentHour;
    //endregion

    //region Prepare value for spinner
    StationsList list = new StationsList();
    List<Station> allDirections = list.getAllDirections();
    List<Station> allStations = list.getAllStations();
    //endregion

    //region Hashmaps, Arrays for Spinners
    String[] endSpinnerArray = new String[ allDirections.size() ];
    String[] startSpinnerArray = new String [ allStations.size() ];
    HashMap<String, String> endSpinnerMap = new HashMap<String, String>();
    HashMap<String, String> startSpinnerMap = new HashMap<String, String>();
    //endregion

    //region listview from fragment_timetable
    @ViewById(R.id.listTrainLeftLstView)
    ListView listTrainLeftLstView;
    @ViewById(R.id.listTrainRightLstView)
    ListView listTrainRightLstView;
    //endregion

    //region UI controls (buttons, spinners, textviews, etc.
    @ViewById
    ImageButton refreshBtn;
    @ViewById(R.id.restoreGpsBtn)
    ImageButton restoreGpsBtn;
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
    //endregion

    ProgressDialog ringProgressDialog;

    //region GPS variables
    Station closestStation = new Station(); //used in geolocation service
    DirectionHelpers direcionHelpers = new DirectionHelpers();
    //they have to be here (used also in overridden onPause method)
    GeolocationService geolocationService = new GeolocationService();
    GeolocationService.LocationResult locationResult;

    private double lat;
    private double lon;
    //endregion

    @AfterViews
    void init() {
        getActivity().setTitle(R.string.title_timetable);

        //initialize current hour
        calendar.setTime(date);
        currentHour = calendar.get(Calendar.HOUR_OF_DAY) + 1;

        //Prepare values to a spinner
        prepareValuesToSpinner(this.allDirections, this.endSpinnerArray, this.endSpinnerMap);
        prepareValuesToSpinner(this.allStations, this.startSpinnerArray, this.startSpinnerMap);

        //Set values to a spinner
        setDataInSpinner(this.endIdSpinner, this.endSpinnerArray);
        setDataInSpinner(this.startIdSpinner, this.startSpinnerArray);

        //Get closest station
        locationResult = new GeolocationService.LocationResult(){
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

                //direction iteration before/after the closest station
                int directionAfterCurrentIter = direcionHelpers.getDirectionAfterCurrent(allStations, closestStation.id);
                int directionBeforeCurrentIter = direcionHelpers.getDirectionBeforeCurrent(allStations, closestStation.id);

                //REST background task for left listview
                if(directionBeforeCurrentIter > 0){
                    Station endStationLeft = direcionHelpers.getDirection(allStations, directionBeforeCurrentIter);
                    leftTxtView.setText("Kierunek:\n" + endStationLeft.name);
                    mCallback.getBgTaskLeft(closestStation.id, endStationLeft.id, currentHour);
                }
                //REST background task for right listview
                if(directionAfterCurrentIter > 0){
                    Station endStationRight = direcionHelpers.getDirection(allStations, directionAfterCurrentIter);
                    rightTxtView.setText("Kierunek:\n" + endStationRight.name);
                    mCallback.getBgTaskRight(closestStation.id, endStationRight.id, currentHour);
                }
                //endregion

                //toast with GPS accuracy
                Toast.makeText(getActivity().getApplicationContext(),
                        "Znaleziono najbliższą stację\nDokładność GPS: " + String.valueOf(Math.round(location.getAccuracy())) + " m",
                        Toast.LENGTH_SHORT).show();
            }

        };

        //launch overridden method
        geolocationService.getLocation(getActivity(), locationResult);
    }



    //region Spinner methods
    private int getSelectedPosition(String[] dataArray, String stationName){
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, dataArray);

        return spinnerArrayAdapter.getPosition(stationName);
    }

    private void prepareValuesToSpinner(List<Station> stations, String[] spinnerArray, HashMap<String, String> spinnerMap){
        for(int i = 0; i < stations.size(); i++){
            spinnerMap.put(Integer.toString(stations.get(i).id), stations.get(i).name);
            spinnerArray[i] = stations.get(i).name;
        }
    }

    private void setDataInSpinner(Spinner spinner, String[] dataArray) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, dataArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
        ringProgressDialog.setMessage("Ładowanie rozkładu...");
        ringProgressDialog.show();

        //delete linear layout at the right side
        rightLayout.setVisibility(View.GONE);

        String endId = getDataFromSpinner(this.endIdSpinner, this.endSpinnerMap);
        String startId = getDataFromSpinner(this.startIdSpinner, this.startSpinnerMap);

        //get value from lower spinner
        String name = getDataFromSpinner(endIdSpinner, endSpinnerMap);
        int iter = direcionHelpers.getDirectionIteration(allStations, Integer.valueOf(name));
        leftTxtView.setText("Kierunek:\n" + direcionHelpers.getDirection(allStations, iter).name);

        mCallback.getBgTaskLeft(Integer.parseInt(startId), Integer.parseInt(endId), currentHour);
    }

    @Click
    void restoreGpsBtn(){
        //set progress dialog
        ringProgressDialog.setMessage("Ładowanie rozkładu...");
        ringProgressDialog.show();

        //show deleted linear layout at the right side
        rightLayout.setVisibility(View.VISIBLE);

        //run the init method with geolocation service
        init();

    }
    //endregion

    //region BACKGROUND TASK METHODS
    public void updateTrainsLeft(TrainsList trainsList){
        if(trainsList != null){
            listTrainLeftLstView.setAdapter(adapterLeft);
            adapterLeft.update(trainsList);
            Toast.makeText(getActivity(), "Zaktualizowano rozkład jazdy", Toast.LENGTH_SHORT).show();


            //set listview on long item pressed
//            listTrainLeftLstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    //get item at position (gets whole class)
//                    Train selectedItem = (Train) adapterView.getItemAtPosition(i);
//                    String selectedHour = selectedItem.hour;
//
//                    Toast.makeText(getActivity().getApplicationContext(), "Elo!" + selectedHour, Toast.LENGTH_SHORT).show();
//                }
//            });

            listTrainLeftLstView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
               @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l){
                    Toast.makeText(getActivity().getApplicationContext(), "Wysyłanie SMS z rozkładem jazdy...", Toast.LENGTH_SHORT).show();

                   //selected stations
                   String stationFrom = startIdSpinner.getSelectedItem().toString();

                   //selected item
                   Train selectedItem = (Train) adapterView.getItemAtPosition(i);
                   String selectedTime = selectedItem.hour + ":" + selectedItem.minute;
                   String direction = selectedItem.tip;

                   //send sms (communication fragment -> activity)
                   mCallback.sendSms("SKM ze stacji " + stationFrom + " w kierunku " + direction + " odjeżdża o " + selectedTime + ".");

                   return true;
               }
            });
        }

        if(ringProgressDialog.isShowing())
            ringProgressDialog.dismiss();
    }

    public void updateTrainsRight(TrainsList trainsList){
        if(trainsList != null) {
            listTrainRightLstView.setAdapter(adapterRight);
            adapterRight.update(trainsList);

            listTrainRightLstView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l){
                    Toast.makeText(getActivity().getApplicationContext(), "Wysyłanie SMS z rozkładem jazdy...", Toast.LENGTH_SHORT).show();

                    //selected stations
                    String stationFrom = startIdSpinner.getSelectedItem().toString();

                    //selected item
                    Train selectedItem = (Train) adapterView.getItemAtPosition(i);
                    String selectedTime = selectedItem.hour + ":" + selectedItem.minute;
                    String direction = selectedItem.tip;

                    //send sms (communication fragment -> activity)
                    mCallback.sendSms("SKM ze stacji " + stationFrom + " w kierunku " + direction + " odjeżdża o " + selectedTime + ".");

                    return true;
                }
            });
        }

        if(ringProgressDialog.isShowing())
            ringProgressDialog.dismiss();
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

    //region ON... methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        //launch ProgressDialog
        ringProgressDialog = new ProgressDialog(getActivity());
        ringProgressDialog.setMessage("Ładowanie rozkładów\nz najbliższej stacji");
        ringProgressDialog.setIndeterminate(true);

        if(!ringProgressDialog.isShowing())
            ringProgressDialog.show();

        return inflater.inflate(R.layout.fragment_timetable, container, false);
    }

    //avoid app-crashing during getting the GPS location
    @Override
    public void onPause(){
        //cancel the gps request timer to prevent app crashes during fragment switching
        geolocationService.cancelTimer(getActivity(), locationResult);

        super.onPause();
    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//
//        geolocationService.cancelTimer(getActivity(), locationResult);
//    }

    //endregion

}
