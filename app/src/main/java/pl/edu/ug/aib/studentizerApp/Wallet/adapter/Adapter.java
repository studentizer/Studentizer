package pl.edu.ug.aib.studentizerApp.Wallet.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.Collections;
import java.util.List;

import pl.edu.ug.aib.studentizerApp.Wallet.data.Transaction;
import pl.edu.ug.aib.studentizerApp.Wallet.data.Wallet;
import pl.edu.ug.aib.studentizerApp.Wallet.itemView.TransactionItemView;
import pl.edu.ug.aib.studentizerApp.Wallet.itemView.TransactionItemView_;

@EBean
public class Adapter extends BaseAdapter {

    private List<Transaction> lista;

    public void setList(List<Transaction> lista) {

        this.lista = lista;
    }

    @RootContext
    Context context;


    public Adapter() {
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Transaction getItem(int i) {
        return lista.get(i);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        TransactionItemView transactionItemView;

        if (view == null) {
            transactionItemView = TransactionItemView_.build(context);
        } else {
            transactionItemView = (TransactionItemView) view;
        }

        transactionItemView.bind(getItem(i));

        return transactionItemView;

    }

    public void update(Wallet wallet) {
        lista.clear();
        lista.addAll(wallet.records);
        Collections.sort(lista);
        notifyDataSetChanged();
    }

}
