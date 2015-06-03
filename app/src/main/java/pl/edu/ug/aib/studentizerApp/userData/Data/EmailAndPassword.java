package pl.edu.ug.aib.studentizerApp.userData.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailAndPassword implements Serializable {

    public String email;
    public String password;//
}
