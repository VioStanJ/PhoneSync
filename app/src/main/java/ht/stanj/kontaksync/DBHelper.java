package ht.stanj.kontaksync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE = "create table "+DBContact.TABLE_NAME+
            " (id integer primary key autoincrement,"+DBContact.NAME+" text,"+DBContact.PHONE+" text,status integer)";

    private static final String DROP_TABLE = "drop table if exists "+DBContact.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DBContact.DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public void save(SQLiteDatabase db,Contact contact){
        ContentValues values = new ContentValues();
        values.put(DBContact.NAME,contact.getName());
        values.put(DBContact.PHONE,contact.getPhone());
        values.put(DBContact.STATUS,contact.isStatus()?DBContact.SYNC_STATUS_OK:DBContact.SYNC_STATUS_FAILED);

        db.insert(DBContact.TABLE_NAME,null,values);
    }

    public ArrayList<Contact> getAll(){
        ArrayList<Contact> contactList = new ArrayList<Contact>();

        String selectQuery = "SELECT  * FROM " + DBContact.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhone(cursor.getString(2));
                contact.setStatus(cursor.getInt(3)==1?true:false);

                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        return contactList;
    }

    public void update(SQLiteDatabase db,Contact contact){

        String sql = "update "+DBContact.TABLE_NAME+" set name='"+contact.getName()+"',phone='"+contact.getPhone()+"'," +
                "status="+(contact.isStatus()==true?1:0)+" where id="+contact.getId()+"";

        db.execSQL(sql);
    }
}
