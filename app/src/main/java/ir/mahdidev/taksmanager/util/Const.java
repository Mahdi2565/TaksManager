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
                Const.DB.TABLE_TASK_STATUS + " TEXT NOT NULL ) "
                ;
    }
    public static final String USER_MODEL_LOGGED_IN_INTENT_KEY = "ir.mahdidev.taksmanager.usermodel";
}
