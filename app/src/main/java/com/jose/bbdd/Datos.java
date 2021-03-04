package com.jose.bbdd;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Datos extends SQLiteOpenHelper {

    String crearTablaCitas = "CREATE TABLE citas(id INTEGER PRIMARY KEY AUTOINCREMENT, fecha TEXT, hora TEXT, asunto TEXT)";

    public Datos(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(crearTablaCitas);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS citas");
        db.execSQL(crearTablaCitas);
    }



}
