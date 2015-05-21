package pl.edu.ug.aib.studentizerApp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.ViewById;

import pl.edu.ug.aib.studentizerApp.R;
import pl.edu.ug.aib.studentizerApp.skmTimetable.RestBackgroundTrain;
import pl.edu.ug.aib.studentizerApp.skmTimetable.adapter.TrainsListAdapter;
import pl.edu.ug.aib.studentizerApp.skmTimetable.data.Train;
import pl.edu.ug.aib.studentizerApp.skmTimetable.data.TrainsList;

@EActivity(R.layout.fragment_timetable)
public class TimetableFragment extends ActionBarActivity {
    //json single output data model
    @Extra
    Train train;

    //my background task where is applied method from rest client interface
    @Bean
    @NonConfigurationInstance
    RestBackgroundTrain restBackgroundTrain;

    //adapter
    @Bean
    TrainsListAdapter adapter;

    //listview from fragment_timetable
    @ViewById
    ListView listTrain;
    @ViewById
    Button refreshBtn;
    @ViewById
    EditText startId;
    @ViewById
    EditText endId;

    @AfterViews
    void init() {
        //getActivity().setTitle(R.string.title_timetable);

        listTrain.setAdapter(adapter);
    }

    @Click
    void refreshBtnClicked(){
        restBackgroundTrain.getTrains(Integer.parseInt(startId.getText().toString()), Integer.parseInt(endId.getText().toString()));

    }

    public void updateTrains(TrainsList trainsList){
//        if(trainsList != null){
            adapter.update(trainsList);
//        }
    }

    public void showError(Exception e){
        e.printStackTrace(); //debug
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_timetable);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

}
