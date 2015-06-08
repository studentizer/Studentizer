package pl.edu.ug.aib.studentizerApp.Wallet.restBackgroundTasks;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import pl.edu.ug.aib.studentizerApp.DrawerActivity;
import pl.edu.ug.aib.studentizerApp.Wallet.WalletRestClient;
import pl.edu.ug.aib.studentizerApp.Wallet.data.Wallet;
import pl.edu.ug.aib.studentizerApp.userData.Data.UserLogout;
import pl.edu.ug.aib.studentizerApp.userData.UserPreferences_;

@EBean
public class RestBackgroundTask {


    @Pref
    UserPreferences_ preferences;

    @RootContext
    DrawerActivity activity;

    @RestService
    WalletRestClient restClient;

    @Background
    public void getWallet() {
        try {
            String userID = String.valueOf(preferences.id().get());
            String sesja = preferences.sessionId().get();
            restClient.setHeader("X-Dreamfactory-Application-Name", "aib-android");
            restClient.setHeader("X-Dreamfactory-Session-Token", sesja);
            Wallet wallet = restClient.getWallet("ownerId="+userID);
                publishResult(wallet);

            }


        catch (Exception e) {
            publishError(e);
        }
    }



    @UiThread
        void publishResult(Wallet wallet) {

        activity.updateSuccess(wallet);
        }

    @Background
    public void logout (String sessionId){
        try {
            restClient.setHeader("X-Dreamfactory-Application-Name", "aib-android");
            restClient.setHeader("X-Dreamfactory-Session-Token", sessionId);
            UserLogout userLogout = restClient.logout();
            publishLogoutResult(userLogout.success);
        } catch (Exception e) {
            publishError(e);
        }
    }

    @UiThread
    void publishLogoutResult (Boolean success){
        activity.onLogout(success);
    }


    @UiThread
    void publishError(Exception e) {
        activity.showErrorUpdate(e);
    }
}
