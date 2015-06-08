package pl.edu.ug.aib.studentizerApp.userData;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;

import pl.edu.ug.aib.studentizerApp.MainActivity;
import pl.edu.ug.aib.studentizerApp.userData.Data.EmailAndPassword;
import pl.edu.ug.aib.studentizerApp.userData.Data.User;

/**
 * Created by Patryk on 2015-06-09.
 */
@EBean
public class MainBackgroundTask {

    @RootContext
    MainActivity activity;
    @RestService
    UserDataRestClient restClient;


    @Background
    public void login(EmailAndPassword emailAndPassword){
        try {
            restClient.setHeader("X-Dreamfactory-Application-Name", "aib-android");
            User user = restClient.login(emailAndPassword);
            publishLoginResult(user, emailAndPassword.password);
        }catch(Exception e){
            publishError(e);
        }
    }
    @UiThread
    void publishLoginResult(User user, String password){
        activity.loginSuccess(user, password);
    }
    @UiThread
    void publishError(Exception e) {
        activity.showError(e);
    }
}
