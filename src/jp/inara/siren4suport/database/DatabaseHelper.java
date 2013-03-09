
package jp.inara.siren4suport.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DatabaseHelper extends SQLiteOpenHelper {

    /** データベース名 */
    private static final String DATABASE_NAME = "Siren4DB";
    /** データベースバージョン */
    private static final int DATABASE_VERSION = 1;
    /** {@link DatabaseHelper}のインスタンス */
    private static DatabaseHelper mDatabaseHelper;

    /**
     * コンストラクタ
     * 
     * @param context 親クラスのコンテキスト
     * @param factory {@link CursorFactory}
     */
    private DatabaseHelper(Context context, CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    
    public static DatabaseHelper getInstance(Context context) {
        if(mDatabaseHelper == null) {
            mDatabaseHelper = new DatabaseHelper(context, null);
        }
        return mDatabaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            // アイテム用テーブル作成
            db.execSQL("create table item (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "type INTEGER NOT NULL, " +
                    "name INTEGER NOT NULL, " +
                    "is_identify INTEGER NOT NULL," +
                    "item_id INTEGER NOT NULL)");
            
            // 価格用テーブル作成
            db.execSQL("create table item_price (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "item_id INTEGER NOT NULL, " +
                    "use_count INTEGER NOT NULL, " +
                    "selling_price INTEGER NOT NULL, " +
                    "buying_price INTEGER NOT NULL)");
            
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
