package pl.edu.ug.aib.studentizerApp.Wallet.restBackgroundTasks;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;

import java.util.ArrayList;
import java.util.List;

import pl.edu.ug.aib.studentizerApp.DrawerActivity;
import pl.edu.ug.aib.studentizerApp.Wallet.WalletRestClient;
import pl.edu.ug.aib.studentizerApp.Wallet.data.Transaction;
import pl.edu.ug.aib.studentizerApp.Wallet.data.Wallet;
import pl.edu.ug.aib.studentizerApp.userData.Data.User;
import pl.edu.ug.aib.studentizerApp.userData.Data.UserList;
import pl.edu.ug.aib.studentizerApp.userData.Data.UserLogout;

@EBean
public class RestBackgroundTask {

    @RootContext
    DrawerActivity activity;

    @RestService
    WalletRestClient restClient;

    @Background
    public void getWallet(User user) {
        try {
            restClient.setHeader("X-Dreamfactory-Application-Name", "aib-android");
            restClient.setHeader("X-Dreamfactory-Session-Token", user.sessionId);
            Wallet wallet = restClient.getWallet();
            if (user != null) {
                List<Integer> list = new ArrayList<Integer>();
                String userIds = null;//get unique IDs
                for (Transaction transaction : wallet.records) {
                    if (!list.contains(transaction.ownerId)) {
                        list.add(transaction.ownerId);
                        userIds += transaction.ownerId + ",";
                    }
                }

                userIds += 3 + ",";
                //String preparation
                String ids = userIds.substring(0, userIds.length()-1);

                //get
                restClient.setHeader("X-Dreamfactory-Application-Name", "aib-android");
                restClient.setHeader("X-Dreamfactory-Session-Token", user.sessionId);
                UserList userList = restClient.getUserId(ids);
                for (Transaction transaction : wallet.records) {
                    for (int i = 0; i < userList.records.size(); i++) {
                        if (userList.records.get(i).id.equals(transaction.ownerId))
                            transaction.displayName = userList.records.get(i).displayName;
                    }
                }
                publishResult(wallet);
            }

            }
        catch (Exception e) {
            publishError(e);
        }
    }



    @UiThread
        void publishResult(Wallet wallet) {

        activity.updateSucess(wallet);
        }

    @Background
    public void logout (String sessionId){
        try {
            restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
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
