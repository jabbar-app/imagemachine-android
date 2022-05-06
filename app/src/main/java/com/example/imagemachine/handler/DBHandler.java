package com.example.imagemachine.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.imagemachine.modal.Image;
import com.example.imagemachine.modal.Machine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "machinedb";
    private static final int DB_VERSION = 1;
    private static final String TABLE_MACHINE = "machine";
    private static final String TABLE_IMAGE = "image";
    private static final String MachineID = "machine_id";
    private static final String MachineName = "machine_name";
    private static final String MachineType = "machine_type";
    private static final String MachineQr = "machine_qr";
    private static final String MachineMt = "machine_mt";
    private static final String ImageID = "image_id";
    private static final String ImageMachine = "image_machine";
    private static final String ImageUri = "image_uri";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String queryMachine = "CREATE TABLE " + TABLE_MACHINE + " ("
                + MachineID + " TEXT NOT NULL UNIQUE, "
                + MachineName + " TEXT,"
                + MachineType + " TEXT,"
                + MachineQr + " TEXT,"
                + MachineMt + " TEXT)";

        sqLiteDatabase.execSQL(queryMachine);

        String queryImage = "CREATE TABLE " + TABLE_IMAGE + " ("
                + ImageID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ImageMachine + " INTEGER,"
                + ImageUri + " TEXT)";

        sqLiteDatabase.execSQL(queryImage);
    }

    public void addMachine(String machineName, String machineType, String machineMt) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MachineID, UUID.randomUUID().toString());
        values.put(MachineName, machineName);
        values.put(MachineType, machineType);
        values.put(MachineQr, random());
        values.put(MachineMt, machineMt);
        db.insert(TABLE_MACHINE, null, values);
        db.close();
    }

    public void addImage(String imageMachine, String imageUri){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ImageMachine, imageMachine);
        values.put(ImageUri, imageUri);
        db.insert(TABLE_IMAGE, null, values);
        db.close();
    }

    public void deleteMachine(String machineId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MACHINE, "machine_id=?", new String[]{machineId});
        db.close();
    }

    public void deleteImage(int imageId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_IMAGE, "image_id=?", new String[]{String.valueOf(imageId)});
        db.close();
    }

    public void updateMachine(String machineId, String machineName, String machineType, String machineMt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(MachineName, machineName);
        values.put(MachineType, machineType);
        values.put(MachineMt, machineMt);

        // on below line we are calling a update method to update our database and passing our values.
        // and we are comparing it with name of our course which is stored in original name variable.
        db.update(TABLE_MACHINE, values, "machine_id=?", new String[]{machineId});
        db.close();
    }

    public ArrayList<Image> getImage(String imageMachine) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_IMAGE + " WHERE " + ImageMachine +
                " = " +  imageMachine, null);
        ArrayList<Image> courseModalArrayList = new ArrayList<>();
        if (cursorCourses.moveToFirst()) {
            do {
                courseModalArrayList.add(new Image(cursorCourses.getShort(0),
                        cursorCourses.getString(1),
                        cursorCourses.getString(2)));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return courseModalArrayList;
    }

    public ArrayList<Machine> getMachine() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_MACHINE + " ORDER BY " +
                MachineName + " ASC", null);
        ArrayList<Machine> courseModalArrayList = new ArrayList<>();
        if (cursorCourses.moveToFirst()) {
            do {
                courseModalArrayList.add(new Machine(cursorCourses.getString(0),
                        cursorCourses.getString(1),
                        cursorCourses.getString(2),
                        cursorCourses.getString(3),
                        cursorCourses.getString(4)));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return courseModalArrayList;
    }

    public ArrayList<Machine> getMachineQr(String qr){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_MACHINE + " WHERE " + MachineQr +
                        " = " +  qr + " ORDER BY " + MachineName + " ASC", null);
        ArrayList<Machine> courseModalArrayList = new ArrayList<>();
        if (cursorCourses.moveToFirst()) {
            do {
                courseModalArrayList.add(new Machine(cursorCourses.getString(0),
                        cursorCourses.getString(1),
                        cursorCourses.getString(2),
                        cursorCourses.getString(3),
                        cursorCourses.getString(4)));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return courseModalArrayList;
    }

    public ArrayList<Machine> getMachineType() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_MACHINE + " ORDER BY " +
                MachineType + " ASC", null);
        ArrayList<Machine> courseModalArrayList = new ArrayList<>();
        if (cursorCourses.moveToFirst()) {
            do {
                courseModalArrayList.add(new Machine(cursorCourses.getString(0),
                        cursorCourses.getString(1),
                        cursorCourses.getString(2),
                        cursorCourses.getString(3),
                        cursorCourses.getString(4)));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return courseModalArrayList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MACHINE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE);
        onCreate(db);
    }

    Integer random() {
        char[] chars = "1234567890".toCharArray();
        StringBuilder sb = new StringBuilder(5);
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        Integer output = Integer.parseInt(sb.toString());
        return output;
    }
}
