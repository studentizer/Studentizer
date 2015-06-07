package pl.edu.ug.aib.studentizerApp.skmTimetable.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import pl.edu.ug.aib.studentizerApp.skmTimetable.data.Train;
import pl.edu.ug.aib.studentizerApp.skmTimetable.data.TrainsList;
import pl.edu.ug.aib.studentizerApp.skmTimetable.itemView.TrainItemView;
import pl.edu.ug.aib.studentizerApp.skmTimetable.itemView.TrainItemView_;

/**
 * Created by Konrad on 19.05.2015.
 */

@EBean
public class TrainsListAdapter  extends BaseAdapter {

    @RootContext
    Context context;

    List<Train> trainsTimetable = new ArrayList<Train>();

    public TrainsListAdapter(){

    }

    @Override
    public int getCount() {
        return trainsTimetable.size();
    }

    @Override
    public Train getItem(int i) {
        return trainsTimetable.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TrainItemView trainItemView;
        if (convertView == null) {
            trainItemView = TrainItemView_.build(context);
        } else {
            trainItemView = (TrainItemView) convertView;
        }

        if(position % 2 == 0) {
            trainItemView.setBackgroundColor(Color.parseColor("#9E9E9E"));
        } else{
            trainItemView.setBackgroundColor(Color.parseColor("#424242"));
        }



        trainItemView.bind(getItem(position));


        return trainItemView;
    }

    public void update(TrainsList trains) {
        trainsTimetable.clear();
        trainsTimetable.addAll(trains.records);
        this.notifyDataSetChanged();
    }
}
