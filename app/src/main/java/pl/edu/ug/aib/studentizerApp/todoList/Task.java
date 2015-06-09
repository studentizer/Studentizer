package pl.edu.ug.aib.studentizerApp.todoList;


/**
 * Created by Kisiel on 2015-06-02.
 */
public class Task {

    private String _zadanie, _opis, _data, _adres;
    private int _id;

    public Task (int id, String zadanie, String opis, String data, String adres) {
        _id = id;
        _zadanie = zadanie;
        _opis = opis;
        _data = data;
        _adres = adres;

    }

    public int getId() { return _id; }

    public String getZadanie() {    return _zadanie; }

    public String getOpis() {
        return _opis;
    }

    public String getData() {
        return _data;
    }

    public String getAdres() { return _adres; }

}


