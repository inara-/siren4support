package jp.inara.siren4suport;

import java.util.List;

import jp.inara.siren4suport.database.ItemPrice;
import jp.inara.siren4suport.database.ItemPriceDAO;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class ItemSearchLoader extends AsyncTaskLoader<List<ItemPrice>> {

    private List<ItemPrice> mItems;
    private int mPrice;
    
    public ItemSearchLoader(Context context, int price) {
        super(context);
        mPrice = price;
    }

    @Override
    public List<ItemPrice> loadInBackground() {
        
        ItemPriceDAO dao = new ItemPriceDAO(getContext());
        List<ItemPrice> itemPrices = dao.selectByItemPrice(String.valueOf(mPrice));
        return itemPrices;
    }
    
    /**
     * 提供する新しいデータがある場合に呼び出される
     * @param records レコードのリスト
     */
    @Override
    public void deliverResult(List<ItemPrice> items) {
        if(isReset()){
            // リセット時
            return;
        }
        mItems = items;
        if(isStarted()) {
            // データの読込中
            super.deliverResult(items);
        }
    }
    
    /**
     * ローダーの開始
     */
    @Override
    protected void onStartLoading() {
        if(mItems != null){
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
}
