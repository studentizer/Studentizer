package pl.edu.ug.aib.studentizerApp.todoList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.widget.TextView;

import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kisiel on 2015-06-02.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "ZadaniaManager",
            TABLE_ZADANIA = "zadania",
            KEY_ID = "id",
            KEY_ZADANIE = "zadanie",
            KEY_OPIS = "opis",
            KEY_DATA = "data",
            KEY_ADRES = "adres";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_ZADANIA + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ZADANIE + " TEXT," + KEY_OPIS + " TEXT," + KEY_DATA + " TEXT," + KEY_ADRES + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ZADANIA);

        onCreate(db);
    }

    public void createZadanie(Task task) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ZADANIE, task.getZadanie());
        values.put(KEY_OPIS, task.getOpis());
        values.put(KEY_DATA, task.getData());
        values.put(KEY_ADRES, task.getAdres());

        db.insert(TABLE_ZADANIA, null, values);
        db.close();
    }

    public Task getTask(int id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_ZADANIA, new String[] { KEY_ID, KEY_ZADANIE, KEY_OPIS, KEY_DATA, KEY_ADRES }, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null );

        if (cursor != null)
            cursor.moveToFirst();

        Task task = new Task(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        db.close();
        cursor.close();
        return task;
    }

    public void deleteTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_ZADANIA, KEY_ID + "=?", new String[] { String.valueOf(task.getId()) });
        db.close();
    }

    public int getTaskCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ZADANIA, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();

        return count;
    }
//edycja dodanych juz zadan - cos nie dziala
//    public int updateTask(Task task) {
//        SQLiteDatabase db = getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//
//        values.put(KEY_ZADANIE, task.getZadanie());
//        values.put(KEY_OPIS, task.getOpis());
//        values.put(KEY_DATA, task.getData());
//        values.put(KEY_ADRES, task.getAdres());
//
//        int rowsAffected = db.update(TABLE_ZADANIA, values, KEY_ID + "=?", new String[] { String.valueOf(task.getId()) });
//        db.close();
//
//        return rowsAffected;
//    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<Task>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ZADANIA, null);

        if (cursor.moveToFirst()) {
            do {
                tasks.add(new Task(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tasks;
    }
}


