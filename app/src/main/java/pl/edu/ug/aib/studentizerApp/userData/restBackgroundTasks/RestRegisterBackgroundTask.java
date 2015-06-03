package pl.edu.ug.aib.studentizerApp.userData.restBackgroundTasks;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;

import pl.edu.ug.aib.studentizerApp.UserActivity;
import pl.edu.ug.aib.studentizerApp.userData.Data.EmailAndPassword;
import pl.edu.ug.aib.studentizerApp.userData.Data.User;
import pl.edu.ug.aib.studentizerApp.userData.UserDataRestClient;

@EBean
public class RestRegisterBackgroundTask {

    @RootContext
    UserActivity activity;

    @RestService
    UserDataRestClient restClient;

    @Background
    public void register(EmailAndPassword emailAndPassword) {
        try {
            restClient.setHeader("X-Dreamfactory-Application-Name", "aib-android");//sprawdzic czy na dreamfactory sie zgadza
            User user = restClient.register(emailAndPassword);
            publishResult(user);
        } catch (Exception e) {
            publishError(e);
        }
    }

    @UiThread
    void publishResult(User user) {
        activity.registerSuccess(user);
    }

    @UiThread
    void publishError(Exception e) {
        activity.showErrorRegister(e);
    }

}