
package jp.inara.siren4support;

import java.util.ArrayList;
import java.util.List;

import jp.inara.siren4support.database.ItemPrice;
import jp.inara.siren4support.database.ItemPriceDAO;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class ItemSearchLoader extends AsyncTaskLoader<List<ItemPrice>> {

    private static final String LOG_TAG = "ItemSearchLoader";
    private List<ItemPrice> mItems;
    private int mPrice;

    public ItemSearchLoader(Context context, int price) {
        super(context);
        setPrice(price);
    }

    @Override
    public List<ItemPrice> loadInBackground() {

        ItemPriceDAO dao = new ItemPriceDAO(getContext());
        // 通常価格
        List<ItemPrice> normalPrices = dao.selectByItemPrice(String.valueOf(getPrice()),
                ItemPrice.PRICE_TYPE_NORMAL);

        // 祝福価格
        int blessingPrice = (int) Math.round(getPrice() / 1.1);
        Log.d(LOG_TAG, String.format("blessingPrice = %d", blessingPrice));
        List<ItemPrice> blessingPrices = dao.selectByItemPrice(String.valueOf(blessingPrice),
                ItemPrice.PRICE_TYPE_BRESSING);

        // 呪い・封印価格
        int cursePrice = (int) Math.round(getPrice() / 0.8);
        Log.d(LOG_TAG, String.format("cursePrice = %d", cursePrice));
        List<ItemPrice> cursePrices = dao.selectByItemPrice(String.valueOf(cursePrice),
                ItemPrice.PRICE_TYPE_CURSE);
        
        // 結果の結合
        List<ItemPrice> itemPrices = new ArrayList<ItemPrice>();
        itemPrices.addAll(normalPrices);
        itemPrices.addAll(blessingPrices);
        itemPrices.addAll(cursePrices);

        return itemPrices;
    }

    /**
     * 提供する新しいデータがある場合に呼び出される
     * 
     * @param records レコードのリスト
     */
    @Override
    public void deliverResult(List<ItemPrice> items) {
        if (isReset()) {
            // リセット時
            return;
        }
        mItems = items;
        if (isStarted()) {
            // データの読込中
            super.deliverResult(items);
        }
    }

    /**
     * ローダーの開始
     */
    @Override
    protected void onStartLoading() {
        if (mItems != null) {
            // データが取得済みの場合それを返す
            deliverResult(mItems);
        } else {
            // 取得できていない場合読み込む
            forceLoad();
        }
    }

    /**
     * ローダーの停止
     */
    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    /**
     * キャンセル処理
     * 
     * @param records レコードリスト
     */
    @Override
    public void onCanceled(List<ItemPrice> items) {
        super.onCanceled(items);
    }

    /**
     * ローダーのリセット
     */
    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mItems != null) {
            mItems = null;
        }
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }
}
