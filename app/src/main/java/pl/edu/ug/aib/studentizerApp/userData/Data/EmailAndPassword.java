package pl.edu.ug.aib.studentizerApp.userData.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailAndPassword {

    public String email;
    public String password;
}
