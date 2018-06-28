package com.homex.fexp;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.Calendar;
import android.util.Log;

public class DBhandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UsersExpenseManager";
    private static final String TABLE_USERS = "users";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String TABLE_EXPENSES = "Expenses";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE = "date";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_BUDGET = "budget";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_MINUTE = "minute";


    public DBhandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_EMAIL + " TEXT PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_PHONE_NUMBER + " TEXT,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_BUDGET + " INTEGER,"
                + KEY_HOUR + " INTEGER,"
                + KEY_MINUTE + " INTEGER"+")";

        db.execSQL(CREATE_USERS_TABLE_USERS);
        String CREATE_USERS_TABLE_EXPENSE = "CREATE TABLE " + TABLE_EXPENSES + "("
                + KEY_EMAIL + " TEXT,"
                + KEY_CATEGORY + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_AMOUNT + " INTEGER,"
                + KEY_DATE + " DATE" + ")";
        db.execSQL(CREATE_USERS_TABLE_EXPENSE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    void addContact(Mydata contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, contact.getEmail());
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PHONE_NUMBER, contact.getPhone_number());
        values.put(KEY_PASSWORD, contact.getPassword());
        values.put(KEY_BUDGET, contact.getBudget());
        values.put(KEY_HOUR, contact.getHour());
        values.put(KEY_MINUTE, contact.getMinute()); // Contact Phone
        // Inserting Row
        db.insert(TABLE_USERS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    void addexpense(AddExpense expense) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, expense.getEmail());
        values.put(KEY_CATEGORY, expense.getCategory());
        values.put(KEY_DESCRIPTION, expense.getDescription());
        values.put(KEY_AMOUNT, expense.getAmount());
        values.put(KEY_DATE, expense.getDate());
        // Inserting Row
        db.insert(TABLE_EXPENSES, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }
    // code to get the single contact
    Mydata getContact(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_EMAIL,
                        KEY_NAME, KEY_PHONE_NUMBER, KEY_PASSWORD, KEY_BUDGET, KEY_HOUR, KEY_MINUTE }, KEY_EMAIL + "=?",
                new String[] { String.valueOf(email) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Mydata contact = new Mydata(cursor.getString(0),
                cursor.getString(3), cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(4)),Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)) );
        // return contact
        return contact;
    }

    // code to get all contacts in a list view
    public List<Mydata> getAllContacts() {
        List<Mydata> contactList = new ArrayList<Mydata>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Mydata contact = new Mydata();
                contact.setEmail(cursor.getString(0));
                contact.setName(cursor.getString(1));
                contact.setPhone_number(cursor.getString(2));
                contact.setPassword(cursor.getString(3));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }
    public List<AddExpense> getallexpenses(String email) {
        List<AddExpense> contactList = new ArrayList<AddExpense>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EXPENSES + " WHERE "+KEY_EMAIL+ "='"+email+ "' ORDER BY "+KEY_DATE+ " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AddExpense expense = new AddExpense();
                expense.setEmail(cursor.getString(0));
                expense.setCategory(cursor.getString(1));
                expense.setDescription(cursor.getString(2));
                expense.setAmount(cursor.getInt(3));
                expense.setDate(cursor.getString(4));
                // Adding contact to list
                contactList.add(expense);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }


    public int getcategoryexpense(String email, String category, String date) {
        List<AddExpense> contactList = new ArrayList<AddExpense>();
        contactList = getallexpenses(email);
        int sum = 0;
        for(AddExpense ae: contactList){
            String[] data = ae.getDate().split("-");
            String year = data[0];
            String month = data[1];
            String dt = year+"-"+month;
            Log.i("tax",dt);
            Log.i("tax", date);
            if(ae.getCategory().equals(category)&&dt.equals(date))
                sum+=ae.getAmount();
        }
        Log.e("tax",sum+"");
        // return contact list
        return sum;
    }

    public int getmonthexpense(String email, String date) {
        int ryear;
        List<AddExpense> contactList = new ArrayList<AddExpense>();
        contactList = getallexpenses(email);
        int sum = 0;
        for(AddExpense ae: contactList){
            String[] data = ae.getDate().split("-");
            String year = data[0];
            String month = data[1];
            String dt = year+"-"+month;
            Log.i("tax",dt);
            Log.i("tax", date);
            if(dt.equals(date))
                sum+=ae.getAmount();
        }
        Log.e("tax",sum+"");
        // return contact list
        return sum;
    }



    // code to update the single contact
    public int updateContact(Mydata contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, contact.getEmail());
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PHONE_NUMBER, contact.getPhone_number());
        values.put(KEY_PASSWORD, contact.getPassword());
        values.put(KEY_BUDGET, contact.getBudget());
        values.put(KEY_HOUR, contact.getHour());
        values.put(KEY_MINUTE, contact.getMinute());

        // updating row
        return db.update(TABLE_USERS, values, KEY_EMAIL + " = ?",
                new String[] { String.valueOf(contact.getEmail()) });
    }

}
