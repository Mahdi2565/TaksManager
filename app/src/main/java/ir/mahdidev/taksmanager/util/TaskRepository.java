package ir.mahdidev.taksmanager.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    public byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }

    public boolean insertUserToDb(String username , String password , String email ,
                                  String age , boolean admin , Bitmap imageUser){
        Date date = new Date();
        ContentValues contentValues = new ContentValues();
        boolean isInsertToDb  ;
        byte[] image = getBitmapAsByteArray(imageUser);
        contentValues.put(Const.DB.TABLE_USER_USERNAME , username);
        contentValues.put(Const.DB.TABLE_USER_PASSWORD , password);
        contentValues.put(Const.DB.TABLE_USER_EMAIL , email);
        contentValues.put(Const.DB.TABLE_USER_AGE , age);
        contentValues.put(Const.DB.TABLE_USER_IS_ADMIN , admin?1:0 );
        contentValues.put(Const.DB.TABLE_USER_REGISTER_DATE , date.toString());
        contentValues.put(Const.DB.TABLE_USER_IMAGE , image );

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
       // userModel.setImageUser(imageUser);
     //   userModel.setImageUser(BitmapFactory.decodeByteArray(imageUser, 0, imageUser.length));
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
            taskModel.setDate(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_TASK_DATE)));
            taskModel.setTime(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_TASK_TIME)));
            taskList.add(taskModel);
        }
        if (cursor != null){
            cursor.close();
        }
        return taskList ;
    }

    public TaskModel readTask ( int userId ,int taskId){
        Cursor cursor = G.DB.rawQuery("SELECT * FROM " + Const.DB.DB_TABLE_TASK + " " +
                "WHERE " + Const.DB.TABLE_TASK_ID + " = " +taskId + " AND " +
                Const.DB.TABLE_TASK_USER_ID + " = " + userId, null);
        while (cursor.moveToNext()){
            TaskModel taskModel = new TaskModel();
            taskModel.setId(cursor.getInt(cursor.getColumnIndex(Const.DB.TABLE_TASK_ID)));
            taskModel.setUserId(cursor.getInt(cursor.getColumnIndex(Const.DB.TABLE_TASK_USER_ID)));
            taskModel.setTitle(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_TASK_TITLE)));
            taskModel.setDescription(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_TASK_DESCRIPTION)));
            taskModel.setStatus(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_TASK_STATUS)));
            taskModel.setDate(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_TASK_DATE)));
            taskModel.setTime(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_TASK_TIME)));
            return taskModel;
        }
        if (cursor != null){
            cursor.close();
        }
        return null ;
    }

    public boolean insertTask( int userId , String title , String description ,
                              String status , String date , String time){
        ContentValues contentValues = new ContentValues();
        boolean isInsert ;

        contentValues.put(Const.DB.TABLE_TASK_USER_ID , userId);
        contentValues.put(Const.DB.TABLE_TASK_TITLE , title);
        contentValues.put(Const.DB.TABLE_TASK_DESCRIPTION , description);
        contentValues.put(Const.DB.TABLE_TASK_STATUS , status);
        contentValues.put(Const.DB.TABLE_TASK_DATE , date);
        contentValues.put(Const.DB.TABLE_TASK_TIME , time);
       isInsert =  G.DB.insert(Const.DB.DB_TABLE_TASK , null , contentValues) > 0;

       return isInsert;
    }

    public boolean updateTask( int taskId ,int userId , String title , String description ,
                              String status , String date , String time){
        boolean isUpdate ;
        ContentValues contentValues = new ContentValues();
        contentValues.put(Const.DB.TABLE_TASK_USER_ID , userId);
        contentValues.put(Const.DB.TABLE_TASK_TITLE , title);
        contentValues.put(Const.DB.TABLE_TASK_DESCRIPTION , description);
        contentValues.put(Const.DB.TABLE_TASK_STATUS , status);
        contentValues.put(Const.DB.TABLE_TASK_DATE , date);
        contentValues.put(Const.DB.TABLE_TASK_TIME , time);

        isUpdate =G.DB.update(Const.DB.DB_TABLE_TASK , contentValues , Const.DB.TABLE_TASK_ID + " = " +
                taskId + " AND " + Const.DB.TABLE_TASK_USER_ID + " = " + userId , null) > 0;
        return isUpdate;
    }
    public boolean deleteTask (int taskId , int userId){
        boolean isDeleted ;
       isDeleted = G.DB.delete(Const.DB.DB_TABLE_TASK , Const.DB.TABLE_TASK_ID + " = " + taskId +" " +
                " AND " + Const.DB.TABLE_TASK_USER_ID + " = " + userId , null) > 0;
        return isDeleted ;
    }

    public boolean deleteTask (int userId){
        boolean isDeleted ;
        isDeleted = G.DB.delete(Const.DB.DB_TABLE_TASK ,  Const.DB.TABLE_TASK_USER_ID + " = " + userId , null) > 0;
        return isDeleted ;
    }

    public UserModel readUserProfile(int userId){
        Cursor cursor = G.DB.rawQuery("SELECT * FROM " + Const.DB.DB_TABLE_USER + " WHERE "
        + Const.DB.TABLE_USER_ID + " = " + userId , null);

        while (cursor.moveToNext()){
            UserModel userModel = new UserModel();

            byte[] imageUser = cursor.getBlob(cursor.getColumnIndex(Const.DB.TABLE_USER_IMAGE));
            userModel.setId(cursor.getInt(cursor.getColumnIndex(Const.DB.TABLE_USER_ID)));
            userModel.setUserName(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_USER_USERNAME)));
            userModel.setPassword(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_USER_PASSWORD)));
            userModel.setAge(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_USER_AGE)));
            userModel.setEmail(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_USER_EMAIL)));
            userModel.setIsLoggedIn(cursor.getInt(cursor.getColumnIndex(Const.DB.TABLE_USER_IS_LOGGED_IN)));
            userModel.setIsAdmin(cursor.getInt(cursor.getColumnIndex(Const.DB.TABLE_USER_IS_ADMIN)));
            userModel.setRegisterDate(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_USER_REGISTER_DATE)));
            userModel.setImageUser(imageUser);
            setUserModel(userModel , cursor);
            return userModel ;
        }
        return null ;
    }

    public boolean updateUser(int userId  , String password , String email ,
                              String age , boolean admin , Bitmap imageUser){

        ContentValues contentValues = new ContentValues();
        boolean isUpdated  ;
        byte[] image = getBitmapAsByteArray(imageUser);
        contentValues.put(Const.DB.TABLE_USER_PASSWORD , password);
        contentValues.put(Const.DB.TABLE_USER_EMAIL , email);
        contentValues.put(Const.DB.TABLE_USER_AGE , age);
        contentValues.put(Const.DB.TABLE_USER_IS_ADMIN , admin?1:0 );
        contentValues.put(Const.DB.TABLE_USER_IMAGE , image );

        isUpdated = G.DB.update(Const.DB.DB_TABLE_USER , contentValues , Const.DB.TABLE_USER_ID + " = " + userId , null
         ) > 0;

        return isUpdated ;
    }

   /* public void insertTestData(){
        for (int i = 1 ; i<15 ; i++){
            ContentValues insertValues = new ContentValues();
            DateFormat date = new SimpleDateFormat("MMM dd yyyy" , Locale.US);
            String dateFormatted = date.format(Calendar.getInstance().getTime());
            DateFormat time = new SimpleDateFormat("hh:mm a" , Locale.US);
            String timeFormatted = time.format(Calendar.getInstance().getTime());
            insertValues.put("user_id", 1);
            insertValues.put("title", "Test" + i);
            insertValues.put("description", "04/06/2011");
            insertValues.put("status", "DOING");
            insertValues.put("date" , dateFormatted);
            insertValues.put("time" , timeFormatted);
            G.DB.insert("task", null, insertValues);
        }
    }
*/

}
