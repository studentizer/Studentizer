package pl.edu.ug.aib.studentizerApp.fragment;

import android.location.Location;
import android.support.v4.app.Fragment;
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

    private double lat;
    private double lon;

    @ViewById(R.id.stanMiasto)
    TextView stanMiasto;

    @ViewById(R.id.stanPogoda)
    TextView stanPogoda;

    @ViewById(R.id.stanC)
    TextView stanC;

    GeolocationService geolocationService = new GeolocationService();
    GeolocationService.LocationResult locationResult;


    @AfterViews
    void init() {

        getActivity().setTitle(R.string.title_dashboard);

        locationResult = new GeolocationService.LocationResult(){
            @Override
        public void gotLocation(Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();




                Toast.makeText(getActivity(),"Znaleziono lokalizację", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onPause() {
        geolocationService.cancelTimer(getActivity(), locationResult);

        super.onPause();
    }
}
