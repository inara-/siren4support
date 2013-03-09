
package jp.inara.siren4suport;

import java.util.List;

import jp.inara.siren4suport.database.Item;
import jp.inara.siren4suport.database.ItemDAO;
import jp.inara.siren4suport.database.ItemPrice;
import android.app.SearchManager;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;

/**
 * アイテム検索結果のリストを表示する
 * 
 * @author inara
 */
public class ItemSearchListFragment extends SherlockListFragment implements LoaderCallbacks<List<ItemPrice>>
{
    private static final int SEARCH_LOADER_ID = 1;
    private static final String LOG_TAG = "ItemPriceList";
    private ItemSearchListAdapter mAdapter;
    private ItemSearchLoader mLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(LOG_TAG, "onActivityCreated");
        setEmptyText(getText(R.string.no_search_item));

        mAdapter = new ItemSearchListAdapter(getSherlockActivity(), android.R.layout.simple_list_item_1);
        setListAdapter(mAdapter);

        setListShown(false);

        LoaderManager lm = getActivity().getSupportLoaderManager();
        Bundle args = new Bundle();
        args.putInt("type", 1);
        lm.initLoader(SEARCH_LOADER_ID, args, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ItemPrice itemPrice = mAdapter.getItem(position);
        Item item = itemPrice.getItem();
        // 識別済みフラグを反転
        item.setIdentify(!item.isIdentify());

        // DBへ保存
        ItemDAO dao = new ItemDAO(getActivity());
        if (!dao.updateByItemId(item)) {
            Toast.makeText(getActivity(), "update failed", Toast.LENGTH_LONG).show();
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public Loader<List<ItemPrice>> onCreateLoader(int id, Bundle args) {
        if(id == SEARCH_LOADER_ID) {
            final String queryString = getActivity().getIntent().getStringExtra(SearchManager.QUERY);
            mLoader = new ItemSearchLoader(getActivity(), Integer.valueOf(queryString));
        }
        return mLoader;  
    }

    @Override
    public void onLoadFinished(Loader<List<ItemPrice>> loader, List<ItemPrice> data) {
        Log.d(LOG_TAG, "onLoadFinished");
        mAdapter.setData(data);
        if (isResumed()) {
            // リストを表示
            setListShown(true);
        } else {
            // 一時停止時はアニメーションなしで表示
            setListShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<ItemPrice>> loader) {
        mAdapter.setData(null);
    }

    /**
     * リスト表示に使用しているローダーを再読み込みする
     */
    public void loderContentChanged() {
        mLoader.onContentChanged();
    }
}
