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
import pl.edu.ug.aib.studentizerApp.userData.Data.EmailAndPassword;

/**
 * Created by Patryk on 2015-06-03.
 */
@EFragment(R.layout.fragment_login)
public class LoginFragment extends Fragment{

    @ViewById
    Button login;

    @ViewById
    EditText email;

    @ViewById
    EditText password;

    OnLoginFragmentCommunicationListener listener;

    ProgressDialog ringProgressDialog;

    @AfterViews
    void init(){
        ringProgressDialog = new ProgressDialog(getActivity());
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            listener = (OnLoginFragmentCommunicationListener) activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnLoginFragmentCommunicationListener");
        }
    }

    @Click(R.id.login)
    void loginClicked(){
        ringProgressDialog.show();
        ringProgressDialog.setMessage("Logowanie...");
        ringProgressDialog.setIndeterminate(true);
        //email and password verification
        if(email.getText().toString().isEmpty()){
            ringProgressDialog.dismiss();
            Toast.makeText(getActivity(), R.string.missEmail, Toast.LENGTH_LONG).show();
            return;
        }
        if(password.getText().toString().isEmpty()){
            ringProgressDialog.dismiss();
            Toast.makeText(getActivity(),R.string.misspass, Toast.LENGTH_LONG).show();
            return;
        }
        //creates object with login data and passes to activity
        EmailAndPassword emailAndPassword = new EmailAndPassword();
        emailAndPassword.email = email.getText().toString();
        emailAndPassword.password = password.getText().toString();
        listener.onLogin(emailAndPassword);
    }

    public interface OnLoginFragmentCommunicationListener {
        void onLogin(EmailAndPassword emailAndPassword);
    }



}
