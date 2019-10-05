package ir.mahdidev.taksmanager.util;

import android.content.Context;

import ir.mahdidev.taksmanager.model.DaoMaster;

public class GreenDaoHelper extends DaoMaster.OpenHelper {
    public GreenDaoHelper(Context context, String name) {
        super(context, name);
    }
}
