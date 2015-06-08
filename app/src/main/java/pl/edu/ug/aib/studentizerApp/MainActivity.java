package pl.edu.ug.aib.studentizerApp;

import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;

import pl.edu.ug.aib.studentizerApp.userData.Data.EmailAndPassword;
import pl.edu.ug.aib.studentizerApp.userData.Data.User;
import pl.edu.ug.aib.studentizerApp.userData.MainBackgroundTask;
import pl.edu.ug.aib.studentizerApp.userData.UserPreferences_;

/**
 * Created by Patryk on 2015-06-09.
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends ActionBarActivity {

    @Pref
    UserPreferences_ preferences;



    @Bean
    @NonConfigurationInstance
    MainBackgroundTask mainBackgroundTask;

    @AfterViews
    void init() {
        EmailAndPassword emailAndPassword = new EmailAndPassword();
        emailAndPassword.email = preferences.email().get();
        emailAndPassword.password = preferences.password().get();
        if (!emailAndPassword.email.isEmpty() && !emailAndPassword.password.isEmpty()) {
            mainBackgroundTask.login(emailAndPassword);
            finish();
        }
        else {
            UserActivity_.intent(this).start();
            finish();
        }
    }

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

    public void showError(Exception e){
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

    }

}
