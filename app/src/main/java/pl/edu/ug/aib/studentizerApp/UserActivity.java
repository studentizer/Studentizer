package pl.edu.ug.aib.studentizerApp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.sharedpreferences.Pref;

import pl.edu.ug.aib.studentizerApp.fragment.LoginFragment;
import pl.edu.ug.aib.studentizerApp.fragment.LoginFragment_;
import pl.edu.ug.aib.studentizerApp.fragment.RegisterFragment;
import pl.edu.ug.aib.studentizerApp.fragment.RegisterFragment_;
import pl.edu.ug.aib.studentizerApp.fragment.UserFragment;
import pl.edu.ug.aib.studentizerApp.fragment.UserFragment_;
import pl.edu.ug.aib.studentizerApp.userData.Data.EmailAndPassword;
import pl.edu.ug.aib.studentizerApp.userData.Data.User;
import pl.edu.ug.aib.studentizerApp.userData.Data.UserRegister;
import pl.edu.ug.aib.studentizerApp.userData.RestLoginBackgroundTask;
import pl.edu.ug.aib.studentizerApp.userData.UserPreferences_;

/**
 * Created by Patryk on 2015-06-02.
 */
@EActivity(R.layout.activity_user)
public class UserActivity extends ActionBarActivity implements UserFragment.OnUserFragmentCommunicationListener,
        LoginFragment.OnLoginFragmentCommunicationListener,
        RegisterFragment.OnRegisterFragmentCommunicationListener{

    private static final String INTRO_FRAGMENT_TAG = "user";
    @Pref
    UserPreferences_ preferences;
    @Bean
    @NonConfigurationInstance
    RestLoginBackgroundTask restBackgroundTask;

    FragmentManager fragmentManager;

    @AfterViews
    void init(){
        //load initial fragment
        fragmentManager = this.getSupportFragmentManager();
        UserFragment userFragment = new UserFragment_();
        fragmentManager.beginTransaction()
                .add(R.id.contentFrame, userFragment, INTRO_FRAGMENT_TAG)
                .commit();
        EmailAndPassword emailAndPassword = new EmailAndPassword();
        emailAndPassword.email = preferences.email().get();
        emailAndPassword.password = preferences.password().get();
        //if user credentials already saved in preferences, try to log in
        if(!emailAndPassword.email.isEmpty() && !emailAndPassword.password.isEmpty()){
            onLogin(emailAndPassword);
        }


    }
    //IntroFragment communication
    @Override
    public void onLoginClicked() {
        fragmentManager.beginTransaction()
                .replace(R.id.contentFrame, new LoginFragment_())
                .addToBackStack(null)
                .commit();
    }
    //IntroFragment communication
    @Override
    public void onRegisterClicked() {
        fragmentManager.beginTransaction()
                .replace(R.id.contentFrame, new RegisterFragment_())
                .addToBackStack(null)
                .commit();
    }
    //LoginFragment communication
    @Override
    public void onLogin(EmailAndPassword emailAndPassword) {
        restBackgroundTask.login(emailAndPassword);
    }
    //RegisterFragment communication
    @Override
    public void onRegister(UserRegister userRegister) {
        restBackgroundTask.register(userRegister);
    }

    //RestLoginBackgroundTask communication
    public void loginSuccess(User user, String password){
        //updates shared preferences with user data
        preferences.id().put(user.id);
        preferences.sessionId().put(user.sessionId);
        preferences.firstName().put(user.firstName);
        preferences.lastName().put(user.lastName);
        preferences.displayName().put(user.displayName);
        preferences.email().put(user.email);
        preferences.password().put(password);
        Toast.makeText(this, R.string.loginOk, Toast.LENGTH_LONG).show();
        //starts main activity
        DrawerActivity_.intent(this).start();
        finish();
    }
    //Invoked after login/register exception in rest background task
    public void showError(Exception e){
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

    }

    //asd
}