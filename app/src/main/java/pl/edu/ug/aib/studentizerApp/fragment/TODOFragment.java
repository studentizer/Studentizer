package pl.edu.ug.aib.studentizerApp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import java.util.ArrayList;
import java.util.List;

import pl.edu.ug.aib.studentizerApp.R;

import pl.edu.ug.aib.studentizerApp.todoList.DatabaseHandler;
import pl.edu.ug.aib.studentizerApp.todoList.Task;


@EFragment(R.layout.fragment_todo)
public class TODOFragment extends Fragment {
    private static final int EDIT = 0, DELETE = 1;

    EditText zadanieTxt, opisTxt, dataTxt, adresTxt;
    List<Task> Tasks = new ArrayList<Task>();

    ListView taskListView;

    DatabaseHandler dbHandler;
    int longClickedItemIndex;
    ArrayAdapter<Task> taskAdapter;

    @AfterViews
    void init() {
        getActivity().setTitle(R.string.title_todo);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todo, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();


        zadanieTxt = (EditText) getView().findViewById(R.id.txtZadanie);
        opisTxt = (EditText) getView().findViewById(R.id.txtOpis);
        dataTxt = (EditText) getView().findViewById(R.id.txtData);
        adresTxt = (EditText) getView().findViewById(R.id.txtAdres);
        taskListView = (ListView) getView().findViewById(R.id.listView);
        dbHandler = new DatabaseHandler(getActivity().getApplicationContext());


        registerForContextMenu(taskListView);

        taskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longClickedItemIndex = position;
                return false;
            }
        });

        TabHost tabHost = (TabHost) getView().findViewById(R.id.baner);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Dodaj zadanie");
        tabSpec.setContent(R.id.tabZadanie);
        tabSpec.setIndicator("Dodaj Zadanie");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("lista");
        tabSpec.setContent(R.id.tabListaZadan);
        tabSpec.setIndicator("Lista");
        tabHost.addTab(tabSpec);


        final Button addBtn = (Button) getView().findViewById(R.id.btnAdd);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task task = new Task(dbHandler.getTaskCount(), String.valueOf(zadanieTxt.getText()), String.valueOf(opisTxt.getText()), String.valueOf(dataTxt.getText()), String.valueOf(adresTxt.getText()));
                if (!taskExists(task)) {
                    dbHandler.createZadanie(task);
                    Tasks.add(task);
                    taskAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity().getApplicationContext(), String.valueOf(zadanieTxt.getText()) + " zostało dodane do listy zadań!", Toast.LENGTH_SHORT).show();

                    return ;

                }
                Toast.makeText(getActivity().getApplicationContext(), String.valueOf(zadanieTxt.getText()) + " Zadanie już istnieje", Toast.LENGTH_SHORT).show();
            }
        });


        zadanieTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                addBtn.setEnabled(String.valueOf(zadanieTxt.getText()).trim().length() > 0);
            }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        if (dbHandler.getTaskCount() != 0)
            Tasks.addAll(dbHandler.getAllTasks());

        populateList();
    }

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);


        menu.setHeaderTitle("Opcje zadań");
        menu.add(Menu.NONE, DELETE, menu.NONE, "Usuń zadanie");
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case DELETE:
                dbHandler.deleteTask(Tasks.get(longClickedItemIndex));
                Tasks.remove(longClickedItemIndex);
                taskAdapter.notifyDataSetChanged();
                break;
        }

        return super.onContextItemSelected(item);
    }

    private boolean taskExists(Task task) {
        String name = task.getZadanie();
        int taskCount = Tasks.size();

        for (int i = 0; i < taskCount; i++) {
            if (name.compareToIgnoreCase(Tasks.get(i).getZadanie()) == 0)
                return true;
        }
        return false;
    }



    private void populateList() {
        taskAdapter = new TaskListAdapter();
        taskListView.setAdapter(taskAdapter);
    }

    private class TaskListAdapter extends ArrayAdapter<Task> {
        public TaskListAdapter() {

            super(TODOFragment.this.getActivity(), R.layout.listview_item, Tasks);
        }


        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null)
                view = getActivity().getLayoutInflater().inflate(R.layout.listview_item, parent, false);

            Task currentTask = Tasks.get(position);

            TextView zadanie = (TextView) view.findViewById(R.id.zadanie);
            zadanie.setText(currentTask.getZadanie());
            TextView opis = (TextView) view.findViewById(R.id.opis);
            opis.setText(currentTask.getOpis());
            TextView data = (TextView) view.findViewById(R.id.data);
            data.setText(currentTask.getData());
            TextView adres = (TextView) view.findViewById(R.id.adres);
            adres.setText(currentTask.getAdres());

            return view;

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuInflater mInflater = getActivity().getMenuInflater();
        mInflater.inflate(R.menu.my, menu);
    }
}
