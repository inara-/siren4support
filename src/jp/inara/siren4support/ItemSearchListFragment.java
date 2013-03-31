
package jp.inara.siren4support;

import java.util.List;

import jp.inara.siren4support.R;
import jp.inara.siren4support.database.Item;
import jp.inara.siren4support.database.ItemDAO;
import jp.inara.siren4support.database.ItemPrice;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.widget.SearchView;

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
    private String mQueryString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        setEmptyText(getText(R.string.no_search_item));
        
        // クエリを取り出す。
        mQueryString = getActivity().getIntent().getStringExtra(SearchManager.QUERY);
        mAdapter = new ItemSearchListAdapter(getSherlockActivity(), android.R.layout.simple_list_item_1);
        setListAdapter(mAdapter);

        setListShown(false);

        LoaderManager lm = getActivity().getSupportLoaderManager();
        Bundle args = new Bundle();
        args.putInt("type", 1);
        lm.initLoader(SEARCH_LOADER_ID, args, this);
        getListView().requestFocus();
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
            mLoader = new ItemSearchLoader(getActivity(), Integer.valueOf(mQueryString));
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
        setListShown(false);
        mLoader.onContentChanged();
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_search, menu);
        
        // SearchViewを開いた状態にしてクエリを入力した状態にする。
        final MenuItem searchItem = menu.findItem(R.id.menu_search);  
        searchItem.expandActionView();
        SearchView searchView = (SearchView) searchItem.getActionView();
        String query = getActivity().getIntent().getStringExtra(SearchManager.QUERY);
        searchView.setQuery(query, false);
        
        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getActivity().getComponentName());
        searchView.setSearchableInfo(searchableInfo);
        
        // SearchViewを閉じた時にActivityを終了する。
        searchItem.setOnActionExpandListener(new OnActionExpandListener() {     
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // 何もしないよ。
                return false;
            }
            
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                getActivity().finish();
                return true;
            }
        });
        
        super.onCreateOptionsMenu(menu, inflater);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    
    /**
     * アイテムを検索する
     * @param queryString
     */
    public void search(String queryString) {
        mQueryString = queryString;
        mLoader.setPrice(Integer.valueOf(mQueryString));
        loderContentChanged();
        mAdapter.notifyDataSetChanged();
    }
    
}
