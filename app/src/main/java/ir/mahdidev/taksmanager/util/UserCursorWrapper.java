package ir.mahdidev.taksmanager.util;

import android.database.Cursor;
import android.database.CursorWrapper;

import ir.mahdidev.taksmanager.model.UserModel;

public class UserCursorWrapper extends CursorWrapper {
    public UserCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public String checkUserExists(){
        return getString(getColumnIndex(Const.DB.TABLE_USER_USERNAME)) ;
    }


    
}
