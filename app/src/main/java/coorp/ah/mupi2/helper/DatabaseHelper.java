package coorp.ah.mupi2.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import coorp.ah.mupi2.model.Soal;
import coorp.ah.mupi2.model.Tempat;

/**
 * Created by hidayatasep43 on 14-Dec-15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    //database version
    private static final int DATABASE_VERSION = 1;

    //DATABASE NAME
    private  static final String DATABASE_NAME = "mupi";

    //table Names
    private static final String TABLE_TEMPAT = "tempat";
    private static final String TABLE_SOAL = "soal";

    //kolom tabel tempat
    private static final String TEMPAT_ID = "id";
    private static final String TEMPAT_NAMA = "nama";
    private static final String TEMPAT_LATITUDE = "latitude";
    private static final String TEMPAT_LONGITUDE = "longitude";
    private static final String TEMPAT_STATUS = "status";

    //kolom tabel soal
    private static final String SOAL_ID = "id";
    private static final String SOAL_SOAL = "soal";
    private static final String SOAL_KUNCIJAWABAN = "kuncijawaban";
    private static final String SOAL_STATUS = "status";
    private static final String SOAL_TEMPAT = "tempat";
    private static final String SOAL_TIPE = "tipesoal";
    private static final String SOAL_GAMBAR = "gambar";

    //create table tempat
    private static final String CREATE_TABLE_TEMPAT="CREATE TABLE " + TABLE_TEMPAT + "(" +
            TEMPAT_ID + " INTEGER PRIMARY KEY, " +
            TEMPAT_NAMA + " TEXT, "+
            TEMPAT_LATITUDE + " TEXT, "+
            TEMPAT_LONGITUDE + " TEXT, "+
            TEMPAT_STATUS + " TEXT ); ";

    private static final String CREATE_TABLE_SOAL="CREATE TABLE " + TABLE_SOAL + "(" +
            SOAL_ID + " INTEGER PRIMARY KEY, " +
            SOAL_SOAL + " TEXT, "+
            SOAL_KUNCIJAWABAN + " TEXT , " +
            SOAL_STATUS + " TEXT , "+
            SOAL_TIPE + " TEXT , "+
            SOAL_TEMPAT + " TEXT , "+
            SOAL_GAMBAR + " TEXT ); ";

    private static final String LOG = "DATABASEHEPLER";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table
        db.execSQL(CREATE_TABLE_SOAL);
        db.execSQL(CREATE_TABLE_TEMPAT);
        Log.e(LOG, CREATE_TABLE_SOAL);
        Log.e(LOG, CREATE_TABLE_TEMPAT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST" + TABLE_SOAL);
        db.execSQL("DROP TABLE IF EXIST" + TABLE_TEMPAT);

        //CREATE NEW TABLE
        onCreate(db);
    }

    //insert soal
    public long insertSoal(Soal soal){
        long id;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SOAL_SOAL,soal.getSoal());
        values.put(SOAL_KUNCIJAWABAN,soal.getKunciJawaban());
        values.put(SOAL_TEMPAT,soal.getTempat());
        values.put(SOAL_STATUS,soal.getStatus());
        values.put(SOAL_TIPE,soal.getTipeSoal());
        values.put(SOAL_GAMBAR,soal.getGambar());

        id = db.insert(TABLE_SOAL,null,values);
        db.close();
        Log.e(LOG, soal.getSoal() + soal.getTempat() + soal.getTipeSoal());
        return  id;
    }

    //insert soal
    public void insertTempat(Tempat tempat){
        long id;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TEMPAT_NAMA, tempat.getNmTempat());
        values.put(TEMPAT_LATITUDE, tempat.getLatitude());
        values.put(TEMPAT_LONGITUDE, tempat.getLongitude());
        values.put(TEMPAT_STATUS, tempat.getStatus());

        db.insert(TABLE_TEMPAT,null,values);
        db.close();
        Log.e(LOG,tempat.getNmTempat() + tempat.getStatus());
    }

    //mengambil data 1 soal
    public Soal getSoal(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_SOAL + " WHERE " + SOAL_ID + " = " + id;
        Log.e(LOG,selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if(c!=null){
            c.moveToFirst();
        }
        Soal soal = new Soal();

        soal.setId(c.getInt(c.getColumnIndex(SOAL_ID)));
        soal.setSoal(c.getString(c.getColumnIndex(SOAL_SOAL)));
        soal.setKunciJawaban(c.getString(c.getColumnIndex(SOAL_KUNCIJAWABAN)));
        soal.setTempat(c.getString(c.getColumnIndex(SOAL_TEMPAT)));
        soal.setStatus(c.getInt(c.getColumnIndex(SOAL_STATUS)));
        soal.setTipeSoal(c.getString(c.getColumnIndex(SOAL_TIPE)));
        soal.setGambar(c.getString(c.getColumnIndex(SOAL_GAMBAR)));

        c.close();
        db.close();

        return soal;
    }

    public Tempat getTempat(String tempat){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TEMPAT + " WHERE " + TEMPAT_NAMA + " = '" + tempat +"'";
        Log.e(LOG,selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if(c!=null){
            c.moveToFirst();
        }
        Tempat tempat1 = new Tempat();
        tempat1.setId(c.getInt(c.getColumnIndex(TEMPAT_ID)));
        tempat1.setNmTempat(c.getString(c.getColumnIndex(TEMPAT_NAMA)));
        tempat1.setLatitude(c.getString(c.getColumnIndex(TEMPAT_LATITUDE)));
        tempat1.setLongitude(c.getString(c.getColumnIndex(TEMPAT_LONGITUDE)));
        tempat1.setStatus(c.getInt(c.getColumnIndex(TEMPAT_STATUS)));

        c.close();
        db.close();

        return tempat1;
    }

    public Tempat getTempatbyID(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TEMPAT + " WHERE " + TEMPAT_ID + " = '" + id +"'";
        Log.e(LOG,selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if(c!=null){
            c.moveToFirst();
        }
        Tempat tempat1 = new Tempat();
        tempat1.setId(c.getInt(c.getColumnIndex(TEMPAT_ID)));
        tempat1.setNmTempat(c.getString(c.getColumnIndex(TEMPAT_NAMA)));
        tempat1.setLatitude(c.getString(c.getColumnIndex(TEMPAT_LATITUDE)));
        tempat1.setLongitude(c.getString(c.getColumnIndex(TEMPAT_LONGITUDE)));
        tempat1.setStatus(c.getInt(c.getColumnIndex(TEMPAT_STATUS)));

        c.close();
        db.close();

        return tempat1;
    }


    //getting id soal
    public List<Integer> getAllIdTempat(String tempat){
        List<Integer> id = new ArrayList<Integer>();
        String selectQuery = "SELECT * fROM " + TABLE_SOAL + " WHERE " + SOAL_TEMPAT + " = '" + tempat+"'";

        Log.e(LOG,selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        int temp;

        if(c.moveToFirst()){
            do{
                temp = c.getInt((c.getColumnIndex(TEMPAT_ID)));
                //adding integer ke list
                id.add(temp);
            }while (c.moveToNext());
        }

        c.close();
        db.close();
        return id;
    }



    //getting id dan tipesoal
    public List<Soal> getAllIdTipeTempat(String tempat){
        List<Soal> id = new ArrayList<Soal>();
        String selectQuery = "SELECT * fROM " + TABLE_SOAL + " WHERE " + SOAL_TEMPAT + " = '" + tempat+"'";
        Soal tempSoal = new Soal();
        Log.e(LOG,selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        int temp;
        String temptipe;

        if(c.moveToFirst()){
            do{
                temp = c.getInt((c.getColumnIndex(TEMPAT_ID)));
                temptipe = c.getString((c.getColumnIndex(SOAL_TIPE)));
                tempSoal.setId(temp);
                tempSoal.setTipeSoal(temptipe);
                //adding integer ke list
                id.add(tempSoal);
            }while (c.moveToNext());
        }

        c.close();
        db.close();
        return id;
    }

    public List<String> getAllTipeTempat(String tempat){
        List<String> id = new ArrayList<String>();
        String selectQuery = "SELECT * fROM " + TABLE_SOAL + " WHERE " + SOAL_TEMPAT + " = '" + tempat+"'";

        Log.e(LOG,selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        String temp;

        if(c.moveToFirst()){
            do{
                temp = c.getString((c.getColumnIndex(SOAL_TIPE)));
                //adding integer ke list
                id.add(temp);
            }while (c.moveToNext());
        }

        c.close();
        db.close();
        return id;
    }

    public int StatusSoal(Soal soal){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SOAL_ID, soal.getId());
        values.put(SOAL_SOAL, soal.getSoal());
        values.put(SOAL_KUNCIJAWABAN, soal.getKunciJawaban());
        values.put(SOAL_STATUS, 1);
        values.put(SOAL_TEMPAT, soal.getTempat());

        // updating row
        return db.update(TABLE_SOAL, values, SOAL_ID + " = ?",
                new String[] { String.valueOf(soal.getId()) });
    }

    public int getSumStat(Soal soal){

        SQLiteDatabase db = this.getWritableDatabase();
        int total = 0;
        Cursor c;
        c = db.rawQuery("select sum(" + SOAL_STATUS + ") from " + TABLE_SOAL +" where " +
                SOAL_TEMPAT + " = '" + soal.getTempat() + "';", null);
        if(c.moveToFirst())
            total = c.getInt(0);
        else
            total = -1;
        c.close();

        return total;
    }

    public int StatusTempat(Tempat tempat){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TEMPAT_ID, tempat.getId());
        values.put(TEMPAT_NAMA, tempat.getNmTempat());
        values.put(TEMPAT_LATITUDE, tempat.getLatitude());
        values.put(TEMPAT_LONGITUDE, tempat.getLongitude());
        values.put(TEMPAT_STATUS, 1);

        // updating row
        return db.update(TABLE_TEMPAT, values, TEMPAT_ID + " = ?",
                new String[] { String.valueOf(tempat.getId()) });
    }
}
