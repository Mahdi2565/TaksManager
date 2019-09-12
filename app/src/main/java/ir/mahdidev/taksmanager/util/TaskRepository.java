package ir.mahdidev.taksmanager.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import ir.mahdidev.taksmanager.model.UserModel;

public class TaskRepository {
    private static TaskRepository taskRepository;

    public static TaskRepository getInstance(){

        if (taskRepository==null){
            taskRepository = new TaskRepository();
        }
        return taskRepository;
    }

    public boolean insertUserToDb(String username , String password , String email ,
                               String age , boolean admin){
        Date date = new Date();
        ContentValues contentValues = new ContentValues();
        boolean isInsertToDb  ;

        contentValues.put(Const.DB.TABLE_USER_USERNAME , username);
        contentValues.put(Const.DB.TABLE_USER_PASSWORD , password);
        contentValues.put(Const.DB.TABLE_USER_EMAIL , email);
        contentValues.put(Const.DB.TABLE_USER_AGE , age);
        contentValues.put(Const.DB.TABLE_USER_IS_ADMIN , admin?1:0 );
        contentValues.put(Const.DB.TABLE_USER_REGISTER_DATE , date.toString());

       isInsertToDb = G.DB.insert(Const.DB.DB_TABLE_USER , null , contentValues) > 0;
       G.DB.close();
        return isInsertToDb;
    }

    public boolean checkUserExists(String userName) {

        String dbUsername;
        Cursor cursor = G.DB.rawQuery("SELECT * FROM " + Const.DB.DB_TABLE_USER , null );
        while (cursor.moveToNext()){
            dbUsername = cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_USER_USERNAME));
            if (dbUsername.equals(userName)){
                return true;
            }
        }
        cursor.close();
        return false ;
    }
    public UserModel signIn(String userName , String password){
        String dbUsername;
        String dbPassword;
        UserModel userModel = new UserModel();
        Cursor cursor = G.DB.rawQuery("SELECT * FROM " + Const.DB.DB_TABLE_USER , null );
        while (cursor.moveToNext()){
            dbUsername = cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_USER_USERNAME));
            dbPassword = cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_USER_PASSWORD));
            if (dbUsername.equals(userName) && dbPassword.equals(password)){
                updateLoggedIn(dbUsername , 1);
                setUserModel(userModel , cursor);
               return userModel ;
            }
        }
        cursor.close();
        return null ;
    }

    public void updateLoggedIn(String username , int loggedIn){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Const.DB.TABLE_USER_IS_LOGGED_IN , loggedIn);

        String [] args = {username};
        G.DB.update(Const.DB.DB_TABLE_USER ,contentValues , Const.DB.TABLE_USER_USERNAME
                + " = ?" ,args );
    }

    public UserModel UserLoggedIn(){
        UserModel userModel = new UserModel();
        Cursor cursor = G.DB.rawQuery("SELECT * FROM " + Const.DB.DB_TABLE_USER , null );
        int isLoggedIn ;
        while (cursor.moveToNext()){
            isLoggedIn = cursor.getInt(cursor.getColumnIndex(Const.DB.TABLE_USER_IS_LOGGED_IN));
            if (isLoggedIn == 1){
               setUserModel(userModel , cursor);
                return userModel;
            }
        }
        cursor.close();
        return null;
    }

    private void setUserModel(UserModel userModel , Cursor cursor){
        userModel.setId(cursor.getInt(cursor.getColumnIndex(Const.DB.TABLE_USER_ID)));
        userModel.setUserName(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_USER_USERNAME)));
        userModel.setPassword(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_USER_PASSWORD)));
        userModel.setAge(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_USER_AGE)));
        userModel.setEmail(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_USER_EMAIL)));
        userModel.setIsLoggedIn(cursor.getInt(cursor.getColumnIndex(Const.DB.TABLE_USER_IS_LOGGED_IN)));
        userModel.setIsAdmin(cursor.getInt(cursor.getColumnIndex(Const.DB.TABLE_USER_IS_ADMIN)));
        userModel.setRegisterDate(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_USER_REGISTER_DATE)));
    }

}
