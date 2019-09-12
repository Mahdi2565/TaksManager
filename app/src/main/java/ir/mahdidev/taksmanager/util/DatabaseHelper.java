package ir.mahdidev.taksmanager.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context) {
        super(context, Const.DB.DB_NAME, null, Const.DB.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createDB(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void createDB(SQLiteDatabase db){
        db.execSQL(Const.DB.CREATE_USER_TABLE);
        db.execSQL(Const.DB.CREATE_TASK_TABLE);
    }
}
