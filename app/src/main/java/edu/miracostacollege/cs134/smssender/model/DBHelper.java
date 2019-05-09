package edu.miracostacollege.cs134.smssender.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    //TASK 1: DEFINE THE DATABASE VERSION, NAME AND TABLE NAME
    static final String DATABASE_NAME = "SMSSender";
    private static final int DATABASE_VERSION = 1;

    //TASK 2: DEFINE THE FIELDS (COLUMN NAMES) FOR THE TABLE
    private static final String DATABASE_TABLE = "Contacts";
    private static final String KEY_FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_PHONE_NUMBER = "phone_number";

    public DBHelper(Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase database){
        String table = "CREATE TABLE " + DATABASE_TABLE + " ("
                + KEY_FIELD_ID + " INTEGER PRIMARY KEY, "
                + FIELD_NAME + " TEXT, "
                + FIELD_PHONE_NUMBER + " TEXT)";
        database.execSQL(table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          int oldVersion,
                          int newVersion) {

        database.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(database);
    }

    //********** DATABASE OPERATIONS:  ADD, GET ALL, GET 1, DELETE

    public void addContact(Contact contact) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // add key-value
        values.put(FIELD_NAME, contact.getName());
        values.put(FIELD_PHONE_NUMBER, contact.getPhone());

        long id = db.insert(DATABASE_TABLE, null, values);
        contact.setId(id);
        db.close();
    }

    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> contactsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DATABASE_TABLE, null, null, null, null, null, null);

        if(cursor.moveToFirst())
        {
            do{
                int id = cursor.getInt(0); // index column
                String name = cursor.getString(1);
                String phoneNumber = cursor.getString(2);

                Contact newContact = new Contact(id, name, phoneNumber);

                contactsList.add(newContact);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return contactsList;
    }

    public Contact getContact(String name) {
        Contact contact = new Contact();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DATABASE_TABLE,
                new String[] {KEY_FIELD_ID, FIELD_NAME, FIELD_PHONE_NUMBER},
                FIELD_NAME + " = ?", new String[]{name}, null, null, null);

        if(cursor.moveToFirst())
        {
            do{
                int id = cursor.getInt(0); // index column
                String mName = cursor.getString(1);
                String phone = cursor.getString(2);

                contact.setName(mName);
                contact.setPhone(phone);
                contact = new Contact(id, mName, phone);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contact;
    }

    public void deleteContact(long id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, KEY_FIELD_ID + " = ?", new String[] {String.valueOf(id)});
        db.close();
    }
}
