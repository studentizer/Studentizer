package pl.edu.ug.aib.studentizerApp.skmTimetable.itemView;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import pl.edu.ug.aib.studentizerApp.R;
import pl.edu.ug.aib.studentizerApp.skmTimetable.data.Train;

/**
 * Created by Konrad on 19.05.2015.
 */

@EViewGroup(R.layout.list_train)
public class TrainItemView extends RelativeLayout {
    //    "timetableId": "30x33x19",
//            "IdStart": "30",
//            "IdEnd": "33",
//            "TimetableDate": "2015-05-19",
//            "Hour": "03:",
//            "Minute": "12",
//            "Tip": "W1"

    //use fields from list_train layout
//    @ViewById
//    TextView idStart;
//    @ViewById
//    TextView idEnd;
//    @ViewById
//    TextView timetableDate;
    @ViewById
    TextView hour;
    @ViewById
    TextView minute;
    @ViewById
    TextView tip;

    public TrainItemView(Context context){
        super(context);
    }

    public void bind(Train train){
        //fill textviews
//        idStart.setText("idStart: " + Integer.toString(train.idStart));
//        idEnd.setText(" idEnd: " + Integer.toString(train.idEnd));
//        timetableDate.setText(train.timetableDate.toString());
        hour.setText(train.hour + ":");
        minute.setText(train.minute);
        tip.setText(train.tip);
    }

}
