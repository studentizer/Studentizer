package pl.edu.ug.aib.studentizerApp.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import pl.edu.ug.aib.studentizerApp.R;

/**
 * Created by Patryk on 2015-06-03.
 */
@EFragment(R.layout.fragment_user)
public class UserFragment extends Fragment{
    OnUserFragmentCommunicationListener listener;

    @AfterViews
    void init(){
//tutaj cos?
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            listener = (OnUserFragmentCommunicationListener) activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnIntroFragmentCommunicationListener");
        }
    }

    @Click
    void loginButtonClicked(){
        listener.onLoginClicked();
    }
    @Click
    void registerButtonClicked(){
        listener.onRegisterClicked();
    }

    public interface OnUserFragmentCommunicationListener {
        public void onLoginClicked();
        public void onRegisterClicked();
    }



}
