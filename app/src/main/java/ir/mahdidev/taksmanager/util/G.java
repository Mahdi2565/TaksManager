package ir.mahdidev.taksmanager.util;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import ir.mahdidev.taksmanager.model.DaoMaster;
import ir.mahdidev.taksmanager.model.DaoSession;
import ir.mahdidev.taksmanager.model.TaskModelDao;
import ir.mahdidev.taksmanager.model.UserModelDao;

public class G extends Application {

    public static SQLiteDatabase DB ;
    public static G mInstance;
    private   DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        createGreenDaoDb();
        createDB();
        mInstance = this;
    }

    private void createGreenDaoDb() {
    daoSession = new DaoMaster(new GreenDaoHelper(this , "takManagerGreenDao.db").getWritableDb()).newSession();

    }

    public DaoSession getDaoSession() {
        return daoSession;
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
