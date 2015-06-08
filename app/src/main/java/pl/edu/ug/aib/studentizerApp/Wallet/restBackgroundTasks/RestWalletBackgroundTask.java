package pl.edu.ug.aib.studentizerApp.Wallet.restBackgroundTasks;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import pl.edu.ug.aib.studentizerApp.DrawerActivity;
import pl.edu.ug.aib.studentizerApp.Wallet.WalletRestClient;
import pl.edu.ug.aib.studentizerApp.Wallet.data.Transaction;
import pl.edu.ug.aib.studentizerApp.userData.UserPreferences_;

@EBean
public class RestWalletBackgroundTask {

    @RootContext
    DrawerActivity activity;

    @RestService
    WalletRestClient restClient;

    @Pref
    UserPreferences_ preferences;

    @Background
    public void addWalletEntry(Transaction transaction) {
        try {
            restClient.setHeader("X-Dreamfactory-Application-Name", "aib-android");
            restClient.setHeader("X-Dreamfactory-Session-Token", preferences.sessionId().get());
            restClient.addWalletEntry(transaction);
            publishResult(transaction);
        } catch (Exception e) {
            publishError(e);
        }
    }

    @UiThread
    void publishResult(Transaction transaction) {
        activity.addSuccess(transaction);
    }

    @UiThread
    void publishError(Exception e) {
        activity.showErrorAdd(e);
    }
}
