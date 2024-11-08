package com.example.final_project_admin_new.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.final_project_admin_new.model.ClassInstance;
import com.example.final_project_admin_new.model.YogaClass;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "YogaClassDB";
    private static final int DATABASE_VERSION = 1;
    //yoga class
    public static final String TABLE_YOGA_CLASS = "yoga_class";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DAY_OF_WEEK = "dayOfWeek";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_CAPACITY = "capacity";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_CLASS_TYPE = "classType";
    public static final String COLUMN_DESCRIPTION = "description";


    //class instance
    public static final String TABLE_CLASS_INSTANCE = "class_instance";
    public static final String COLUMN_INSTANCE_ID = "instance_id";
    public static final String COLUMN_CLASS_ID = "class_id"; // Foreign key to YogaClass
    public static final String COLUMN_INSTANCE_DATE = "instance_date";
    public static final String COLUMN_INSTANCE_TEACHER = "teacher";
    public static final String COLUMN_INSTANCE_COMMENTS = "comments";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_YOGA_CLASS_TABLE = "CREATE TABLE " + TABLE_YOGA_CLASS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DAY_OF_WEEK + " TEXT NOT NULL, "
                + COLUMN_TIME + " TEXT NOT NULL, "
                + COLUMN_CAPACITY + " INTEGER NOT NULL, "
                + COLUMN_DURATION + " INTEGER NOT NULL, "
                + COLUMN_PRICE + " REAL NOT NULL, "
                + COLUMN_CLASS_TYPE + " TEXT NOT NULL, "
                + COLUMN_DESCRIPTION + " TEXT"
                + ")";
        db.execSQL(CREATE_YOGA_CLASS_TABLE);
        String CREATE_CLASS_INSTANCE_TABLE = "CREATE TABLE " + TABLE_CLASS_INSTANCE + "("
                + COLUMN_INSTANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CLASS_ID + " INTEGER NOT NULL, "
                + COLUMN_INSTANCE_DATE + " TEXT NOT NULL, "
                + COLUMN_INSTANCE_TEACHER + " TEXT NOT NULL, "
                + COLUMN_INSTANCE_COMMENTS + " TEXT, "
                + "FOREIGN KEY(" + COLUMN_CLASS_ID + ") REFERENCES " + TABLE_YOGA_CLASS + "(" + COLUMN_ID + ") ON DELETE CASCADE"
                + ")";
        db.execSQL(CREATE_CLASS_INSTANCE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_YOGA_CLASS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASS_INSTANCE);
        onCreate(db);
    }

    // yoga class
    public void addYogaClass(YogaClass yogaClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DAY_OF_WEEK, yogaClass.getDayOfWeek());
        values.put(COLUMN_TIME, yogaClass.getTime());
        values.put(COLUMN_CAPACITY, yogaClass.getCapacity());
        values.put(COLUMN_DURATION, yogaClass.getDuration());
        values.put(COLUMN_PRICE, yogaClass.getPrice());
        values.put(COLUMN_CLASS_TYPE, yogaClass.getClassType());
        values.put(COLUMN_DESCRIPTION, yogaClass.getDescription());
        db.insert(TABLE_YOGA_CLASS, null, values);
        db.close();
    }

    public List<YogaClass> getAllYogaClasses() {
        ArrayList<YogaClass> yogaClassList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_YOGA_CLASS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                YogaClass yogaClass = new YogaClass(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAY_OF_WEEK)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CAPACITY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURATION)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASS_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                );
                yogaClass.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                yogaClassList.add(yogaClass);
            } while (cursor.moveToNext());
        }

        return yogaClassList;
    }
    public void deleteYogaClass(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_YOGA_CLASS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }
    public Cursor getClassDetails(int classId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_YOGA_CLASS + " WHERE " + COLUMN_ID + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(classId)});
    }
    public boolean updateClassDetails(int classId, String dayOfWeek, String time, int capacity, int duration, double price, String classType, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DAY_OF_WEEK, dayOfWeek);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_CAPACITY, capacity);
        values.put(COLUMN_DURATION, duration);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_CLASS_TYPE, classType);
        values.put(COLUMN_DESCRIPTION, description);

        int rowsUpdated = db.update(TABLE_YOGA_CLASS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(classId)});
        return rowsUpdated > 0;
    }


    //class Instance
    public void addClassInstance(ClassInstance classInstance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLASS_ID, classInstance.getClassId());
        values.put(COLUMN_INSTANCE_DATE, classInstance.getDate());
        values.put(COLUMN_INSTANCE_TEACHER, classInstance.getTeacher());
        values.put(COLUMN_INSTANCE_COMMENTS, classInstance.getComments());
        db.insert(TABLE_CLASS_INSTANCE, null, values);
        db.close();
    }

    // Lấy tất cả các class instance của một yoga class
    public List<ClassInstance> getClassInstancesByClassId(int classId) {
        List<ClassInstance> classInstanceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CLASS_INSTANCE, null, COLUMN_CLASS_ID + " = ?", new String[]{String.valueOf(classId)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                ClassInstance instance = new ClassInstance(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTANCE_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTANCE_TEACHER)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTANCE_COMMENTS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CLASS_ID))
                );
                classInstanceList.add(instance);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return classInstanceList;
    }

    // Xóa một class instance
    public void deleteClassInstance(int instanceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CLASS_INSTANCE, COLUMN_INSTANCE_ID + " = ?", new String[]{String.valueOf(instanceId)});
        db.close();
    }
}

