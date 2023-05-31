package com.example.translatelg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TranslationLG.db";
    //lỊCH SỬ
    private static final String TABLE_TRANSLATION_HISTORY = "translation_history";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SOURCE_TEXT = "source_text";
    private static final String COLUMN_TRANSLATED_TEXT = "translated_text";
    //NOTE
    private static final String TABLE_TRANSLATION_NOTE = "translation_note";
    private static final String COLUMN_ID_NOTE = "id";
    private static final String COLUMN_SOURCE_TITLE = "source_title";
    private static final String COLUMN_CONTEXT_TEXT = "context_text";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
// Tạo bảng
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TRANSLATION_HISTORY_TABLE = "CREATE TABLE " + TABLE_TRANSLATION_HISTORY + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_SOURCE_TEXT + " TEXT,"
                + COLUMN_TRANSLATED_TEXT + " TEXT"
                + ")";
        String CREATE_TRANSLATION_NOTE_TABLE = "CREATE TABLE " + TABLE_TRANSLATION_NOTE + "("
                + COLUMN_ID_NOTE + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_SOURCE_TITLE + " TEXT,"
                + COLUMN_CONTEXT_TEXT + " TEXT"
                + ")";
        db.execSQL(CREATE_TRANSLATION_NOTE_TABLE);
        db.execSQL(CREATE_TRANSLATION_HISTORY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSLATION_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSLATION_NOTE);
        onCreate(db);
    }

    // lấy dữ liệu dịch
    public void insertTranslation(String sourceText, String translatedText) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SOURCE_TEXT, sourceText);
        values.put(COLUMN_TRANSLATED_TEXT, translatedText);
        db.insert(TABLE_TRANSLATION_HISTORY, null, values);
        db.close();
    }

    //lưu lại và thêm vào bàng
    public void addContent(String source_title, String context_text) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SOURCE_TITLE, source_title);
        values.put(COLUMN_CONTEXT_TEXT, context_text);
        db.insert(TABLE_TRANSLATION_NOTE, null, values);
        db.close();
    }

    // hiển thị dữ liệu dịch
    Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_TRANSLATION_HISTORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    // hiển thị dữ liệu ghi chú
    Cursor readAllDataNote() {
        String query = "SELECT * FROM " + TABLE_TRANSLATION_NOTE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    //xóa tất cả dữ liệu trong bảng
    public void deleteAllDataTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TRANSLATION_HISTORY);
        db.close();
    }
// cập nhật ghi chú
    public void UpdateDataNote(String rowid, String source_title, String context_text) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SOURCE_TITLE, source_title);
        values.put(COLUMN_CONTEXT_TEXT, context_text);
        db.update(TABLE_TRANSLATION_NOTE, values, "id=?", new String[]{rowid});
        db.close();
    }
// xóa ghi chú
    public void DeleteDataNote(String rowid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSLATION_NOTE, "id=?", new String[]{rowid});
        db.close();
    }


}

