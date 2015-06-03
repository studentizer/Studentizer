package pl.edu.ug.aib.studentizerApp.Wallet.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Wallet {

    @JsonProperty("record")
    public List<Transaction> records = new ArrayList<Transaction>();
    //
}