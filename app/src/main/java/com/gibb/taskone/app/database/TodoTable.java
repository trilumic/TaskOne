package com.gibb.taskone.app.database;

/**
 * Created by emictr on 16.04.14.
 */

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TodoTable {


    /***
     * Konstanten zurm Erstellung der Tabelle(n)
     */
    public static final String TABLE_TODO = "todo";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_SUMMARY = "summary";
    public static final String COLUMN_DESCRIPTION = "description";


    /***
     * SQL-Statement zur Tabellenerzeugung
     */
    private static final String DATABASE_CREATE = "create table "
            + TABLE_TODO
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_CATEGORY + " text not null, "
            + COLUMN_SUMMARY + " text not null,"
            + COLUMN_DESCRIPTION
            + " text not null"
            + ");";

    //
    /***
     * onCreate() und onUpdate(): Zu implementierende Methoden der SQLiteDatabase-Klasse
     * onCreate(): Erstellt Tabelle(n)
     * @param database Datenbank, in der die Tabelle erstellt werden soll (Ziel für execSQL())
     */
    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    /***
     * Upgrade der DB
     * @param database Ziel-DB
     * @param oldVersion Alte Versionsnummer
     * @param newVersion Neue Versionsnummer
     */
    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(TodoTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        onCreate(database);
    }
}
