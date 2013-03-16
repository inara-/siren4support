
package jp.inara.siren4support.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jp.inara.siren4support.util.CSVParser;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 価格テーブル用のDAO
 * 
 * @author inara
 */
public class ItemPriceDAO {

    private static final String LOG_TAG = "ItemPricesDAO";

    /** DB接続用Helper */
    private DatabaseHelper mHelper;

    /**
     * コンストラクタ
     * 
     * @param context 親クラスのコンテキスト
     */
    public ItemPriceDAO(Context context) {
        mHelper = DatabaseHelper.getInstance(context);
    }

    /**
     * アイテム価格データを登録する
     * 
     * @param itemPrice アイテム価格
     * @return 登録成功:true 登録失敗:false
     */
    public boolean insert(ItemPrice itemPrice) {
        long rowId;
        SQLiteDatabase db = mHelper.getWritableDatabase();
        try {
            ContentValues values = getContentValues(itemPrice);
            rowId = db.insert("item_price", null, values);
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
     * IRecordからDB登録用にContentValuesにデータを取り出す
     * 
     * @param itemPrice アイテム価格
     */
    private ContentValues getContentValues(ItemPrice itemPrice) {
        ContentValues values = new ContentValues();
        values.put("item_id", itemPrice.getItemId());
        values.put("use_count", itemPrice.getUseCount());
        values.put("selling_price", itemPrice.getSellingPrice());
        values.put("buying_price", itemPrice.getBuyingPrice());
        return values;
    }

    /**
     * アイテム価格データを登録する。Transaction管理は引数のSQLiteDatabaseで
     * 行われている前提。そのためSQLiteDatabaseをクローズしていない。
     * 
     * @param itemPrice アイテム価格
     * @param db {@link SQLiteDatabase}オブジェクト
     * @return 登録成功:true 登録失敗:false
     */
    public boolean insert(ItemPrice itemPrice, SQLiteDatabase db) {
        long rowId;
        try {
            ContentValues values = getContentValues(itemPrice);
            rowId = db.insert("item_price", null, values);
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
     * CusorからItemPriceオブジェクトを生成する
     * 
     * @param cursor itemテーブルに対してのカーソル
     * @param priceType 通常・祝福・呪いのどれか
     * @return Itemオブジェクト
     */
    private ItemPrice createFromCursor(Cursor cursor, int priceType) {
        ItemPrice itemPrice = new ItemPrice();
        itemPrice.setItemId(cursor.getInt(cursor.getColumnIndex("item_id")));
        itemPrice.setUseCount(cursor.getInt(cursor.getColumnIndex("use_count")));
        itemPrice.setBuyingPrice(cursor.getInt(cursor.getColumnIndex("buying_price")));
        itemPrice.setSellingPrice(cursor.getInt(cursor.getColumnIndex("selling_price")));
        itemPrice.setPriceType(priceType);
        
        Item item = new Item();
        item.setId(cursor.getInt(cursor.getColumnIndex("item_id")));
        item.setName(cursor.getString(cursor.getColumnIndex("name")));
        item.setType(cursor.getInt(cursor.getColumnIndex("type")));
        int isIdentify = cursor.getInt(cursor.getColumnIndex("is_identify"));
        item.setIdentify(isIdentify == 0 ? false : true);
        
        itemPrice.setItem(item);
        
        return itemPrice;
    }
    
    /**
     * 入力された金額からアイテムを検索する。
     * @param priceString 金額の文字列
     * @param priceType 通常・祝福・呪いのどれか
     * @return
     */
    public List<ItemPrice> selectByItemPrice(String priceString, int priceType) {
        Cursor cursor = null;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select item.*, item_price.* from item inner join item_price on item.item_id = item_price.item_id where selling_price = ? or buying_price = ? order by type";
        List<ItemPrice> items = new ArrayList<ItemPrice>();
        try {
            String[] args = new String[] {priceString, priceString};
            cursor = db.rawQuery(sql, args);
            while (cursor.moveToNext()) {
                ItemPrice item = createFromCursor(cursor, priceType);
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
     * CSVからアイテムのデータを作成する 初回起動時のみ実行される
     * 
     * @param context
     */
    public void createInitData(Context context) {
        final AssetManager am = context.getAssets();
        InputStream in = null;
        try {
            in = am.open("item_price.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSVParser parser = new CSVParser(in);
        List<String> itemList = null;
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            while ((itemList = parser.readLine()) != null) {
                ItemPrice itemPrice = new ItemPrice();
                itemPrice.setItemId(Integer.valueOf(itemList.get(0)));
                itemPrice.setUseCount(Integer.valueOf(itemList.get(1)));
                itemPrice.setSellingPrice(Integer.valueOf(itemList.get(2)));
                itemPrice.setBuyingPrice(Integer.valueOf(itemList.get(3)));
                insert(itemPrice, db);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        Log.d(LOG_TAG, "Init Data End");
    }

}
