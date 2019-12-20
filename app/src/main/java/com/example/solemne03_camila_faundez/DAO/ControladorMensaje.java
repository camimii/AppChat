package com.example.solemne03_camila_faundez.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;


import java.util.ArrayList;

public class ControladorMensaje extends DB {
    public ControladorMensaje(@Nullable Context context) {
        super(context);
    }

    public boolean insertar(String msj) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            if(!msj.isEmpty()) {
                ContentValues valores = new ContentValues();
                valores.put("MENSAJE", msj);
                db.insert("TEXTO", null, valores);
                return true;
            }
        } finally {
            db.close();
        }
        return false;
    }

    public boolean insertarTodo(ArrayList<String> datos){
        SQLiteDatabase db = getWritableDatabase();
        try {
            if(!datos.isEmpty()) {
                for(int x=0;x<datos.size();x++){
                    ContentValues valores = new ContentValues();
                    valores.put("MENSAJE", datos.get(x));
                    db.insert("TEXTO", null, valores);
                    return true;
                }

            }
        } finally {
            db.close();
        }
        return false;
    }

    public ArrayList<String> obtener() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TEXTO", null);
        ArrayList<String> msjs = new ArrayList<>();
        /*try {
            while (cursor.moveToNext()) {
                String msjDb = cursor.getString(cursor.getColumnIndex("MENSAJE"));
                msjs.add(msjDb);
                return msjs;
            }
        }
        finally {
            cursor.close();
        }*/
        if (cursor.moveToFirst()) {
            do {
                String msjDb = cursor.getString(cursor.getColumnIndex("MENSAJE"));
                msjs.add(msjDb);

            } while (cursor.moveToNext());
        }

        return msjs;
    }

    public void eliminarTabla(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("TEXTO",null,null);
    }
}
