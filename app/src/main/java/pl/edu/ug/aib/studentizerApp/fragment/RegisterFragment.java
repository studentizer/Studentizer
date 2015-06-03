package pl.edu.ug.aib.studentizerApp.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import pl.edu.ug.aib.studentizerApp.R;
import pl.edu.ug.aib.studentizerApp.userData.Data.UserRegister;

/**
 * Created by Patryk on 2015-06-03.
 */
@EFragment(R.layout.fragment_register)
public class RegisterFragment extends Fragment {

    @ViewById
    Button register;
    @ViewById
    EditText email;
    @ViewById
    EditText passworD;
    @ViewById
    EditText firstName;
    @ViewById
    EditText lastName;
    @ViewById
    EditText displayName;
    //interface for communication with activity
    OnRegisterFragmentCommunicationListener listener;

    ProgressDialog ringProgressDialog;

    @AfterViews
    void init(){
        ringProgressDialog = new ProgressDialog(getActivity());
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            listener = (OnRegisterFragmentCommunicationListener) activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnRegisterFragmentCommunicationListener");
        }
    }

    @Click(R.id.register)
    void registerClicked(){
        ringProgressDialog.show();
        ringProgressDialog.setMessage("Logowanie...");
        ringProgressDialog.setIndeterminate(true);
        //registration data verification
        if(email.getText().toString().isEmpty()){
            ringProgressDialog.dismiss();
            Toast.makeText(getActivity(), R.string.missEmail, Toast.LENGTH_LONG).show();
            return;
        }
        //password verification
        String password = passworD.getText().toString();
        if(password.isEmpty()){
            ringProgressDialog.dismiss();
            Toast.makeText(getActivity(), R.string.misspass, Toast.LENGTH_LONG).show();
            return;
        }
        //creates object with registration data and passes to activity
        UserRegister userRegister = new UserRegister();
        userRegister.email = email.getText().toString();
        userRegister.password = password;
        userRegister.firstName = firstName.getText().toString();
        userRegister.lastName = lastName.getText().toString();
        userRegister.displayName = displayName.getText().toString();
        listener.onRegister(userRegister);
    }

    public interface OnRegisterFragmentCommunicationListener {
        void onRegister(UserRegister userRegister);
    }


}
