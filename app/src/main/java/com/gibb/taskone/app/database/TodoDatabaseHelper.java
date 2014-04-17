package com.gibb.taskone.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by emictr on 16.04.14.
 */
public class TodoDatabaseHelper extends SQLiteOpenHelper {

    /***
     * Konstanten zur Erstellung der DB
     */

    private static final String DATABASE_NAME = "todotable.db";
    private static final int DATABASE_VERSION = 1;


    /***
     * Konstruktor, erstellt DB
     * @param context Zu übergebender Kontext (-->"Zustand" der Applikation)
     */
    public TodoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /***
     * Erstellt nach Erzeugung der DB die Tabellen in der TodoTable-Klasse
     * @param database Datenbank, in der die Tabelle erstellt werden soll
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        TodoTable.onCreate(database);
    }

    /***
     * Methode zur Aktualisierung (Versionsnummer) der DB, ruft die entsprechende Methode der TodoTable-Klasse auf
     * @param database Zu aktualisierende Datenbank
     * @param oldVersion Versionsnummer alt
     * @param newVersion Versionsnummer neu
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        TodoTable.onUpgrade(database, oldVersion, newVersion);
    }

}
