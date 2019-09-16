package ir.mahdidev.taksmanager.util;

public class Const {

    public class DB {

        public static final String DB_NAME = "task_manager" ;
        public static final int DB_VERSION = 1;

        public static final String DB_TABLE_USER = "user"   ;
        public static final String DB_TABLE_TASK = "task"   ;

        public static final String TABLE_USER_IS_ADMIN   = "is_admin";
        public static final String TABLE_USER_ID = "id";
        public static final String TABLE_USER_USERNAME = "username";
        public static final String TABLE_USER_PASSWORD = "password";
        public static final String TABLE_USER_EMAIL    = "emial";
        public static final String TABLE_USER_AGE      = "age";
        public static final String TABLE_USER_IS_LOGGED_IN = "is_logged_in";
        public static final String TABLE_USER_REGISTER_DATE = "register_date";

        public static final String TABLE_TASK_ID = "id";
        public static final String TABLE_TASK_USER_ID = "user_id";
        public static final String TABLE_TASK_TITLE = "title";
        public static final String TABLE_TASK_DESCRIPTION = "description";
        public static final String TABLE_TASK_STATUS = "status";
        public static final String TABLE_TASK_DATE   = "date"  ;
        public static final String TABLE_TASK_TIME   = "time"  ;

        public static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + Const.DB.DB_TABLE_USER
                + " ( " + Const.DB.TABLE_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                Const.DB.TABLE_USER_IS_ADMIN + " INTEGER DEFAULT (0) , " +
                Const.DB.TABLE_USER_USERNAME + " TEXT    NOT NULL , " +
                Const.DB.TABLE_USER_PASSWORD + " TEXT    NOT NULL , " +
                Const.DB.TABLE_USER_EMAIL + " TEXT    NOT NULL , " +
                Const.DB.TABLE_USER_AGE + " TEXT    NOT NULL , " +
                Const.DB.TABLE_USER_IS_LOGGED_IN + " INTEGER DEFAULT (0) , " +
                Const.DB.TABLE_USER_REGISTER_DATE + " TEXT NOT NULL )"
                ;
        public static final String CREATE_TASK_TABLE = "CREATE TABLE IF NOT EXISTS " +
                Const.DB.DB_TABLE_TASK + " ( " +
                Const.DB.TABLE_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                Const.DB.TABLE_TASK_USER_ID + " INTEGER NOT NULL , " +
                Const.DB.TABLE_TASK_TITLE + " TEXT , "+
                Const.DB.TABLE_TASK_DESCRIPTION + " TEXT , " +
                Const.DB.TABLE_TASK_STATUS + " TEXT NOT NULL , "
                + DB.TABLE_TASK_DATE + " TEXT NOT NULL , " +
                DB.TABLE_TASK_TIME + " TEXT NOT NULL ) "
                ;
    }
    public static final String USER_MODEL_LOGGED_IN_INTENT_KEY = "ir.mahdidev.taksmanager.usermodel";
    public static final String STATUS_BUNDLE_KEY = "ir.mahdidev.taksmanager.status.bundle.key";
    public static final String USER_ID_BUNDLE_KEY = "ir.mahdidev.taksmanager.userid";
    public static final String MODE_TASK_BUNDLE_KEY = "ir.mahdidev.taksmanager.mode.task";
    public static final String  EDIT_FRAGMENT_TASK_ID_BUNDLE_KEY = "ir.mahdidev.taksmanager.task.id.bundle.key";
    public static final String  EDIT_FRAGMENT_USER_ID_BUNDLE_KEY = "ir.mahdidev.taksmanager.edit.fragment.user.id.bundle.key";
    public static final String  ADD_FRAGMENT_USER_ID_BUNDLE_KEY = "ir.mahdidev.taksmanager.add.fragment.user.id.bundle.key";
    public static final String  TASK_DIALOG_FRAGMENT_TASK_ID_BUNDLE_KEY = "ir.mahdidev.taksmanager.task.id.dialog.task.fragment.bundle.key";
    public static final String  TASK_DIALOG_FRAGMENT_USER_ID_BUNDLE_KEY = "ir.mahdidev.taksmanager.user.id.dialog.task.fragment.bundle.key";
    public static final String  EDIT_DIALOG_FRAGMENT_TAG = "ir.mahdidev.taksmanager.edit.dialog.fragment.tag";
    public static final String  ADD_DIALOG_FRAGMENT_TAG = "ir.mahdidev.taksmanager.add.dialog.fragment.tag";
    public static final String  DELETE_ALL_TASK_DIALOG_FRAGMENT_TAG = "ir.mahdidev.taksmanager.add.delete.all.task.dialog.fragment.tag";
    public static final String  ADD_FRAGMENT_TAG = "ir.mahdidev.taksmanager.add.fragment.tag";
    public static final String  EDIT_FRAGMENT_TAG = "ir.mahdidev.taksmanager.edit.fragment.tag";
    public static final String  DATE_PICKER_FRAGMENT = "ir.mahdidev.taksmanager.date.picker.fragment";
    public static final String  DATE_PICKER_FRAGMENT_BUNDLE_KEY = "ir.mahdidev.taksmanager.date.picker.fragment.bundle.key";
    public static final String  TIME_PICKER_FRAGMENT = "ir.mahdidev.taksmanager.time.picker.fragment";
    public static final String  TIME_PICKER_FRAGMENT_BUNDLE_KEY = "ir.mahdidev.taksmanager.time.picker.fragment.bundle.onactivity.result";
    public static final String  DELETE_FRAGMENT_TASK_ID_BUNDLE_KEY = "ir.mahdidev.taksmanager.delete.fragment.taskid.bundle.key";
    public static final String  DELETE_FRAGMENT_USER_ID_BUNDLE_KEY = "ir.mahdidev.taksmanager.delete.fragment.userid.bundle.key";
    public static final String  DELETE_ALL_TASK_FRAGMENT_USER_ID_BUNDLE_KEY = "ir.mahdidev.taksmanager.delete.all.task.fragment.userid.bundle.key";
    public static final int Add_TASK_MODE = 1;
    public static final int EDIT_TASK_MODE = 0;
    public static final int DELETE_TASK_MODE = 2;
    public static final int TASK_DIALOG_DEFAULT_TASK_ID = 0;
    public static final int TASK_DIALOG_DEFAULT_USER_ID = 0;
    public static final int TARGET_REQUSET_CODE_DATE_PICKER_FRAGMENT = 150;
    public static final int TARGET_REQUSET_CODE_TIME_PICKER_FRAGMENT = 250;
    public static final int TARGET_REQUSET_CODE_EDIT_FRAGMENT_FRAGMENT = 350;
    public static final int TARGET_REQUSET_CODE_DELETE_FRAGMENT_FRAGMENT = 450;
    public static final int TARGET_REQUSET_CODE_DELETE_ALL_FRAGMENT_FRAGMENT = 550;

}
