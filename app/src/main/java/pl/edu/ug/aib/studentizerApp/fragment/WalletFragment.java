package pl.edu.ug.aib.studentizerApp.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.edu.ug.aib.studentizerApp.R;
import pl.edu.ug.aib.studentizerApp.Wallet.adapter.Adapter;
import pl.edu.ug.aib.studentizerApp.Wallet.data.Transaction;
import pl.edu.ug.aib.studentizerApp.Wallet.data.Wallet;
import pl.edu.ug.aib.studentizerApp.userData.Data.User;
import pl.edu.ug.aib.studentizerApp.userData.UserPreferences_;

@EFragment(R.layout.fragment_wallet)
public class WalletFragment extends Fragment {

    @Pref
    UserPreferences_ preferences;

    User user;

    Wallet wallet;

    BgTask mCallback; //call back from DrawerActivity
    Fragment fragment;

    Transaction transaction;

    //Container activity must implement that interface
    public interface BgTask{
        public void updateWallet();
        public void addTransaction(Transaction transaction);
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
        lista = new ArrayList<Transaction>();
        adapter.setList(lista);
        listaoperacji.setAdapter(adapter);
        ringProgressDialog = new ProgressDialog(getActivity());
        ringProgressDialog.setMessage(String.valueOf(R.string.walletLoad));
        ringProgressDialog.setIndeterminate(true);
        mCallback.updateWallet();
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

    @ViewById(R.id.data_transakcji)
    EditText data_transakcji;

    @Bean
    Adapter adapter;

    private ProgressDialog ringProgressDialog;

    private List<Transaction> lista;

    public void updateSuccess(Wallet wallet) {
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
        DecimalFormat df = new DecimalFormat("#.00");
        srodki.setText(df.format(saldo));
    }

    public void showErrorUpdate(Exception e) {
        ringProgressDialog.dismiss();
        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        e.printStackTrace();
    }

    @Click(R.id.add)//cos nie dziala, sprawdzic
    void addClicked() {
        ringProgressDialog.show();
        ringProgressDialog.setMessage("Dodawanie transakcji...");
        ringProgressDialog.setIndeterminate(true);
        if ((nazwa_transakcji.toString().isEmpty()) || data_transakcji.getText().toString().isEmpty() || wartosc_transakcji.getText().toString().isEmpty()) {
            ringProgressDialog.dismiss();
            Toast.makeText(getActivity(), R.string.missFields, Toast.LENGTH_LONG).show();
        } else {
            Transaction transaction = new Transaction();
            transaction.ownerId = preferences.id().get();
            transaction.nazwa_transakcji = nazwa_transakcji.getText().toString();
            transaction.data_transakcji = data_transakcji.getText().toString();
            transaction.wartosc_transakcji = Double.parseDouble(wartosc_transakcji.getText().toString());
            double saldo = Double.parseDouble(srodki.getText().toString());
            double wartosc = Double.parseDouble(wartosc_transakcji.getText().toString());
            if (saldo - (2 * saldo) > wartosc) {
                ringProgressDialog.dismiss();
                Toast.makeText(getActivity(), R.string.errorAddWallet, Toast.LENGTH_LONG).show();
            } else {
                if (wartosc == 0) {
                    ringProgressDialog.dismiss();
                    Toast.makeText(getActivity(), R.string.errorAddWallet2, Toast.LENGTH_LONG).show();
                } else {
                    if (preferences.sessionId().get().isEmpty()) {
                        ringProgressDialog.dismiss();
                        Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_LONG).show();
                    } else {
                        mCallback.addTransaction(transaction);
                    }
                }
            }
        }
    }
    public void addSuccess(Transaction transaction) {
        ringProgressDialog.dismiss();
        Toast.makeText(getActivity(), R.string.walletAddOk, Toast.LENGTH_LONG).show();
        mCallback.updateWallet();
        nazwa_transakcji.setText("");
        data_transakcji.setText("");
        wartosc_transakcji.setText("");
    };
}
