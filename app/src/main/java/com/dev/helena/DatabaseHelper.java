package com.dev.helena;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.TimedMetaData;

import androidx.annotation.Nullable;

import com.dev.helena.Therapy.DaysDrug;
import com.dev.helena.Therapy.Therapy;
import com.dev.helena.Therapy.TimeDrug;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context) {
        super(context, "helena.db", null, 1);
    }

    /*TABELLA DELLE TERAPIE*/
    public static final String THERAPY_TABLE = "THERAPY_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_THERAPY_NAME = "THERAPY_NAME";
    public static final String COLUMN_DRUG_NAME = "MEDICINE_NAME";
    public static final String COLUMN_DOSAGE = "DOSAGE";
    public static final String COLUMN_FINAL_DATE = "FINAL_DATE";

    /*TABELLA DEGLI ORARI DELLE TERAPIE*/
    public static final String TIME_TABLE = "THERAPY_TIME_TABLE";
    public static final String COLUMN_MINUTE = "MINUTE";
    public static final String COLUMN_HOUR = "HOUR";
    public static final String COLUMN_THERAPY_ID = "THERAPY_ID";

    /*TABELLA DEI GIORNI DELLE TERAPIE*/
    public static final String DAYS_TABLE = "THERAPY_DAYS_TABLE";
    public static final String COLUMN_DAY = "DAY";

    /*TABELLA DIZIONARIO MEDICO*/


    private static final String SQL_CREATE_TABLE_THERAPY = "CREATE TABLE " + THERAPY_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_THERAPY_NAME + " TEXT, " + COLUMN_DRUG_NAME + " TEXT, " + COLUMN_DOSAGE + " INT, " + COLUMN_FINAL_DATE + " TEXT)";
    private static final String SQL_CREATE_DAYS_TIME = "CREATE TABLE " + DAYS_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DAY + " TEXT, " + COLUMN_THERAPY_ID + " INT, " + "FOREIGN KEY" + "(" + COLUMN_THERAPY_ID +")" + " REFERENCES " + THERAPY_TABLE +"(" + COLUMN_ID +")" +")";
    private static final String SQL_CREATE_TABLE_TIME = "CREATE TABLE " + TIME_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_HOUR + " INT, " + COLUMN_MINUTE + " INT, " + COLUMN_THERAPY_ID + " INT, " + "FOREIGN KEY" + "(" + COLUMN_THERAPY_ID +")" + " REFERENCES " + THERAPY_TABLE +"(" + COLUMN_ID +")" +")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_THERAPY);
        db.execSQL(SQL_CREATE_TABLE_TIME);
        db.execSQL(SQL_CREATE_DAYS_TIME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOneTherapy(Therapy therapy) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues therapyValue = new ContentValues();
        //Valori Terapia
        therapyValue.put(COLUMN_THERAPY_NAME, therapy.getName());
        therapyValue.put(COLUMN_DRUG_NAME, therapy.getDrugName());
        therapyValue.put(COLUMN_DOSAGE, therapy.getDosage());
        therapyValue.put(COLUMN_FINAL_DATE, therapy.getEndTime());
        long insertTherapyValue = db.insert(THERAPY_TABLE, null, therapyValue);
        if (insertTherapyValue == -1)
            return false;
        else
            return true;
    }

    public boolean addOneTime(TimeDrug timeDrug)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues timeValue = new ContentValues();
        //Valori Orari
        timeValue.put(COLUMN_HOUR, timeDrug.getHour());
        timeValue.put(COLUMN_MINUTE, timeDrug.getMinute());
        timeValue.put(COLUMN_THERAPY_ID, timeDrug.getTherapyID());
        long insertTimeValue = db.insert(TIME_TABLE, null, timeValue);
        if (insertTimeValue == -1)
            return false;
        else
            return true;
    }

    public boolean addOnedDay(DaysDrug daysDrug)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues timeValue = new ContentValues();
        //Valori Orari
        timeValue.put(COLUMN_DAY, String.valueOf(daysDrug.getDay()));
        timeValue.put(COLUMN_THERAPY_ID, daysDrug.getTherapyID());
        long insertTimeValue = db.insert(TIME_TABLE, null, timeValue);
        if (insertTimeValue == -1)
            return false;
        else
            return true;
    }

    public boolean deleteOne(Therapy therapy) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + THERAPY_TABLE + " WHERE " + COLUMN_ID + " = " + therapy.getId();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
            return true;
        else
            return false;
    }

    public List<Therapy> getAllTherapy() {
        List<Therapy> returnTherapy = new ArrayList<>();
        String queryString = "SELECT * FROM " + THERAPY_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            do {
                int therapyID = cursor.getInt(0);
                String therapyName = cursor.getString(1);
                String drugName = cursor.getString(2);
                int dosage = cursor.getInt(3);
                String endDate = cursor.getString(4);

                Therapy newTherapy = new Therapy(therapyID, therapyName, drugName, dosage, endDate);
                returnTherapy.add(newTherapy);

            } while (cursor.moveToNext());
        } else {
            // failure, do not add anything to the list
        }
        cursor.close();
        db.close();
        return returnTherapy;
    }

}
