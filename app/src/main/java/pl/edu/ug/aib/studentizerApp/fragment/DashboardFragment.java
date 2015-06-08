package pl.edu.ug.aib.studentizerApp.fragment;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import pl.edu.ug.aib.studentizerApp.R;
import pl.edu.ug.aib.studentizerApp.Wallet.data.Transaction;
import pl.edu.ug.aib.studentizerApp.Wallet.data.Wallet;
import pl.edu.ug.aib.studentizerApp.skmTimetable.gps.GeolocationService;
import pl.edu.ug.aib.studentizerApp.userData.Data.User;
import pl.edu.ug.aib.studentizerApp.userData.UserPreferences_;

@EFragment(R.layout.fragment_dashboard)
public class DashboardFragment extends Fragment {

    FragmentManager fragmentManager;

    OnDashboardFragmentCommunicationListener listener;

    Transaction transaction;

    Wallet wallet;

    @Pref
    UserPreferences_ preferences;

    User user;

    @ViewById
    TextView hello;

    @ViewById(R.id.stanMiasto)
    TextView stanMiasto;

    @ViewById(R.id.stanPogoda)
    TextView stanPogoda;

    @ViewById(R.id.stanC)
    TextView stanC;

    @ViewById(R.id.saldo)
    TextView saldo;

        //they have to be here (used also in overridden onPause method)
    GeolocationService geolocationService = new GeolocationService();
    GeolocationService.LocationResult locationResult;
    private double lat;
    private double lon;
    private FragmentManager supportFragmentManager;


    @AfterViews
    void init() {
        getActivity().setTitle(R.string.title_dashboard);
        listener.getUser();
        getUserInfo();

        preferences.saldokonta().get();
        if (preferences.saldokonta().get().isEmpty()||preferences.saldokonta().get().equals(".00")) {
            saldo.setText("0.00zł");
        } else {
            saldo.setText(preferences.saldokonta().get().toString() + "zł");
        }

        locationResult = new GeolocationService.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                //Got the location!
                lat = location.getLatitude();
                lon = location.getLongitude();

                //toast with GPS accuracy
                Toast.makeText(getActivity().getApplicationContext(), "Znaleziono lokalizację", Toast.LENGTH_SHORT).show();
            }

        };
    }


    public void getUserInfo(){
        if (preferences.sessionId().get().isEmpty()) {
            hello.setText("Witaj nieznajomy!");
        } else {
            hello.setText("Witaj " + preferences.displayName().get() + "!");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            listener = (OnDashboardFragmentCommunicationListener) activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnDashboardFragmentCommunicationListener");
        }
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

    public FragmentManager getSupportFragmentManager() {
        return supportFragmentManager;
    }

    public interface OnDashboardFragmentCommunicationListener {
        public User getUser();
    }

    private class TimeFormat {
    }
}
