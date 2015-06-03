package pl.edu.ug.aib.studentizerApp.Wallet.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction implements Serializable, Comparable<Transaction>{

    public String nazwa_transakcji;
    public double wartosc_transakcji;//nie String!
    public Integer id_transakcji;
    public String data_transakcji;
    public Integer ownerId;

    @JsonIgnore
    public String displayName;

    @JsonProperty("session_id")
    public String sessionId;

    @Override
    public int compareTo(Transaction transaction) {

        if(id_transakcji > transaction.id_transakcji){
            return -1;
        }
        else if(id_transakcji == transaction.id_transakcji){
            return 0;

        }
        else
        {
            return 1;
        }
    }
//

}