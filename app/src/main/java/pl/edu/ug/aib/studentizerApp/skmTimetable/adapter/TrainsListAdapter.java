package pl.edu.ug.aib.studentizerApp.skmTimetable.adapter;

import android.content.Context;
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

    List<Train> trains = new ArrayList<Train>();

    public TrainsListAdapter(){

    }

    @Override
    public int getCount() {
        return trains.size();
    }

    @Override
    public Train getItem(int i) {
        return trains.get(i);
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

        trainItemView.bind(getItem(position));

        return trainItemView;
    }

    public void update(TrainsList trains) {
        //TODO: check it, probably sth went wrong
        trains.records.clear();
        trains.records.addAll(trains.records);
        this.notifyDataSetChanged();
    }
}
