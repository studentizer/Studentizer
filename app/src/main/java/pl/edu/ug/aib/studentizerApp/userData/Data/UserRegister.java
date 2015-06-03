package pl.edu.ug.aib.studentizerApp.userData.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegister implements Serializable {
    public Integer id;
    @JsonProperty("session_id")
    public String sessionId;
    @JsonProperty("display_name")
    public String displayName;
    @JsonProperty("first_name")
    public String firstName;
    @JsonProperty("last_name")
    public String lastName;
    public String email;

    public String password;//

}
