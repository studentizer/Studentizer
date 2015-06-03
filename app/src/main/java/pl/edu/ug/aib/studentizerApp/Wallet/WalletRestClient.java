package pl.edu.ug.aib.studentizerApp.Wallet;

import android.util.Log;

import org.androidannotations.annotations.rest.Delete;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.RequiresHeader;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import pl.edu.ug.aib.studentizerApp.Wallet.data.Transaction;
import pl.edu.ug.aib.studentizerApp.Wallet.data.Wallet;
import pl.edu.ug.aib.studentizerApp.userData.Data.UserList;
import pl.edu.ug.aib.studentizerApp.userData.Data.UserLogout;


@Rest(rootUrl = "https://dsp-patryk-tolodziecki-45894.cloud.dreamfactory.com:443/rest",
        converters = { MappingJackson2HttpMessageConverter.class })
@RequiresHeader({"X-Dreamfactory-Application-Name"})
public interface WalletRestClient extends RestClientHeaders {

    @RequiresHeader({"X-Dreamfactory-Application-Name","X-Dreamfactory-Session-Token"})
    @Get("/system/user?ids={id}")
    UserList getUserId(String id);

    @RequiresHeader({"X-Dreamfactory-Application-Name", "X-Dreamfactory-Session-Token"})
    @Get("/db/portfel?filter={filter}")
    Wallet getWallet(String filter);

    @RequiresHeader({"X-Dreamfactory-Session-Token", "X-Dreamfactory-Application-Name"})
    @Post("/db/portfel/")
    void addWalletEntry(Transaction transaction);

    @Delete("/user/session")
    UserLogout logout();
}