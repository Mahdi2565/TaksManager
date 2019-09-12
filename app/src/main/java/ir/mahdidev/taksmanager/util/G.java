package ir.mahdidev.taksmanager.util;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class G extends Application {

    public static SQLiteDatabase DB ;
    @Override
    public void onCreate() {
        super.onCreate();
        createDB();
    }

    private void createDB(){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        DB = databaseHelper.getWritableDatabase();
    }

}
