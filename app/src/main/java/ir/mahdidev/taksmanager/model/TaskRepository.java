package ir.mahdidev.taksmanager.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;

import org.greenrobot.greendao.query.DeleteQuery;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import ir.mahdidev.taksmanager.util.Const;
import ir.mahdidev.taksmanager.util.G;
import ir.mahdidev.taksmanager.util.UserCursorWrapper;

public class TaskRepository {
    private static TaskRepository taskRepository;
    private G global = G.getInstance() ;
    private   DaoSession daoSession;
    private   TaskModelDao taskModelDao ;
    private   UserModelDao userModelDao ;

    public static TaskRepository getInstance(){
        if (taskRepository==null){
            taskRepository = new TaskRepository();
        }
        return taskRepository;
    }

    private TaskRepository(){
        daoSession = global.getDaoSession();
        taskModelDao = daoSession.getTaskModelDao();
        userModelDao = daoSession.getUserModelDao();
    }

    public byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }

    public boolean insertUserToDb(UserModel userModel){
      /*
        ContentValues contentValues = new ContentValues();
        contentValues.put(Const.DB.TABLE_USER_USERNAME , userModel.getUserName());
        contentValues.put(Const.DB.TABLE_USER_PASSWORD , userModel.getPassword());
        contentValues.put(Const.DB.TABLE_USER_EMAIL , userModel.getEmail());
        contentValues.put(Const.DB.TABLE_USER_AGE , userModel.getAge());
        contentValues.put(Const.DB.TABLE_USER_IS_ADMIN , userModel.getIsAdmin() );
        contentValues.put(Const.DB.TABLE_USER_REGISTER_DATE , userModel.getRegisterDate());
        contentValues.put(Const.DB.TABLE_USER_IMAGE , userModel.getImageUser() );

       isInsertToDb = G.DB.insert(Const.DB.DB_TABLE_USER , null , contentValues) > 0;
*/
        try {
            userModelDao.insert(userModel);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean checkUserExists(String userName) {

        return userModelDao.queryBuilder().where(UserModelDao.Properties.UserName.eq(userName))
                .unique() != null;
        /*
        Cursor cursor =  G.DB.rawQuery("SELECT * FROM " + Const.DB.DB_TABLE_USER , null );
        UserCursorWrapper cursorWrapper = new UserCursorWrapper(cursor);
        while (cursor.moveToNext()){
            if (cursorWrapper.checkUserExists().equals(userName)){
                return true;
            }
        }
        if (cursor != null) {
            cursor.close();
        }        return false ;
        */

    }
    public UserModel signIn(String userName , String password){

        /*
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
*/
        UserModel userModel = userModelDao.queryBuilder().where(UserModelDao.Properties.UserName.eq(userName) ,
                UserModelDao.Properties.Password.eq(password))
                .unique() ;
        if ( userModel != null){
            updateLoggedIn(userName , 1);
            return userModel ;
        }else return null;

    }

    public void updateLoggedIn(String username , int loggedIn){
        UserModel userModel = userModelDao.queryBuilder().where(UserModelDao.Properties.UserName.eq(username))
                .unique();
        userModel.setIsLoggedIn(loggedIn);
        userModelDao.update(userModel);
        /*
        ContentValues contentValues = new ContentValues();
        contentValues.put(Const.DB.TABLE_USER_IS_LOGGED_IN , loggedIn);

        String [] args = {username};
        G.DB.update(Const.DB.DB_TABLE_USER ,contentValues , Const.DB.TABLE_USER_USERNAME
                + " = ?" ,args );
                */

    }

    public UserModel UserLoggedIn(){
        UserModel userModel = userModelDao.queryBuilder().where(UserModelDao.Properties.IsLoggedIn.eq(1))
                .unique();
        if (userModel != null){
            return userModel;
        }else return null;
        /*
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

         */
    }

    private void setUserModel(UserModel userModel , Cursor cursor){
        userModel.setId(cursor.getLong(cursor.getColumnIndex(Const.DB.TABLE_USER_ID)));
        userModel.setUserName(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_USER_USERNAME)));
        userModel.setPassword(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_USER_PASSWORD)));
        userModel.setAge(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_USER_AGE)));
        userModel.setEmail(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_USER_EMAIL)));
        userModel.setIsLoggedIn(cursor.getInt(cursor.getColumnIndex(Const.DB.TABLE_USER_IS_LOGGED_IN)));
        userModel.setIsAdmin(cursor.getInt(cursor.getColumnIndex(Const.DB.TABLE_USER_IS_ADMIN)));
        userModel.setRegisterDate(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_USER_REGISTER_DATE)));
    }



    public ArrayList<TaskModel> readTask (String status , long userId){

        /*
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

         */
        return (ArrayList<TaskModel>) taskModelDao.queryBuilder().where(TaskModelDao.Properties.Status.eq(status) , TaskModelDao
                .Properties.UserId.eq(userId)).list();
    }

    public ArrayList<TaskModel> readTask (String status){
        /*
        ArrayList<TaskModel> taskList = new ArrayList<>();
        Cursor cursor = G.DB.rawQuery("SELECT * FROM " + Const.DB.DB_TABLE_TASK + " " +
                "WHERE " + Const.DB.TABLE_TASK_STATUS + " = " +"\"" + status + "\"" , null);
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

         */
        return (ArrayList<TaskModel>) taskModelDao.queryBuilder()
                .where(TaskModelDao.Properties.Status.eq(status)).list() ;
    }

    public TaskModel readTask (UUID uuid){
        return taskModelDao.queryBuilder()
                .where(TaskModelDao.Properties.Uuid.eq(uuid)).unique() ;
    }

    public TaskModel readTask ( long userId ,long taskId){
        TaskModel taskModel = taskModelDao.queryBuilder().where(TaskModelDao.Properties.UserId.eq(userId) ,
                TaskModelDao.Properties.Id.eq(taskId)).unique();
        if (taskModel != null) return taskModel ;
        else return null ;
        /*
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

         */

    }

    public TaskModel readTask (long taskId){
        TaskModel taskModel = taskModelDao.queryBuilder()
                .where(TaskModelDao.Properties.Id.eq(taskId)).unique();
        if (taskModel != null) return taskModel ;
        else return null ;
        /*
        Cursor cursor = G.DB.rawQuery("SELECT * FROM " + Const.DB.DB_TABLE_TASK + " " +
                "WHERE " + Const.DB.TABLE_TASK_ID + " = " +taskId , null);
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

         */
    }

    public boolean insertTask( TaskModel taskModel){
        /*
        ContentValues contentValues = new ContentValues();
        boolean isInsert ;
        contentValues.put(Const.DB.TABLE_TASK_USER_ID , taskModel.getUserId());
        contentValues.put(Const.DB.TABLE_TASK_TITLE , taskModel.getTitle());
        contentValues.put(Const.DB.TABLE_TASK_DESCRIPTION , taskModel.getDescription());
        contentValues.put(Const.DB.TABLE_TASK_STATUS , taskModel.getStatus());
        contentValues.put(Const.DB.TABLE_TASK_DATE , taskModel.getDate());
        contentValues.put(Const.DB.TABLE_TASK_TIME , taskModel.getTime());
       isInsert =  G.DB.insert(Const.DB.DB_TABLE_TASK , null , contentValues) > 0;
*/

        try {
            taskModelDao.insert(taskModel);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean updateTask( TaskModel taskModel){
        try {
            taskModelDao.update(taskModel);
            return true;
        }catch (Exception e){
            return false;
        }
        /*
        boolean isUpdate ;
        ContentValues contentValues = new ContentValues();
        contentValues.put(Const.DB.TABLE_TASK_TITLE , taskModel.getTitle());
        contentValues.put(Const.DB.TABLE_TASK_DESCRIPTION , taskModel.getDescription());
        contentValues.put(Const.DB.TABLE_TASK_STATUS , taskModel.getStatus());
        contentValues.put(Const.DB.TABLE_TASK_DATE , taskModel.getDate());
        contentValues.put(Const.DB.TABLE_TASK_TIME , taskModel.getTime());

        isUpdate =G.DB.update(Const.DB.DB_TABLE_TASK , contentValues , Const.DB.TABLE_TASK_ID + " = " +
                taskModel.getId() , null) > 0;
        return isUpdate;

         */
    }
    public boolean deleteTask (long taskId , long userId){
       TaskModel taskModel = taskModelDao.queryBuilder().where(TaskModelDao.Properties.Id.eq(taskId) ,
                TaskModelDao.Properties.UserId.eq(userId))
                .unique();

        try {
            taskModelDao.delete(taskModel);
            return true;
        }catch (Exception e){
            return false;
        }
        /*
        boolean isDeleted ;
       isDeleted = G.DB.delete(Const.DB.DB_TABLE_TASK , Const.DB.TABLE_TASK_ID + " = " + taskId +" " +
                " AND " + Const.DB.TABLE_TASK_USER_ID + " = " + userId , null) > 0;
        return isDeleted ;

         */
    }

    public boolean deleteTask (long userId){

        try {
         DeleteQuery<TaskModel> deleteQuery = taskModelDao.queryBuilder()
                    .where(TaskModelDao.Properties.UserId.eq(userId))
                    .buildDelete();
            deleteQuery.executeDeleteWithoutDetachingEntities();
            daoSession.clear();

            return true;
        }catch (Exception e){
            return false;
        }
        /*
        boolean isDeleted ;
        isDeleted = G.DB.delete(Const.DB.DB_TABLE_TASK ,  Const.DB.TABLE_TASK_USER_ID + " = " + userId , null) > 0;
        Log.e("TAG4" , isDeleted + "");
        return isDeleted ;

         */
    }
    public UserModel readUserProfile(long userId){
        UserModel userModel = userModelDao.queryBuilder().where(UserModelDao.Properties.Id.eq(userId))
                .unique();
        if (userModel != null) return userModel;
        else return null;
        /*
        Cursor cursor = G.DB.rawQuery("SELECT * FROM " + Const.DB.DB_TABLE_USER + " WHERE "
        + Const.DB.TABLE_USER_ID + " = " + userId , null);

        while (cursor.moveToNext()){
            UserModel userModel = new UserModel();

            byte[] imageUser = cursor.getBlob(cursor.getColumnIndex(Const.DB.TABLE_USER_IMAGE));
            userModel.setId(cursor.getLong(cursor.getColumnIndex(Const.DB.TABLE_USER_ID)));
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

         */
    }

    public ArrayList<UserModel> readListUserProfile(){

        return (ArrayList<UserModel>)
        userModelDao.loadAll();
        /*
        Cursor cursor = G.DB.rawQuery("SELECT * FROM " + Const.DB.DB_TABLE_USER ,null);
        ArrayList<UserModel> userList = new ArrayList<>();
        try {
            while (cursor.moveToNext()){
                UserModel userModel = new UserModel();
                userModel.setId(cursor.getLong(cursor.getColumnIndex(Const.DB.TABLE_USER_ID)));
                userModel.setUserName(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_USER_USERNAME)));
                userModel.setPassword(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_USER_PASSWORD)));
                userModel.setAge(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_USER_AGE)));
                userModel.setEmail(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_USER_EMAIL)));
                userModel.setIsLoggedIn(cursor.getInt(cursor.getColumnIndex(Const.DB.TABLE_USER_IS_LOGGED_IN)));
                userModel.setIsAdmin(cursor.getInt(cursor.getColumnIndex(Const.DB.TABLE_USER_IS_ADMIN)));
                userModel.setRegisterDate(cursor.getString(cursor.getColumnIndex(Const.DB.TABLE_USER_REGISTER_DATE)));
                userModel.setImageUser(cursor.getBlob(cursor.getColumnIndex(Const.DB.TABLE_USER_IMAGE)));
                userList.add(userModel) ;
            }
            if (cursor != null){
                cursor.close();
            }
        }catch (Exception e){

        }

        return userList;

         */
    }

    public long getProfilesCount(long userId) {
        return taskModelDao.queryBuilder()
                .where(TaskModelDao.Properties.UserId.eq(userId)).count();
        /*
        String countQuery = "SELECT  * FROM " + Const.DB.DB_TABLE_TASK + " WHERE "
                + Const.DB.TABLE_TASK_USER_ID + " = " + userId ;
        Cursor cursor = G.DB.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;

         */
    }

    public boolean updateUser(UserModel userModel){
        try {
            userModelDao.update(userModel);
            return true;
        }catch (Exception e){
            return false ;
        }
        /*
        ContentValues contentValues = new ContentValues();
        boolean isUpdated  ;
        byte[] image = getBitmapAsByteArray(imageUser);
        contentValues.put(Const.DB.TABLE_USER_PASSWORD , userModel.getPassword());
        contentValues.put(Const.DB.TABLE_USER_EMAIL , userModel.getEmail());
        contentValues.put(Const.DB.TABLE_USER_AGE , userModel.getAge());
        contentValues.put(Const.DB.TABLE_USER_IS_ADMIN , userModel.getIsAdmin());
        contentValues.put(Const.DB.TABLE_USER_IMAGE , userModel.getImageUser());

        isUpdated = G.DB.update(Const.DB.DB_TABLE_USER , contentValues , Const.DB.TABLE_USER_ID + " = " + userId , null
         ) > 0;

        return isUpdated ;

         */
    }

    public boolean deleteUser(long userId){
        UserModel oldUserModel = userModelDao.queryBuilder()
                .where(UserModelDao.Properties.Id.eq(userId))
                .unique();

        try {
            userModelDao.delete(oldUserModel);
          DeleteQuery<TaskModel> deleteQuery = taskModelDao.queryBuilder()
                    .where(TaskModelDao.Properties.UserId.eq(userId))
                    .buildDelete();
            deleteQuery.executeDeleteWithoutDetachingEntities();
            daoSession.clear();
            return true;
        }catch (Exception e) {
            return false;
        }
        /*
        boolean isUserDeleted ;
        boolean isTaskDeleted ;

         isTaskDeleted = G.DB.delete(Const.DB.DB_TABLE_TASK , Const.DB.TABLE_TASK_USER_ID + " = ?" , new String[]{String.valueOf(userId)})> 0;
         isUserDeleted = G.DB.delete(Const.DB.DB_TABLE_USER , Const.DB.TABLE_USER_ID + " = ?" , new String[]{String.valueOf(userId)})> 0;
         if (isTaskDeleted && isUserDeleted) return true ;
        return false;

         */
    }

    public File getImageFile(UUID uuid){
        return new File(global.getContext().getFilesDir() , "IMG_" +uuid+".jpg");
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
    public ArrayList<TaskModel> searchFunction(String searchText , String status , boolean isAdmin ,
                                               long userId){
        ArrayList<TaskModel> searchedList = (ArrayList<TaskModel>) taskModelDao.queryBuilder().whereOr(TaskModelDao.Properties.Title
                        .like("%" + searchText + "%") ,
                TaskModelDao.Properties.Description.like("%" + searchText + "%"))
                .where(TaskModelDao.Properties.Status.eq(status))
                .list();
        if (searchText.equals("")){
            if (isAdmin) return readTask(status);
            else return readTask(status , userId);
        }else {
            return searchedList;
        }
    }
}
