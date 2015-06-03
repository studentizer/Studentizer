package pl.edu.ug.aib.studentizerApp.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;

import org.androidannotations.annotations.EFragment;

/**
 * Created by Patryk on 2015-06-03.
 */
@EFragment
public class LogoutFragment extends Fragment {

        OnLogoutFragmentCommunicationListener listener;
        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            try{
                listener = (OnLogoutFragmentCommunicationListener) activity;
                listener.logout();
            }
            catch (ClassCastException e){
                throw new ClassCastException(activity.toString() + " must implement OnLogoutFragmentCommunicationListener");
            }
        }

        public interface OnLogoutFragmentCommunicationListener{
            public void logout();
        }
    //
    }
