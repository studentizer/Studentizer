package pl.edu.ug.aib.studentizerApp.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.edu.ug.aib.studentizerApp.R;
import pl.edu.ug.aib.studentizerApp.Wallet.adapter.Adapter;
import pl.edu.ug.aib.studentizerApp.Wallet.data.Transaction;
import pl.edu.ug.aib.studentizerApp.Wallet.data.Wallet;
import pl.edu.ug.aib.studentizerApp.userData.Data.EmailAndPassword;
import pl.edu.ug.aib.studentizerApp.userData.Data.User;

@EFragment(R.layout.fragment_wallet)
public class WalletFragment extends Fragment {

    BgTask mCallback; //call back from DrawerActivity
    Fragment fragment;

    User user;

    Wallet wallet;

    //Container activity must implement that interface
    public interface BgTask{
        public void updateWallet(User user);
        public void addTransaction(User user, Transaction transaction);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try{
            mCallback = (BgTask)activity;
        } catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + "must implement an interface!");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_wallet, container, false);
    }

    @AfterViews
    void init() {
        getActivity().setTitle(R.string.title_wallet);
        Intent intent = new Intent();
        intent.getExtras();
        lista = new ArrayList<Transaction>();
        adapter.setList(lista);
        listaoperacji.setAdapter(adapter);
        ringProgressDialog = new ProgressDialog(getActivity());
        ringProgressDialog.setMessage("£adowanie listy transakcji...");
        ringProgressDialog.setIndeterminate(true);
        mCallback.updateWallet(user);
    }

    @ViewById
    TextView srodki;

    @ViewById
    Button add;

    @ViewById
    ListView listaoperacji;

    @ViewById
    EditText nazwa_transakcji;

    @ViewById
    EditText wartosc_transakcji;

    @ViewById
    EditText data_transakcji;

    @Bean
    Adapter adapter;

    private ProgressDialog ringProgressDialog;

    private List<Transaction> lista;

    public void update(Wallet wallet) {
        ringProgressDialog.dismiss();
        Collections.reverse(lista);
        adapter.update(wallet);
        List<Double> wartosci = new ArrayList<Double>();
        double saldo = 0;//get unique IDs
        for (Transaction transaction : wallet.records) {
            while (!wartosci.contains(transaction.wartosc_transakcji)) {
                wartosci.add(transaction.wartosc_transakcji);
                saldo += transaction.wartosc_transakcji;
            }
        }
        srodki.setText(saldo + " z³");
    }

    public void showErrorUpdate(Exception e) {
        ringProgressDialog.dismiss();
        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        e.printStackTrace();
    }

    @Click(R.id.add)//cos nie dziala, sprawdzic
    void addClicked() {
        ringProgressDialog.show();

            Transaction transaction = new Transaction();
            transaction.ownerId = user.id;
            transaction.nazwa_transakcji = nazwa_transakcji.getText().toString();
            transaction.data_transakcji = data_transakcji.getText().toString();
            transaction.wartosc_transakcji = Double.parseDouble(wartosc_transakcji.getText().toString());

            if ((nazwa_transakcji.length() == 0) || (wartosc_transakcji.length() == 0) || (data_transakcji.length() == 0)) {
                ringProgressDialog.dismiss();
                Toast.makeText(getActivity(), "Wype³nij wszystkie pola!", Toast.LENGTH_LONG).show();
            } else {
                mCallback.addTransaction(user, transaction);
            }
        }
    public void addSuccess(Transaction transaction) {
        ringProgressDialog.dismiss();
        Toast.makeText(getActivity(), "Dodano transakcjê!", Toast.LENGTH_LONG).show();
        mCallback.updateWallet(user);
    }

}
