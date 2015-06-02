package pl.edu.ug.aib.studentizerApp.fragment;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import pl.edu.ug.aib.studentizerApp.R;
import pl.edu.ug.aib.studentizerApp.skmTimetable.gps.GeolocationService;
import pl.edu.ug.aib.studentizerApp.skmTimetable.gps.GeolocationUtilities;

@EFragment(R.layout.fragment_dashboard)
public class DashboardFragment extends Fragment {

    @ViewById(R.id.stanMiasto)
    TextView stanMiasto;

    @ViewById(R.id.stanPogoda)
    TextView stanPogoda;

    @ViewById(R.id.stanC)
    TextView stanC;

    //they have to be here (used also in overridden onPause method)
    GeolocationService geolocationService = new GeolocationService();
    GeolocationService.LocationResult locationResult;
    private double lat;
    private double lon;


    @AfterViews
    void init() {

        getActivity().setTitle(R.string.title_dashboard);

        locationResult = new GeolocationService.LocationResult(){
            @Override
            public void gotLocation(Location location){
                //Got the location!
                lat = location.getLatitude();
                lon = location.getLongitude();

                //toast with GPS accuracy
                Toast.makeText(getActivity().getApplicationContext(), "Znaleziono lokalizację", Toast.LENGTH_SHORT).show();
            }

        };


    }

    //avoid app-crashing during getting the GPS location
    @Override
    public void onPause(){
        //cancel the gps request timer to prevent app crashes during fragment switching
//        geolocationService.cancelTimer(getActivity(), locationResult);

        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();

//        geolocationService.cancelTimer(getActivity(), locationResult);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);



        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }
}
