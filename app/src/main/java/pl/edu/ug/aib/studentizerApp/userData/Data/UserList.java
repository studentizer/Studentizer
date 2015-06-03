package pl.edu.ug.aib.studentizerApp.userData.Data;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class UserList {

    @JsonProperty("record")
    public List<User> records = new ArrayList<User>();//
}
