package pl.edu.ug.aib.studentizerApp.fragment;

import android.support.v4.app.Fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import pl.edu.ug.aib.studentizerApp.R;

@EFragment(R.layout.fragment_dashboard)
public class DashboardFragment extends Fragment {

    @AfterViews
    void init() {

        getActivity().setTitle(R.string.title_dashboard);
    }

}
