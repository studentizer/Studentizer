package pl.edu.ug.aib.studentizerApp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.ViewById;

import pl.edu.ug.aib.studentizerApp.userData.Data.EmailAndPassword;
import pl.edu.ug.aib.studentizerApp.userData.Data.User;
import pl.edu.ug.aib.studentizerApp.userData.restBackgroundTasks.RestLoginBackgroundTask;
import pl.edu.ug.aib.studentizerApp.userData.restBackgroundTasks.RestRegisterBackgroundTask;

/**
 * Created by Patryk on 2015-06-02.
 */
@EActivity(R.layout.activity_user)
public class UserActivity extends ActionBarActivity {

    @Extra("User")
    User user;

    @Bean
    @NonConfigurationInstance
    RestLoginBackgroundTask restLoginBackgroundTask;

    @Bean
    @NonConfigurationInstance
    RestRegisterBackgroundTask restRegisterBackgroundTask;


    @ViewById
    EditText email;

    @ViewById
    EditText email2;

    @ViewById
    EditText password;

    @ViewById
    EditText password2;

    ProgressDialog ringProgressDialog;

    @AfterViews
    void init() {
    }

    @Click(R.id.login)
    void loginClicked()    {
        ringProgressDialog = new ProgressDialog(this);
        ringProgressDialog.setMessage("Logowanie...");
        ringProgressDialog.setIndeterminate(true);
        ringProgressDialog.show();
        EmailAndPassword emailAndPassword = new EmailAndPassword();
        emailAndPassword.email = email.getText().toString(); //"example@example.com";
        emailAndPassword.password = password.getText().toString(); //"test00";

        if(email.getText().length()==0||password.getText().length()==0)
        {
            ringProgressDialog.dismiss();
            Toast.makeText(this, "Wype³nij wszystkie pola!", Toast.LENGTH_LONG).show();
        }
        else{
            restLoginBackgroundTask.login(emailAndPassword);
        }
    }

    @Click(R.id.register)
    void registerClicked() {
        ringProgressDialog = new ProgressDialog(this);
        ringProgressDialog.setMessage("Rejestrowanie...");
        ringProgressDialog.setIndeterminate(true);
        ringProgressDialog.show();
        EmailAndPassword emailAndPassword = new EmailAndPassword();
        emailAndPassword.email = email2.getText().toString();
        emailAndPassword.password = password2.getText().toString();

        if (email2.getText().length()==0||password2.getText().length()==0)
        {
            ringProgressDialog.dismiss();
            Toast.makeText(this, "Wype³nij wszystkie pola!", Toast.LENGTH_LONG).show();
        }
        else
        {
            restRegisterBackgroundTask.register(emailAndPassword);}
    }

    public void loginSuccess(User user) {
        ringProgressDialog.dismiss();
        Intent intent = new Intent();
        intent.putExtra("user", user);
        setResult(RESULT_OK, intent);
        Toast.makeText(this, "Zalogowano!", Toast.LENGTH_LONG).show();
        this.finish();
    }

    public void showErrorLogin(Exception e) {
        ringProgressDialog.dismiss();
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        e.printStackTrace();
    }

    public void registerSuccess(User user) {
        ringProgressDialog.dismiss();
        Intent intent = new Intent();
        intent.putExtra("user", user);
        setResult(RESULT_OK, intent);
        Toast.makeText(this, "Zarejestrowano!", Toast.LENGTH_LONG).show();
        DrawerActivity_.intent(this).user(user).start();
        this.finish();
    }

    public void showErrorRegister(Exception e) {
        ringProgressDialog.dismiss();
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        e.printStackTrace();
    }
}
