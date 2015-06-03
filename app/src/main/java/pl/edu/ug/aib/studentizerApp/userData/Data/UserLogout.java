package pl.edu.ug.aib.studentizerApp.userData.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//Class encapsulating information passed after logout
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLogout {
    public boolean success;
}

