package ir.mahdidev.taksmanager.util;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

public class G extends Application {

    public static SQLiteDatabase DB ;
    private static G mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        createDB();

        mInstance = this;
    }
    public static synchronized G getInstance() {
        return mInstance;
    }
    private void createDB(){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        DB = databaseHelper.getWritableDatabase();
    }
    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
