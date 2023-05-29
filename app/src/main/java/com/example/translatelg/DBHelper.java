package com.example.translatelg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TranslationHistory.db";
    private static final String TABLE_TRANSLATION_HISTORY = "translation_history";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SOURCE_TEXT = "source_text";
    private static final String COLUMN_TRANSLATED_TEXT = "translated_text";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TRANSLATION_HISTORY_TABLE = "CREATE TABLE " + TABLE_TRANSLATION_HISTORY + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_SOURCE_TEXT + " TEXT,"
                + COLUMN_TRANSLATED_TEXT + " TEXT"
                + ")";
        db.execSQL(CREATE_TRANSLATION_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSLATION_HISTORY);
        onCreate(db);
    }

    public void insertTranslation(String sourceText, String translatedText) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SOURCE_TEXT, sourceText);
        values.put(COLUMN_TRANSLATED_TEXT, translatedText);
        db.insert(TABLE_TRANSLATION_HISTORY, null, values);
        db.close();
    }

    Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_TRANSLATION_HISTORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }


    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TRANSLATION_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSLATION_HISTORY);
        onCreate(db);
        db.close();
    }
}
