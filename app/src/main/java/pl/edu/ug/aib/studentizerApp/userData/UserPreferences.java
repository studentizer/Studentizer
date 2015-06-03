package pl.edu.ug.aib.studentizerApp.userData;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(value = SharedPref.Scope.APPLICATION_DEFAULT)
public interface UserPreferences {

    //UserData
    @DefaultInt(0)
    int id();
    @DefaultString("")
    String sessionId();
    @DefaultString("")
    String displayName();
    @DefaultString("")
    String firstName();
    @DefaultString("")
    String lastName();
    @DefaultString("")
    String email();
    @DefaultString("")
    String password();
}