package pl.edu.ug.aib.studentizerApp.userData;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.RequiresHeader;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import pl.edu.ug.aib.studentizerApp.userData.Data.EmailAndPassword;
import pl.edu.ug.aib.studentizerApp.userData.Data.User;
import pl.edu.ug.aib.studentizerApp.userData.Data.UserRegister;


@Rest(rootUrl = "https://dsp-patryk-tolodziecki-45894.cloud.dreamfactory.com:443/rest",
        converters = { MappingJackson2HttpMessageConverter.class })
@RequiresHeader({"X-Dreamfactory-Application-Name"})
public interface UserDataRestClient extends RestClientHeaders {

    @Post("/user/session")
    User login(EmailAndPassword emailAndPassword);

    @Post("/user/register/?login=true")
    User register(UserRegister userData);



    @Get("/user/session")
    User getSession();//potrzebne?
}