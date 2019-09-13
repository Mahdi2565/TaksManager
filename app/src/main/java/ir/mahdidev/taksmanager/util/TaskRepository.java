package ir.mahdidev.taksmanager.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import ir.mahdidev.taksmanager.model.TaskModel;
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
        if (cursor != null) {
            cursor.close();
        }        return false ;
    }
    //Todo: fix cursor problem

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
        if (cursor != null) {
            cursor.close();
        }

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
        if (cursor != null) {
            cursor.close();
        }        return null;
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

    public ArrayList<TaskModel> readTask (String status , int userId){
        ArrayList<TaskModel> taskList = new ArrayList<>();
        Cursor cursor = G.DB.rawQuery("SELECT * FROM " + Const.DB.DB_TABLE_TASK + " " +
                "WHERE " + Const.DB.TABLE_TASK_STATUS + " = " +"\"" + status + "\"" +" AND " +
                Const.DB.TABLE_TASK_USER_ID + " = " + userId, null);
        while (cursor.moveToNext()){
            TaskModel taskModel = new TaskModel();
            taskModel.setId(cursor.getInt(cursor.getColumnIndex(Const.DB.TABLE_TASK_ID)));
            taskModel.setUserId(cursor.getInt(cursor.getColumnIndex(Const.DB.TABLE_TASK_USER_ID)));
            taskModel.setTitle(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_TASK_TITLE)));
            taskModel.setDescription(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_TASK_DESCRIPTION)));
            taskModel.setStatus(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_TASK_STATUS)));

            taskList.add(taskModel);
        }
        if (cursor != null){
            cursor.close();
        }
        return taskList ;
    }

    //Todo: Delete this test method
    public void insertTestData(){
        for (int i = 1 ; i<15 ; i++){
            ContentValues insertValues = new ContentValues();
            insertValues.put("id", i );
            insertValues.put("user_id", 2);
            insertValues.put("title", "Test" + i);
            insertValues.put("description", "04/06/2011");
            insertValues.put("status", "DOING");
            G.DB.insert("task", null, insertValues);
        }
    }

}
