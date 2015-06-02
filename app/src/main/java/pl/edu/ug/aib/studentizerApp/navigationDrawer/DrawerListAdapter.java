package pl.edu.ug.aib.studentizerApp.navigationDrawer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import pl.edu.ug.aib.studentizerApp.R;
import pl.edu.ug.aib.studentizerApp.fragment.TODOFragment_;
import pl.edu.ug.aib.studentizerApp.fragment.TimetableFragment_;
import pl.edu.ug.aib.studentizerApp.fragment.DashboardFragment_;
import pl.edu.ug.aib.studentizerApp.fragment.WalletFragment_;

@EBean
public class DrawerListAdapter extends BaseAdapter {

    @RootContext
    Context context;

    List<DrawerItem> items = new ArrayList<DrawerItem>();

    @AfterInject
    void init() {
        items.clear();

        //Dodawanie do Drawera listy fragmentów
        items.add(new DrawerItem(R.string.title_dashboard, R.drawable.icon_home, DashboardFragment_.class));
        items.add(new DrawerItem(R.string.title_timetable, R.drawable.ic_directions_transit_white_24dp, TimetableFragment_.class));
        items.add(new DrawerItem(R.string.title_todo, R.drawable.icon_about, TODOFragment_.class));
        items.add(new DrawerItem(R.string.title_wallet, R.drawable.wallet_grey, WalletFragment_.class));
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DrawerItemView drawerItemView;
        if (convertView == null) {
            drawerItemView = DrawerItemView_.build(context);
        } else {
            drawerItemView = (DrawerItemView) convertView;
        }

        drawerItemView.bind(getItem(position));

        return drawerItemView;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public DrawerItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
