package jp.inara.siren4suport.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jp.inara.siren4suport.util.CSVParser;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * アイテムテーブル用のDAO
 * @author inara
 *
 */
public class ItemDAO {
    
    private static final String LOG_TAG = "ItemDAO";
    
    /** DB接続用Helper */
    private DatabaseHelper mHelper;
    
    /**
     * コンストラクタ
     * @param context 親クラスのコンテキスト
     */
    public ItemDAO(Context context) {
        mHelper = DatabaseHelper.getInstance(context);
    }
    
    /**
     * アイテムデータを登録する
     * @param item アイテム
     * @return 登録成功:true 登録失敗:false
     */
    public boolean insert(Item item) {
        long rowId;
        SQLiteDatabase db = mHelper.getWritableDatabase();
        try {
            ContentValues values = getContentValues(item);
            rowId = db.insert("item", null, values);

            if (rowId < 0) {
                Log.v(LOG_TAG, "No Insert Data.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            db.close();
        }
        return true;
    }
    
    /**
     * アイテム価格データを登録する。Transaction管理は引数のSQLiteDatabaseで
     * 行われている前提。そのためSQLiteDatabaseをクローズしていない。
     * @param item アイテム
     * @param db {@link SQLiteDatabase}オブジェクト
     * @return 登録成功:true 登録失敗:false
     */
    public boolean insert(Item item, SQLiteDatabase db) {
        long rowId;
        try {
            ContentValues values = getContentValues(item);
            rowId = db.insert("item", null, values);
            if (rowId < 0) {
                Log.v(LOG_TAG, "No Insert Data.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } 
        return true;
    }
    
    /**
     * アイテムデータを更新する
     * @param item Itemオブジェクト
     * @return 更新成功:true、更新失敗:false
     */
    public boolean updateByItemId(Item item) {
        long rows;
        SQLiteDatabase db = mHelper.getWritableDatabase();
        try {
            ContentValues values = getContentValues(item);
            int itemId = item.getId();
            String[] whereArgs = new String[] {String.valueOf(itemId)};
            rows = db.update("item", values, "item_id = ?", whereArgs);
            if (rows < 0) {
                Log.v(LOG_TAG, "No Update Data.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            db.close();
        }
        return true;
    }
    
    /**
     * 全てのアイテムを取得する
     * @return Itemオブジェクトのリスト
     */
    public List<Item> selectAll() {
        Cursor cursor = null;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select * from item order by type";
        List<Item> items = new ArrayList<Item>();
        try {
            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                Item item = createItemFromCursor(cursor);
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
            if (cursor != null) {
                cursor.close();
            }
        }
        return items;
    }
    
    public List<Item> selectByType(int type) {
        Cursor cursor = null;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select * from item where type = ? order by type";
        List<Item> items = new ArrayList<Item>();
        try {
            String[] args = new String[] {String.valueOf(type)};
            cursor = db.rawQuery(sql, args);
            while (cursor.moveToNext()) {
                Item item = createItemFromCursor(cursor);
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
            if (cursor != null) {
                cursor.close();
            }
        }
        return items;
    }
    

    
    /**
     * IRecordからDB登録用にContentValuesにデータを取り出す
     * @param item アイテム
     */
    private ContentValues getContentValues(Item item) {
        ContentValues values = new ContentValues();
        values.put("item_id", item.getId());
        values.put("name", item.getName());
        values.put("type", item.getType());
        values.put("is_identify", item.isIdentify() ? 1 : 0);
        return values;
    }
    
    /**
     * CusorからItemオブジェクトを生成する
     * @param cursor itemテーブルに対してのカーソル
     * @return Itemオブジェクト
     */
    private Item createItemFromCursor(Cursor cursor) {
        Item item = new Item();
        item.setId(cursor.getInt(cursor.getColumnIndex("item_id")));
        item.setName(cursor.getString(cursor.getColumnIndex("name")));
        item.setType(cursor.getInt(cursor.getColumnIndex("type")));
        int isIdentify = cursor.getInt(cursor.getColumnIndex("is_identify"));
        item.setIdentify(isIdentify == 0 ? false : true);
        return item;
    }
    
    /**
     * CSVからアイテムのデータを作成する
     * 初回起動時のみ実行される
     * @param context
     */
    public void createInitData(Context context) {
        final AssetManager am = context.getAssets();
        InputStream in = null;
        try {
            in = am.open("item.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSVParser parser = new CSVParser(in);
        List<String> itemList = null;
        while((itemList = parser.readLine()) != null) {
            Item item = new Item();
            item.setId(Integer.valueOf(itemList.get(0)));
            item.setName(itemList.get(1));
            item.setType(Integer.valueOf(itemList.get(2)));
            item.setIdentify(false);
            insert(item);
        }
        Log.d(LOG_TAG, "Init Data End");
    }
}
