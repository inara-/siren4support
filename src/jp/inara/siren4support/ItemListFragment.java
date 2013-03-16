
package jp.inara.siren4support;

import java.util.List;

import jp.inara.siren4suport.R;
import jp.inara.siren4support.database.Item;
import jp.inara.siren4support.database.ItemDAO;
import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.widget.SearchView;

/**
 * アイテムリストを表示する
 * 
 * @author inara
 */
public class ItemListFragment extends SherlockListFragment implements LoaderCallbacks<List<Item>>,
        OnNavigationListener {

    private static final int ITEM_DATA_LOADER_ID = 0;
    private static final int ITEM_SEARCH_LOADER_ID = 1;
    private ItemListAdapter mAdapter;
    private ItemDataLoader mLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText(getText(R.string.no_item_data));

        mAdapter = new ItemListAdapter(getSherlockActivity(), android.R.layout.simple_list_item_1);
        setListAdapter(mAdapter);

        setListShown(false);
        
        // ActionBarにドロップダウンを設定
        initDropDownActionBar();

        LoaderManager lm = getActivity().getSupportLoaderManager();
        Bundle args = new Bundle();
        args.putInt("type", 1);
        lm.initLoader(ITEM_DATA_LOADER_ID, args, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Item item = mAdapter.getItem(position);
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
    public Loader<List<Item>> onCreateLoader(int id, Bundle args) {
        
        switch (id) {
            case ITEM_DATA_LOADER_ID:
                int type = args.getInt("type");
                mLoader = new ItemDataLoader(getActivity(), type);
                break;
            case ITEM_SEARCH_LOADER_ID:
                
            default:
                break;
        }

        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<Item>> loader, List<Item> data) {
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
    public void onLoaderReset(Loader<List<Item>> loader) {
        mAdapter.setData(null);
    }

    /**
     * リスト表示に使用しているローダーを再読み込みする
     */
    public void loderContentChanged() {
        mLoader.onContentChanged();
    }

    /**
     * DropDown形式のActionBarを設定する
     */
    public void initDropDownActionBar() {

        String[] typeList = {
                "巻物", "草", "腕輪", "札", "壺", "杖"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getSherlockActivity(),
                R.layout.sherlock_spinner_dropdown_item, typeList);

        ActionBar actionBar = getSherlockActivity().getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(adapter, this);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        mLoader.setType(itemPosition + 1);
        mLoader.forceLoad();
        setListShown(false);
        return false;
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main, menu);
        
        // 検索メニュー用の設定
        Activity activity = getActivity();
        SearchManager searchManager = (SearchManager)activity.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)menu.findItem(R.id.menu_search).getActionView();
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(activity.getComponentName());
        searchView.setSearchableInfo(searchableInfo);
        super.onCreateOptionsMenu(menu, inflater);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
