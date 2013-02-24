package jp.inara.siren4suport;

import java.util.List;

import jp.inara.siren4suport.database.Item;
import jp.inara.siren4suport.database.ItemDAO;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;

public class ItemDataLoader extends AsyncTaskLoader<List<Item>> {

    private List<Item> mItems;
    private SharedPreferences mPreferences;
    private int mType;
    
    public ItemDataLoader(Context context, int type) {
        super(context);
        mType = type;
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    @Override
    public List<Item> loadInBackground() {
        // 初回の読み込みがまだの場合、初回データを作成する
        boolean isFinishInit = mPreferences.getBoolean("is_finish_init", false);
        if (!isFinishInit) {
            createInitData();
        }
        ItemDAO dao = new ItemDAO(getContext());
        List<Item> items = dao.selectByType(mType);
        return items;
    }
    
    private void createInitData() {
        ItemDAO dao = new ItemDAO(getContext());
        dao.createInitData(getContext());
        Editor editor = mPreferences.edit();
        editor.putBoolean("is_finish_init", true);
        editor.commit();
    }
    
    /**
     * 提供する新しいデータがある場合に呼び出される
     * @param records レコードのリスト
     */
    @Override
    public void deliverResult(List<Item> items) {
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
    public void onCanceled(List<Item> items) {
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
    
    public void setType(int type) {
        mType = type;
    }
}
